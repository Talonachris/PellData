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
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlockPlaced();
        db.incrementBlockPlaced(player.getUniqueId().toString());
        db.incrementBlockPlaced(player.getUniqueId().toString(), block.getType());
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        if (block.getType() != Material.AIR) {
            db.incrementBlockBroken(player.getUniqueId().toString());
            db.incrementBlockBroken(player.getUniqueId().toString(), block.getType());
        }
    }
}
