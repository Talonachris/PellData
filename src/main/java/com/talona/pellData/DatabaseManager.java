package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager(String dbFilePath) {
        try {
            // SQLite-Datenbankverbindung herstellen
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tabelle erstellen, falls sie noch nicht existiert
    private void createTables() {
        try {
            String createPlayerStatsTable = "CREATE TABLE IF NOT EXISTS player_stats (" +
                    "uuid TEXT PRIMARY KEY," +
                    "blocks_placed INTEGER DEFAULT 0," +
                    "blocks_broken INTEGER DEFAULT 0," +
                    "killed_mobs INTEGER DEFAULT 0," +
                    "deaths INTEGER DEFAULT 0" +
                    ");";

            Statement stmt = connection.createStatement();
            stmt.execute(createPlayerStatsTable);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensurePlayerRecord(String uuid) {
        try {
            String query = "INSERT OR IGNORE INTO player_stats (uuid) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementBlockBroken(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "UPDATE player_stats SET blocks_broken = blocks_broken + 1 WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementBlockPlaced(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "UPDATE player_stats SET blocks_placed = blocks_placed + 1 WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementMobKills(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "UPDATE player_stats SET killed_mobs = killed_mobs + 1 WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementDeaths(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "UPDATE player_stats SET deaths = deaths + 1 WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getBlocksPlaced(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "SELECT blocks_placed FROM player_stats WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("blocks_placed");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getBlocksBroken(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "SELECT blocks_broken FROM player_stats WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("blocks_broken");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getMobsKilled(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "SELECT killed_mobs FROM player_stats WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("killed_mobs");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDeaths(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "SELECT deaths FROM player_stats WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("deaths");
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String[] getTopStats(String type, int rank) {
        try {
            String query = switch (type.toLowerCase()) {
                case "placed" -> "SELECT uuid, blocks_placed AS value FROM player_stats ORDER BY blocks_placed DESC LIMIT ?, 1";
                case "broken" -> "SELECT uuid, blocks_broken AS value FROM player_stats ORDER BY blocks_broken DESC LIMIT ?, 1";
                case "killed" -> "SELECT uuid, killed_mobs AS value FROM player_stats ORDER BY killed_mobs DESC LIMIT ?, 1";
                case "deaths" -> "SELECT uuid, deaths AS value FROM player_stats ORDER BY deaths DESC LIMIT ?, 1";
                default -> null;
            };

            if (query == null) return null;

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, rank);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String uuid = rs.getString("uuid");
                String count = rs.getString("value");
                rs.close();
                ps.close();
                return new String[] {uuid, count};
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<String> getAllStoredPlayerNames() {
        List<String> names = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT uuid FROM player_stats");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
                if (player.getName() != null) {
                    names.add(player.getName());
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }

    public boolean resetStats(String uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("""
            UPDATE player_stats
            SET blocks_placed = 0, blocks_broken = 0, killed_mobs = 0, deaths = 0
            WHERE uuid = ?
        """);
            ps.setString(1, uuid);
            int rows = ps.executeUpdate();
            ps.close();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
