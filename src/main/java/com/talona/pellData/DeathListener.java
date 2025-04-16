package com.talona.pellData;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final DatabaseManager db;
    private final LocalesManager locales;

    public DeathListener(DatabaseManager db, LocalesManager locales) {
        this.db = db;
        this.locales = locales;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        db.incrementDeaths(event.getEntity().getUniqueId().toString());
    }
}
