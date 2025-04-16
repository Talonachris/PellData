package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.sql.*;
import java.util.*;

public class DatabaseManager {

    private Connection connection;

    public DatabaseManager(String dbFilePath) {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFilePath);
            createTables();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createTables() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS player_stats (
                    uuid TEXT PRIMARY KEY,
                    blocks_placed INTEGER DEFAULT 0,
                    blocks_broken INTEGER DEFAULT 0,
                    killed_mobs INTEGER DEFAULT 0,
                    deaths INTEGER DEFAULT 0,
                    playtime_seconds INTEGER DEFAULT 0,
                    chat_messages INTEGER DEFAULT 0
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS block_place_stats (
                    uuid TEXT,
                    block_type TEXT,
                    count INTEGER DEFAULT 0,
                    PRIMARY KEY (uuid, block_type)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS block_break_stats (
                    uuid TEXT,
                    block_type TEXT,
                    count INTEGER DEFAULT 0,
                    PRIMARY KEY (uuid, block_type)
                );
            """);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void ensurePlayerRecord(String uuid) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT OR IGNORE INTO player_stats (uuid) VALUES (?)")) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementBlockPlaced(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE player_stats SET blocks_placed = blocks_placed + 1 WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementBlockBroken(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE player_stats SET blocks_broken = blocks_broken + 1 WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementMobKills(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE player_stats SET killed_mobs = killed_mobs + 1 WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementDeaths(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE player_stats SET deaths = deaths + 1 WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addPlaytime(String uuid, int seconds) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE player_stats SET playtime_seconds = playtime_seconds + ? WHERE uuid = ?")) {
            ps.setInt(1, seconds);
            ps.setString(2, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementChatMessages(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE player_stats SET chat_messages = chat_messages + 1 WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getChatMessages(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT chat_messages FROM player_stats WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("chat_messages");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int getBlocksPlaced(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT blocks_placed FROM player_stats WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("blocks_placed");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getBlocksBroken(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT blocks_broken FROM player_stats WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("blocks_broken");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getMobsKilled(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT killed_mobs FROM player_stats WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("killed_mobs");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getDeaths(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT deaths FROM player_stats WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("deaths");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getPlaytime(String uuid) {
        ensurePlayerRecord(uuid);
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT playtime_seconds FROM player_stats WHERE uuid = ?")) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("playtime_seconds");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void incrementBlockPlaced(String uuid, Material material) {
        try (PreparedStatement ps = connection.prepareStatement("""
            INSERT INTO block_place_stats (uuid, block_type, count)
            VALUES (?, ?, 1)
            ON CONFLICT(uuid, block_type) DO UPDATE SET count = count + 1;
        """)) {
            ps.setString(1, uuid);
            ps.setString(2, material.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementBlockBroken(String uuid, Material material) {
        try (PreparedStatement ps = connection.prepareStatement("""
            INSERT INTO block_break_stats (uuid, block_type, count)
            VALUES (?, ?, 1)
            ON CONFLICT(uuid, block_type) DO UPDATE SET count = count + 1;
        """)) {
            ps.setString(1, uuid);
            ps.setString(2, material.name());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getTopPlacedBlocks(String uuid) {
        Map<String, Integer> result = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
            SELECT block_type, count FROM block_place_stats
            WHERE uuid = ?
            ORDER BY count DESC
            LIMIT 10;
        """)) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("block_type"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Integer> getTopBrokenBlocks(String uuid) {
        Map<String, Integer> result = new LinkedHashMap<>();
        try (PreparedStatement ps = connection.prepareStatement("""
            SELECT block_type, count FROM block_break_stats
            WHERE uuid = ?
            ORDER BY count DESC
            LIMIT 10;
        """)) {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("block_type"), rs.getInt("count"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public String[] getTopStats(String type, int rank) {
        try {
            String query = switch (type.toLowerCase()) {
                case "placed" -> "SELECT uuid, blocks_placed AS value FROM player_stats ORDER BY blocks_placed DESC LIMIT ?, 1";
                case "broken" -> "SELECT uuid, blocks_broken AS value FROM player_stats ORDER BY blocks_broken DESC LIMIT ?, 1";
                case "killed" -> "SELECT uuid, killed_mobs AS value FROM player_stats ORDER BY killed_mobs DESC LIMIT ?, 1";
                case "deaths" -> "SELECT uuid, deaths AS value FROM player_stats ORDER BY deaths DESC LIMIT ?, 1";
                case "playtime" -> "SELECT uuid, playtime_seconds AS value FROM player_stats ORDER BY playtime_seconds DESC LIMIT ?, 1";
                case "chat" -> "SELECT uuid, chat_messages AS value FROM player_stats ORDER BY chat_messages DESC LIMIT ?, 1";
                default -> null;
            };

            if (query == null) return null;

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, rank);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new String[]{rs.getString("uuid"), rs.getString("value")};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean resetStats(String uuid) {
        try (PreparedStatement ps = connection.prepareStatement("""
            UPDATE player_stats
            SET blocks_placed = 0,
                blocks_broken = 0,
                killed_mobs = 0,
                deaths = 0,
                playtime_seconds = 0,
                chat_messages = 0
            WHERE uuid = ?
        """)) {
            ps.setString(1, uuid);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> getAllStoredPlayerNames() {
        List<String> names = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT uuid FROM player_stats")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(rs.getString("uuid")));
                if (player.getName() != null) {
                    names.add(player.getName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return names;
    }
}
