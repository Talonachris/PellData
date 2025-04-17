package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    private final DatabaseManager db;
    private final LocalesManager locales;

    public CommandHandler(DatabaseManager db, LocalesManager locales) {
        this.db = db;
        this.locales = locales;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(locales.get(null, "error.not_player"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(locales.getPrefixed(player, "help.usage"));
            return true;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "stats" -> {
                if (args.length < 2) {
                    player.sendMessage(locales.getPrefixed(player, "help.stats.usage"));
                    return true;
                }
                switch (args[1].toLowerCase()) {
                    case "blocks" -> showBlockStats(player.getUniqueId().toString(), player);
                    case "killed" -> showMobKills(player.getUniqueId().toString(), player);
                    case "pvp" -> showPvPKills(player.getUniqueId().toString(), player);
                    case "deaths" -> showDeaths(player.getUniqueId().toString(), player);
                    case "playtime" -> showPlaytime(player.getUniqueId().toString(), player);
                    case "chat" -> showChatMessages(player.getUniqueId().toString(), player);
                    case "all" -> showAllStats(player.getUniqueId().toString(), player);
                    case "topplaced" -> showTopPlaced(player);
                    case "topbroken" -> showTopBroken(player);
                    case "topmobs" -> showTopMobs(player);
                    default -> player.sendMessage(locales.getPrefixed(player, "error.unknown_subcommand"));
                }
            }
            case "ranking" -> {
                if (args.length < 2) {
                    player.sendMessage(locales.getPrefixed(player, "help.ranking.usage"));
                    return true;
                }
                String type = args[1].toLowerCase();
                if (!List.of("placed", "broken", "killed", "deaths", "playtime", "chat", "pvp").contains(type)) {
                    player.sendMessage(locales.getPrefixed(player, "error.invalid_type"));
                    return true;
                }
                showRanking(player, type);
            }

            case "player" -> {
                if (args.length < 2) {
                    player.sendMessage(locales.getPrefixed(player, "help.player.usage"));
                    return true;
                }
                showPlayerStats(player, args[1]);
            }

            case "reset" -> {
                if (!player.hasPermission("pelldata.reset")) {
                    player.sendMessage(locales.getPrefixed(player, "error.no_permission"));
                    return true;
                }
                if (args.length < 2) {
                    player.sendMessage(locales.getPrefixed(player, "help.reset.usage"));
                    return true;
                }
                resetPlayerStats(player, args[1]);
            }

            case "globalstats" -> {
                showGlobalStats(player);
            }

            default -> player.sendMessage(locales.getPrefixed(player, "error.unknown_subcommand"));
        }

        return true;
    }

    private void showBlockStats(String uuid, Player player) {
        int placed = db.getBlocksPlaced(uuid);
        int broken = db.getBlocksBroken(uuid);
        String msg = locales.getPrefixed(player, "stats.blocks")
                .replace("{placed}", String.valueOf(placed))
                .replace("{broken}", String.valueOf(broken));
        player.sendMessage(msg);
    }

    private void showMobKills(String uuid, Player player) {
        int kills = db.getMobsKilled(uuid);
        player.sendMessage(locales.getPrefixed(player, "stats.killed").replace("{killed}", String.valueOf(kills)));
    }

    private void showPvPKills(String uuid, Player player) {
        int pvp = db.getPvPKills(uuid);
        player.sendMessage(locales.getPrefixed(player, "stats.pvp").replace("{pvp}", String.valueOf(pvp)));
    }

    private void showDeaths(String uuid, Player player) {
        int deaths = db.getDeaths(uuid);
        player.sendMessage(locales.getPrefixed(player, "stats.deaths").replace("{deaths}", String.valueOf(deaths)));
    }

    private void showPlaytime(String uuid, Player player) {
        int seconds = db.getPlaytime(uuid);
        String formatted = formatPlaytime(seconds);
        player.sendMessage(locales.getPrefixed(player, "stats.playtime").replace("{playtime}", formatted));
    }

    private void showChatMessages(String uuid, Player player) {
        int messages = db.getChatMessages(uuid);
        player.sendMessage(locales.getPrefixed(player, "stats.chat").replace("{chat}", String.valueOf(messages)));
    }

    private void showAllStats(String uuid, Player player) {
        player.sendMessage(locales.getPrefixed(player, "stats.all.title"));

        showBlockStats(uuid, player);
        showMobKills(uuid, player);
        showPvPKills(uuid, player);
        showDeaths(uuid, player);
        showPlaytime(uuid, player);
        showChatMessages(uuid, player);
    }


    private void resetPlayerStats(Player sender, String targetName) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
        if (target == null || target.getUniqueId() == null) {
            sender.sendMessage(locales.getPrefixed(sender, "error.player_not_found"));
            return;
        }

        String uuid = target.getUniqueId().toString();
        if (db.resetStats(uuid)) {
            sender.sendMessage(locales.getPrefixed(sender, "stats.reset.success")
                    .replace("{name}", target.getName() != null ? target.getName() : uuid));
        } else {
            sender.sendMessage(locales.getPrefixed(sender, "error.reset_failed"));
        }
    }

    private String formatPlaytime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        return h + "h " + m + "m";
    }

    private void showGlobalStats(Player player) {
        int placed = db.getTotalBlocksPlaced();
        int broken = db.getTotalBlocksBroken();
        int killed = db.getTotalMobKills();
        int pvp = db.getTotalPvPKills();
        int deaths = db.getTotalDeaths();
        int chat = db.getTotalChatMessages();
        String playtime = formatPlaytime(db.getTotalPlaytime());

        player.sendMessage(locales.getPrefixed(player, "stats.global.title"));
        player.sendMessage(locales.getPrefixed(player, "stats.blocks").replace("{placed}", String.valueOf(placed)).replace("{broken}", String.valueOf(broken)));
        player.sendMessage(locales.getPrefixed(player, "stats.killed").replace("{killed}", String.valueOf(killed)));
        player.sendMessage(locales.getPrefixed(player, "stats.pvp").replace("{pvp}", String.valueOf(pvp)));
        player.sendMessage(locales.getPrefixed(player, "stats.deaths").replace("{deaths}", String.valueOf(deaths)));
        player.sendMessage(locales.getPrefixed(player, "stats.playtime").replace("{playtime}", playtime));
        player.sendMessage(locales.getPrefixed(player, "stats.chat").replace("{chat}", String.valueOf(chat)));
    }

    private void showTopPlaced(Player player) {
        Map<String, Integer> top = db.getTopPlacedBlocks(player.getUniqueId().toString());
        player.sendMessage("§6Top 10 gesetzte Blöcke:");
        int i = 1;
        for (var entry : top.entrySet()) {
            player.sendMessage("§e" + i++ + ". " + entry.getKey() + " – " + entry.getValue());
        }
    }

    private void showTopBroken(Player player) {
        Map<String, Integer> top = db.getTopBrokenBlocks(player.getUniqueId().toString());
        player.sendMessage("§6Top 10 abgebaut:");
        int i = 1;
        for (var entry : top.entrySet()) {
            player.sendMessage("§e" + i++ + ". " + entry.getKey() + " – " + entry.getValue());
        }
    }

    private void showTopMobs(Player player) {
        Map<String, Integer> top = db.getTopKilledMobs(player.getUniqueId().toString());
        if (top.isEmpty()) {
            player.sendMessage("§7Du hast bisher keine Mobs getötet.");
            return;
        }

        player.sendMessage("§6Top 10 getötete Mobs:");
        int i = 1;
        for (var entry : top.entrySet()) {
            player.sendMessage("§e" + i++ + ". §f" + entry.getKey() + " – " + entry.getValue());
        }
    }

    private void showPlayerStats(Player sender, String targetName) {
        String uuid = db.getUUIDFromName(targetName);
        if (uuid == null) {
            sender.sendMessage(locales.getPrefixed(sender, "error.player_not_found"));
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
        String name = target.getName() != null ? target.getName() : uuid;

        sender.sendMessage(locales.getPrefixed(sender, "stats.player.title").replace("{name}", name));
        showAllStats(uuid, sender);
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

                if (type.equals("playtime")) {
                    value = formatPlaytime(Integer.parseInt(value));
                }

                player.sendMessage("§e#" + (i + 1) + " §f" + name + " §7– §a" + value);
            }
        }
    }

}
