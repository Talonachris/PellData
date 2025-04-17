
package com.talona.pellData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PvPKillListener implements Listener {
    private final DatabaseManager db;

    public PvPKillListener(DatabaseManager db) {
        this.db = db;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        if (killer != null) {
            db.incrementPvPKills(killer.getUniqueId().toString());
        }
    }
}

// Registrierung in PellData.java innerhalb der onEnable()-Methode:
// getServer().getPluginManager().registerEvents(new PvPKillListener(db), this);
