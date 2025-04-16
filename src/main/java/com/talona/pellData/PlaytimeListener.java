package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaytimeListener implements Listener {

    private final DatabaseManager db;
    private final Map<UUID, Long> lastActivity = new HashMap<>();
    private final long afkTimeoutMillis = 10 * 60 * 1000; // 10 Minuten

    public PlaytimeListener(DatabaseManager db, Plugin plugin) {
        this.db = db;

        // 1-Minuten-Timer zum Speichern der aktiven Spielzeit
        new BukkitRunnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();

                for (Player player : Bukkit.getOnlinePlayers()) {
                    UUID uuid = player.getUniqueId();
                    Long last = lastActivity.get(uuid);

                    if (last == null || now - last <= afkTimeoutMillis) {
                        db.addPlaytime(uuid.toString(), 60); // Aktiv → +60 Sekunden
                    }
                }
            }
        }.runTaskTimer(plugin, 20 * 60, 20 * 60); // alle 60 Sekunden
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        lastActivity.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastActivity.remove(event.getPlayer().getUniqueId());
    }

    // Alle Events, die als Aktivität zählen:
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getFrom().toVector().equals(event.getTo().toVector())) {
            updateActivity(event.getPlayer());
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        updateActivity(event.getPlayer());
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        updateActivity(event.getPlayer());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        updateActivity(event.getPlayer());
    }

    private void updateActivity(Player player) {
        lastActivity.put(player.getUniqueId(), System.currentTimeMillis());
    }
}
