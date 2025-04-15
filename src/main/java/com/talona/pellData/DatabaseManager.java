package com.talona.pellData;

import java.sql.*;

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
                    "killed_mobs INTEGER DEFAULT 0" +
                    ");";

            Statement stmt = connection.createStatement();
            stmt.execute(createPlayerStatsTable);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hilfsmethode: Erstelle einen Datensatz für den Spieler, falls nicht vorhanden
    private void ensurePlayerRecord(String uuid) {
        try {
            // Mit INSERT OR IGNORE wird nichts gemacht, wenn der Datensatz schon existiert.
            String query = "INSERT OR IGNORE INTO player_stats (uuid) VALUES (?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Block stats: Abgebaute Blöcke erhöhen
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

    // Block stats: Gesetzte Blöcke erhöhen
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

    // Mob-Kills erhöhen
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

    // Block-Statistiken für einen Spieler abrufen
    public int getBlocksPlaced(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "SELECT blocks_placed FROM player_stats WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("blocks_placed");
                rs.close();
                ps.close();
                return count;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Falls keine Daten vorhanden sind
    }

    // Abgebaute Blöcke für einen Spieler abrufen
    public int getBlocksBroken(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "SELECT blocks_broken FROM player_stats WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("blocks_broken");
                rs.close();
                ps.close();
                return count;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Mob-Kills für einen Spieler abrufen
    public int getMobsKilled(String uuid) {
        ensurePlayerRecord(uuid);
        try {
            String query = "SELECT killed_mobs FROM player_stats WHERE uuid = ?";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int count = rs.getInt("killed_mobs");
                rs.close();
                ps.close();
                return count;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Die Top 10 Spieler basierend auf einer Statistik (placed, broken, killed) abrufen
    public String[] getTopStats(String type, int rank) {
        try {
            String query = "";

            if (type.equalsIgnoreCase("placed")) {
                query = "SELECT uuid, blocks_placed FROM player_stats ORDER BY blocks_placed DESC LIMIT ?, 1";
            } else if (type.equalsIgnoreCase("broken")) {
                query = "SELECT uuid, blocks_broken FROM player_stats ORDER BY blocks_broken DESC LIMIT ?, 1";
            } else if (type.equalsIgnoreCase("killed")) {
                query = "SELECT uuid, killed_mobs FROM player_stats ORDER BY killed_mobs DESC LIMIT ?, 1";
            }

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, rank);  // Setze das Ranking (Offset)
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String uuid = rs.getString("uuid");
                String count = rs.getString(type.equalsIgnoreCase("killed") ? "killed_mobs" : (type.equalsIgnoreCase("placed") ? "blocks_placed" : "blocks_broken"));
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
}
