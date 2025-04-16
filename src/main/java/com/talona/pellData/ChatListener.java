package com.talona.pellData;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final DatabaseManager db;

    public ChatListener(DatabaseManager db) {
        this.db = db;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        db.incrementChatMessages(event.getPlayer().getUniqueId().toString());
    }
}
