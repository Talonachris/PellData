package com.talona.pellData;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class MobKillListener implements Listener {

    private final DatabaseManager db;

    public MobKillListener(DatabaseManager db) {
        this.db = db;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        // Nur lebende Nicht-Spieler-Mobs zählen
        if (!(entity instanceof LivingEntity livingEntity)) return;

        Player killer = livingEntity.getKiller();
        if (killer != null) {
            UUID uuid = killer.getUniqueId();
            String mobType = entity.getType().name(); // z. B. ZOMBIE, CREEPER

            db.incrementMobKills(uuid.toString());                // Gesamtzahl Mobs
            db.incrementMobKillType(uuid.toString(), mobType);    // Typ-spezifisch
        }
    }
}
