package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    private DatabaseManager db;

    public CommandHandler(DatabaseManager db) {
        this.db = db;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Nur Spieler dürfen diesen Command ausführen
        if (!(sender instanceof Player)) {
            sender.sendMessage("Dieser Befehl kann nur von einem Spieler ausgeführt werden.");
            return true;
        }
        Player player = (Player) sender;

        // Es muss mindestens ein Argument vorhanden sein
        if (args.length == 0) {
            player.sendMessage("Verwendung: /pelldata <stats|ranking|player>");
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "ranking":
                if (args.length < 2) {
                    player.sendMessage("Verwendung: /pelldata ranking <placed|broken|killed>");
                    return true;
                }
                String rankingType = args[1].toLowerCase();
                if (!rankingType.equals("placed") && !rankingType.equals("broken") && !rankingType.equals("killed")) {
                    player.sendMessage("Verwendung: /pelldata ranking <placed|broken|killed>");
                    return true;
                }
                showRanking(player, rankingType);
                break;

            case "player":
                if (args.length < 2) {
                    player.sendMessage("Verwendung: /pelldata player <PLAYERNAME>");
                    return true;
                }
                showPlayerStats(player, args[1]);
                break;

            case "stats":
                if (args.length < 2) {
                    player.sendMessage("Verwendung: /pelldata stats <blocks|killed|all>");
                    return true;
                }
                String statsType = args[1].toLowerCase();
                if (statsType.equals("blocks")) {
                    showBlockStats(player);
                } else if (statsType.equals("killed")) {
                    showMobKills(player);
                } else if (statsType.equals("all")) {
                    showAllStats(player);
                } else {
                    player.sendMessage("Verwendung: /pelldata stats <blocks|killed|all>");
                }
                break;

            default:
                player.sendMessage("Unbekannter Subcommand! Nutze: /pelldata <stats|ranking|player>");
                break;
        }
        return true;
    }

    // Zeigt die eigenen Blockstatistiken an
    private void showBlockStats(Player player) {
        String uuid = player.getUniqueId().toString();
        int placed = db.getBlocksPlaced(uuid);
        int broken = db.getBlocksBroken(uuid);
        player.sendMessage("§6Deine Block-Statistiken:");
        player.sendMessage("§eBlöcke gesetzt: §f" + placed);
        player.sendMessage("§eBlöcke abgebaut: §f" + broken);
    }

    // Zeigt die eigenen Mob-Kill-Statistiken an
    private void showMobKills(Player player) {
        String uuid = player.getUniqueId().toString();
        int mobsKilled = db.getMobsKilled(uuid);
        player.sendMessage("§6Deine Mob-Kill-Statistiken:");
        player.sendMessage("§eMobs getötet: §f" + mobsKilled);
    }

    // Zeigt beide Kategorien zusammen an
    private void showAllStats(Player player) {
        String uuid = player.getUniqueId().toString();
        int placed = db.getBlocksPlaced(uuid);
        int broken = db.getBlocksBroken(uuid);
        int mobsKilled = db.getMobsKilled(uuid);
        player.sendMessage("§6Deine gesamten Statistiken:");
        player.sendMessage("§eBlöcke gesetzt: §f" + placed);
        player.sendMessage("§eBlöcke abgebaut: §f" + broken);
        player.sendMessage("§eMobs getötet: §f" + mobsKilled);
    }

    // Zeigt das Ranking der Top 10 Spieler für einen gegebenen Typ an
    private void showRanking(Player player, String type) {
        player.sendMessage("§6Top 10 Spieler - Ranking nach " + type + ":");
        for (int i = 0; i < 10; i++) {
            String[] topEntry = db.getTopStats(type, i);
            if (topEntry != null) {
                String uuid = topEntry[0];
                String statCount = topEntry[1];
                OfflinePlayer op = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                String name = (op != null && op.getName() != null) ? op.getName() : uuid;
                player.sendMessage("§e#" + (i + 1) + " " + name + " §7- " + type + ": " + statCount);
            }
        }
    }

    // Zeigt alle Statistiken eines bestimmten Spielers an (mittels Spielernamen)
    private void showPlayerStats(Player sender, String targetName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (target == null || !target.hasPlayedBefore()) {
            sender.sendMessage("Der Spieler '" + targetName + "' wurde nicht gefunden.");
            return;
        }
        String targetUUID = target.getUniqueId().toString();
        int placed = db.getBlocksPlaced(targetUUID);
        int broken = db.getBlocksBroken(targetUUID);
        int killed = db.getMobsKilled(targetUUID);
        sender.sendMessage("§6Statistiken für " + target.getName() + ":");
        sender.sendMessage("§eBlöcke gesetzt: §f" + placed);
        sender.sendMessage("§eBlöcke abgebaut: §f" + broken);
        sender.sendMessage("§eMobs getötet: §f" + killed);
    }
}
