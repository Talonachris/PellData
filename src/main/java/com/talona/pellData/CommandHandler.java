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
            case "menu" -> new StatsMenu(db, locales).openMainMenu(player);
            case "stats" -> {
                if (args.length < 2) {
                    player.sendMessage(locales.getPrefixed(player, "help.stats.usage"));
                    return true;
                }
                switch (args[1].toLowerCase()) {
                    case "blocks" -> showBlockStats(player, player);
                    case "killed" -> showMobKills(player, player);
                    case "pvp" -> showPvPKills(player, player);
                    case "deaths" -> showDeaths(player, player);
                    case "playtime" -> showPlaytime(player, player);
                    case "chat" -> showChatMessages(player, player);
                    case "all" -> showAllStats(player);
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
            case "globalstats" -> showGlobalStats(player);
            default -> player.sendMessage(locales.getPrefixed(player, "error.unknown_subcommand"));
        }

        return true;
    }

    private void showBlockStats(Player viewer, OfflinePlayer target) {
        String uuid = target.getUniqueId().toString();
        int placed = db.getBlocksPlaced(uuid);
        int broken = db.getBlocksBroken(uuid);
        String msg = locales.getPrefixed(viewer, "stats.blocks")
                .replace("{placed}", String.valueOf(placed))
                .replace("{broken}", String.valueOf(broken));
        viewer.sendMessage(msg);
    }

    private void showMobKills(Player viewer, OfflinePlayer target) {
        int kills = db.getMobsKilled(target.getUniqueId().toString());
        viewer.sendMessage(locales.getPrefixed(viewer, "stats.killed").replace("{killed}", String.valueOf(kills)));
    }

    private void showPvPKills(Player viewer, OfflinePlayer target) {
        int pvp = db.getPvPKills(target.getUniqueId().toString());
        viewer.sendMessage(locales.getPrefixed(viewer, "stats.pvp").replace("{pvp}", String.valueOf(pvp)));
    }

    private void showDeaths(Player viewer, OfflinePlayer target) {
        int deaths = db.getDeaths(target.getUniqueId().toString());
        viewer.sendMessage(locales.getPrefixed(viewer, "stats.deaths").replace("{deaths}", String.valueOf(deaths)));
    }

    private void showPlaytime(Player viewer, OfflinePlayer target) {
        int seconds = db.getPlaytime(target.getUniqueId().toString());
        String formatted = formatPlaytime(seconds);
        viewer.sendMessage(locales.getPrefixed(viewer, "stats.playtime").replace("{playtime}", formatted));
    }

    private void showChatMessages(Player viewer, OfflinePlayer target) {
        int messages = db.getChatMessages(target.getUniqueId().toString());
        viewer.sendMessage(locales.getPrefixed(viewer, "stats.chat").replace("{chat}", String.valueOf(messages)));
    }

    private void showAllStats(Player player) {
        showBlockStats(player, player);
        showMobKills(player, player);
        showPvPKills(player, player);
        showDeaths(player, player);
        showPlaytime(player, player);
        showChatMessages(player, player);
    }

    private void showTopPlaced(Player player) {
        Map<String, Integer> top = db.getTopPlacedBlocks(player.getUniqueId().toString());
        player.sendMessage("§6Top 10 placed blocks:");
        int i = 1;
        for (var entry : top.entrySet()) {
            player.sendMessage("§e" + i++ + ". " + entry.getKey() + " – " + entry.getValue());
        }
    }

    private void showTopBroken(Player player) {
        Map<String, Integer> top = db.getTopBrokenBlocks(player.getUniqueId().toString());
        player.sendMessage("§6Top 10 broken blocks:");
        int i = 1;
        for (var entry : top.entrySet()) {
            player.sendMessage("§e" + i++ + ". " + entry.getKey() + " – " + entry.getValue());
        }
    }

    private void showTopMobs(Player player) {
        Map<String, Integer> top = db.getTopKilledMobs(player.getUniqueId().toString());
        if (top.isEmpty()) {
            player.sendMessage("§7You haven't killed any mobs yet.");
            return;
        }
        player.sendMessage("§6Top 10 killed mobs:");
        int i = 1;
        for (var entry : top.entrySet()) {
            player.sendMessage("§e" + i++ + ". §f" + entry.getKey() + " – " + entry.getValue());
        }
    }

    private void showRanking(Player player, String type) {
        player.sendMessage(locales.get(player, "ranking.title").replace("{type}", type));
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

                String line = locales.get(player, "ranking.entry")
                        .replace("{rank}", String.valueOf(i + 1))
                        .replace("{name}", name)
                        .replace("{value}", value);
                player.sendMessage(line);
            }
        }
    }

    private void showPlayerStats(Player sender, String targetName) {
        OfflinePlayer target = db.getAllStoredPlayerNames().stream()
                .filter(n -> n.equalsIgnoreCase(targetName))
                .findFirst()
                .map(Bukkit::getOfflinePlayer)
                .orElse(null);
        if (target == null || target.getUniqueId() == null) {
            sender.sendMessage(locales.getPrefixed(sender, "error.player_not_found"));
            return;
        }

        String name = target.getName() != null ? target.getName() : target.getUniqueId().toString();
        sender.sendMessage(locales.getPrefixed(sender, "stats.player.title").replace("{name}", name));

        showBlockStats((Player) sender, target);
        showMobKills((Player) sender, target);
        showPvPKills((Player) sender, target);
        showDeaths((Player) sender, target);
        showPlaytime((Player) sender, target);
        showChatMessages((Player) sender, target);
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
}
