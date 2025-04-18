package com.talona.pellData;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class StatsMenuListener implements Listener {

    private final StatsMenu statsMenu;
    private final RankingMenu rankingMenu;

    public StatsMenuListener(StatsMenu statsMenu, RankingMenu rankingMenu) {
        this.statsMenu = statsMenu;
        this.rankingMenu = rankingMenu;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = event.getView().getTitle();
        boolean inMainMenu = title.equals(statsMenu.getTitleFor(player));

        event.setCancelled(true);

        if (inMainMenu) {
            switch (event.getSlot()) {
                case 10 -> rankingMenu.openRanking(player, "placed");
                case 11 -> rankingMenu.openRanking(player, "killed");
                case 12 -> rankingMenu.openRanking(player, "pvp");
                case 13 -> rankingMenu.openRanking(player, "deaths");
                case 14 -> rankingMenu.openRanking(player, "playtime");
                case 15 -> rankingMenu.openRanking(player, "chat");
                case 22 -> rankingMenu.openRanking(player, "pvp");
            }
        } else {
            // Back-Button (Slot 53) prüfen auf alle Ranking-Titel
            for (String type : new String[]{"placed", "killed", "pvp", "deaths", "playtime", "chat"}) {
                if (title.equals(rankingMenu.getTitleFor(player, type)) && event.getSlot() == 53) {
                    statsMenu.openMainMenu(player);
                    break;
                }
            }
        }
    }
}
