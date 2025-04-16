package com.talona.pellData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final DatabaseManager db;

    public DeathListener(DatabaseManager db) {
        this.db = db;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        db.incrementDeaths(player.getUniqueId().toString());
    }
}
