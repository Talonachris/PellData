package com.talona.pellData;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {

    private final DatabaseManager db;

    public BlockListener(DatabaseManager db) {
        this.db = db;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        String uuid = player.getUniqueId().toString();
        Material material = block.getType();

        db.incrementBlockPlaced(uuid);              // zählt gesamt
        db.incrementBlockPlaced(uuid, material);    // zählt Typ
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();

        String uuid = player.getUniqueId().toString();
        Material material = block.getType();

        db.incrementBlockBroken(uuid);              // zählt gesamt
        db.incrementBlockBroken(uuid, material);    // zählt Typ
    }
}
