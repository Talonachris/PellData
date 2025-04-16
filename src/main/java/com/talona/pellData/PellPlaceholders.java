package com.talona.pellData;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class PellPlaceholders extends PlaceholderExpansion {

    private final DatabaseManager db;

    public PellPlaceholders(DatabaseManager db) {
        this.db = db;
    }

    @Override
    public String getIdentifier() {
        return "pelldata";
    }

    @Override
    public String getAuthor() {
        return "Talonachris";
    }

    @Override
    public String getVersion() {
        return "1.1";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (db == null) return "";

        // Unterstützt %pelldata_blocks_broken_Talonachris%
        for (String stat : new String[]{"blocks_placed", "blocks_broken", "killed_mobs", "deaths", "playtime", "chat", "pvp"}) {
            if (params.toLowerCase().startsWith(stat + "_")) {
                String name = params.substring(stat.length() + 1);
                OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                if (target == null || target.getUniqueId() == null) return "N/A";
                String uuid = target.getUniqueId().toString();

                return switch (stat) {
                    case "blocks_placed" -> String.valueOf(db.getBlocksPlaced(uuid));
                    case "blocks_broken" -> String.valueOf(db.getBlocksBroken(uuid));
                    case "killed_mobs" -> String.valueOf(db.getMobsKilled(uuid));
                    case "deaths" -> String.valueOf(db.getDeaths(uuid));
                    case "playtime" -> formatTime(db.getPlaytime(uuid));
                    case "chat" -> String.valueOf(db.getChatMessages(uuid));
                    case "pvp" -> String.valueOf(db.getPvPKills(uuid));
                    default -> "N/A";
                };
            }
        }

        // Standard: aktueller Spieler
        String uuid = player != null ? player.getUniqueId().toString() : null;
        if (uuid == null) return "";

        return switch (params.toLowerCase()) {
            // Eigene Stats
            case "blocks_placed" -> String.valueOf(db.getBlocksPlaced(uuid));
            case "blocks_broken" -> String.valueOf(db.getBlocksBroken(uuid));
            case "killed_mobs" -> String.valueOf(db.getMobsKilled(uuid));
            case "deaths" -> String.valueOf(db.getDeaths(uuid));
            case "playtime" -> formatTime(db.getPlaytime(uuid));
            case "chat" -> String.valueOf(db.getChatMessages(uuid));
            case "pvp" -> String.valueOf(db.getPvPKills(uuid));

            // Global
            case "global_blocks_placed" -> String.valueOf(db.getTotalBlocksPlaced());
            case "global_blocks_broken" -> String.valueOf(db.getTotalBlocksBroken());
            case "global_killed_mobs" -> String.valueOf(db.getTotalMobKills());
            case "global_deaths" -> String.valueOf(db.getTotalDeaths());
            case "global_playtime" -> formatTime(db.getTotalPlaytime());
            case "global_chat" -> String.valueOf(db.getTotalChatMessages());
            case "global_pvp" -> String.valueOf(db.getTotalPvPKills());

            default -> null;
        };
    }

    private String formatTime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        return h + "h " + m + "m";
    }
}
