package com.talona.pellData;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.UUID;

public class MobKillListener implements Listener {

    private DatabaseManager db;

    public MobKillListener(DatabaseManager db) {
        this.db = db;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();

        // Nur auf Mobs achten (keine Spieler zählen)
        if (!(entity instanceof LivingEntity)) return;  // Sicherstellen, dass es ein lebendes Wesen ist

        // Wenn der Mob von einem Spieler getötet wurde, den Spieler holen
        Player player = ((LivingEntity) entity).getKiller();  // getKiller() gibt den Spieler zurück, der den Mob getötet hat

        if (player != null) {
            UUID playerUUID = player.getUniqueId();

            // Mob-Kills in der Datenbank aktualisieren
            db.incrementMobKills(playerUUID.toString());  // Übergeben der UUID als String
        }
    }
}

