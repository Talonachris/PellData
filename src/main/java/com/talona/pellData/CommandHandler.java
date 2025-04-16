package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    private final DatabaseManager db;

    public CommandHandler(DatabaseManager db) {
        this.db = db;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Nur Spieler können diesen Befehl ausführen.");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§7Verwendung: /pelldata <stats|ranking|player|reset>");
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "stats" -> {
                if (args.length < 2) {
                    player.sendMessage("§7Verwendung: /pelldata stats <blocks|killed|deaths|all>");
                    return true;
                }
                switch (args[1].toLowerCase()) {
                    case "blocks" -> showBlockStats(player);
                    case "killed" -> showMobKills(player);
                    case "deaths" -> showDeaths(player);
                    case "all" -> showAllStats(player);
                    default -> player.sendMessage("§7Verwendung: /pelldata stats <blocks|killed|deaths|all>");
                }
            }

            case "ranking" -> {
                if (args.length < 2) {
                    player.sendMessage("§7Verwendung: /pelldata ranking <placed|broken|killed|deaths>");
                    return true;
                }
                String type = args[1].toLowerCase();
                if (!type.equals("placed") && !type.equals("broken") && !type.equals("killed") && !type.equals("deaths")) {
                    player.sendMessage("§7Ungültiger Typ. Verwende: placed, broken, killed, deaths");
                    return true;
                }
                showRanking(player, type);
            }

            case "player" -> {
                if (args.length < 2) {
                    player.sendMessage("§7Verwendung: /pelldata player <Spielername>");
                    return true;
                }
                showPlayerStats(player, args[1]);
            }

            case "reset" -> {
                if (!player.hasPermission("pelldata.reset")) {
                    player.sendMessage("§cDu hast keine Berechtigung für diesen Befehl.");
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage("§7Verwendung: /pelldata reset <Spielername>");
                    return true;
                }
                resetPlayerStats(player, args[1]);
            }

            default -> player.sendMessage("§7Unbekannter Subcommand. Nutze: /pelldata <stats|ranking|player|reset>");
        }

        return true;
    }

    private void showBlockStats(Player player) {
        String uuid = player.getUniqueId().toString();
        int placed = db.getBlocksPlaced(uuid);
        int broken = db.getBlocksBroken(uuid);
        player.sendMessage("§6Deine Block-Statistiken:");
        player.sendMessage("§eGesetzt: §f" + placed);
        player.sendMessage("§eAbgebaut: §f" + broken);
    }

    private void showMobKills(Player player) {
        String uuid = player.getUniqueId().toString();
        int kills = db.getMobsKilled(uuid);
        player.sendMessage("§6Deine Mob-Kill-Statistiken:");
        player.sendMessage("§eMobs getötet: §f" + kills);
    }

    private void showDeaths(Player player) {
        String uuid = player.getUniqueId().toString();
        int deaths = db.getDeaths(uuid);
        player.sendMessage("§6Deine Todes-Statistik:");
        player.sendMessage("§eTode: §f" + deaths);
    }

    private void showAllStats(Player player) {
        String uuid = player.getUniqueId().toString();
        int placed = db.getBlocksPlaced(uuid);
        int broken = db.getBlocksBroken(uuid);
        int kills = db.getMobsKilled(uuid);
        int deaths = db.getDeaths(uuid);

        player.sendMessage("§6Deine Gesamtstatistik:");
        player.sendMessage("§eGesetzt: §f" + placed);
        player.sendMessage("§eAbgebaut: §f" + broken);
        player.sendMessage("§eMobs getötet: §f" + kills);
        player.sendMessage("§eTode: §f" + deaths);
    }

    private void showRanking(Player player, String type) {
        player.sendMessage("§6Top 10 – " + type + ":");
        for (int i = 0; i < 10; i++) {
            String[] entry = db.getTopStats(type, i);
            if (entry != null) {
                String uuid = entry[0];
                String value = entry[1];
                OfflinePlayer p = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                String name = (p != null && p.getName() != null) ? p.getName() : uuid;
                player.sendMessage("§e#" + (i + 1) + " §f" + name + " §7– §a" + value);
            }
        }
    }

    private void showPlayerStats(Player sender, String targetName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (target == null) {
            sender.sendMessage("§cSpieler konnte nicht gefunden werden.");
            return;
        }

        String uuid = target.getUniqueId().toString();
        int placed = db.getBlocksPlaced(uuid);
        int broken = db.getBlocksBroken(uuid);
        int killed = db.getMobsKilled(uuid);
        int deaths = db.getDeaths(uuid);

        String name = (target.getName() != null) ? target.getName() : uuid;

        sender.sendMessage("§6Statistiken für §f" + name + ":");
        sender.sendMessage("§eGesetzt: §f" + placed);
        sender.sendMessage("§eAbgebaut: §f" + broken);
        sender.sendMessage("§eMobs getötet: §f" + killed);
        sender.sendMessage("§eTode: §f" + deaths);
    }

    private void resetPlayerStats(Player sender, String targetName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (target == null) {
            sender.sendMessage("§cSpieler konnte nicht gefunden werden.");
            return;
        }

        String uuid = target.getUniqueId().toString();
        boolean success = db.resetStats(uuid);

        if (success) {
            sender.sendMessage("§aStatistiken von §e" + (target.getName() != null ? target.getName() : uuid) + " §awurden zurückgesetzt.");
        } else {
            sender.sendMessage("§cFehler beim Zurücksetzen.");
        }
    }
}
