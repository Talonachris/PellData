package com.talona.pellData;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;
import java.util.UUID;

public class RankingMenu {

    private final DatabaseManager db;
    private final LocalesManager locales;

    public RankingMenu(DatabaseManager db, LocalesManager locales) {
        this.db = db;
        this.locales = locales;
    }

    public void openRanking(Player viewer, String type) {
        String title = getTitleFor(viewer, type);
        Inventory inv = Bukkit.createInventory(null, 54, title);

        // Top 10 Einträge
        for (int i = 0; i < 10; i++) {
            String[] entry = db.getTopStats(type, i);
            if (entry == null) continue;

            String uuid = entry[0];
            String value = entry[1];
            OfflinePlayer target = Bukkit.getOfflinePlayer(UUID.fromString(uuid));
            String name = (target != null && target.getName() != null) ? target.getName() : uuid;

            ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) skull.getItemMeta();
            if (meta != null) {
                meta.setOwningPlayer(target);
                meta.setDisplayName("§e#" + (i + 1) + " §f" + name);

                String line = locales.get(viewer, "ranking.value_only")
                        .replace("{value}", value)
                        .replace("{type}", type);
                meta.setLore(List.of("§7" + line));

                skull.setItemMeta(meta);
            }

            inv.setItem(i, skull);
        }

        // Zurück-Button (Slot 53)
        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta backMeta = back.getItemMeta();
        if (backMeta != null) {
            backMeta.setDisplayName("§c← " + locales.get(viewer, "menu.back"));
            back.setItemMeta(backMeta);
        }
        inv.setItem(53, back);

        viewer.openInventory(inv);
    }

    public String getTitleFor(Player viewer, String type) {
        return locales.get(viewer, "menu.ranking_title").replace("{type}", type);
    }
}
