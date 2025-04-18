package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StatsMenu {

    private final DatabaseManager db;
    private final LocalesManager locales;

    public StatsMenu(DatabaseManager db, LocalesManager locales) {
        this.db = db;
        this.locales = locales;
    }

    public void openMainMenu(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, locales.get(player, "menu.title"));

        UUID uuid = player.getUniqueId();
        String id = uuid.toString();

        inv.setItem(10, createStatItem(player, Material.STONE, "menu.stats.blocks.name", "menu.stats.blocks.lore",
                line -> line
                        .replace("{placed}", String.valueOf(db.getBlocksPlaced(id)))
                        .replace("{broken}", String.valueOf(db.getBlocksBroken(id)))
        ));

        inv.setItem(11, createStatItem(player, Material.ROTTEN_FLESH, "menu.stats.mobs.name", "menu.stats.mobs.lore",
                line -> line.replace("{killed}", String.valueOf(db.getMobsKilled(id)))
        ));

        inv.setItem(12, createStatItem(player, Material.IRON_SWORD, "menu.stats.pvp.name", "menu.stats.pvp.lore",
                line -> line.replace("{pvp}", String.valueOf(db.getPvPKills(id)))
        ));

        inv.setItem(13, createStatItem(player, Material.TOTEM_OF_UNDYING, "menu.stats.deaths.name", "menu.stats.deaths.lore",
                line -> line.replace("{deaths}", String.valueOf(db.getDeaths(id)))
        ));

        inv.setItem(14, createStatItem(player, Material.CLOCK, "menu.stats.playtime.name", "menu.stats.playtime.lore",
                line -> line.replace("{playtime}", formatPlaytime(db.getPlaytime(id)))
        ));

        inv.setItem(15, createStatItem(player, Material.PAPER, "menu.stats.chat.name", "menu.stats.chat.lore",
                line -> line.replace("{chat}", String.valueOf(db.getChatMessages(id)))
        ));

        inv.setItem(22, createStatItem(player, Material.BOOK, "menu.stats.global.name", "menu.stats.global.lore", line -> line));

        player.openInventory(inv);
    }

    private ItemStack createStatItem(Player player, Material mat, String nameKey, String loreKey, java.util.function.Function<String, String> transformer) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(locales.get(player, nameKey));
            List<String> rawLore = locales.getList(player, loreKey);
            if (rawLore != null) {
                meta.setLore(rawLore.stream().map(transformer).collect(Collectors.toList()));
            }
            item.setItemMeta(meta);
        }
        return item;
    }

    private String formatPlaytime(int seconds) {
        int h = seconds / 3600;
        int m = (seconds % 3600) / 60;
        return h + "h " + m + "m";
    }

    public String getTitleFor(Player player) {
        return locales.get(player, "menu.title");
    }
}
