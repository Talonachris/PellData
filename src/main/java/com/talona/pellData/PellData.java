package com.talona.pellData;

import org.bukkit.plugin.java.JavaPlugin;

public class PellData extends JavaPlugin {

    private DatabaseManager db;
    private LocalesManager locales;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.db = new DatabaseManager(getDataFolder() + "/data.db");
        this.locales = new LocalesManager(this);

        // PlaceholderAPI
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info("§a✔ PlaceholderAPI detected §7– registering placeholders...");
            new PellPlaceholders(db).register();
        } else {
            getLogger().warning("PlaceholderAPI not found – placeholders will not work.");
        }

        // GUI-Setup
        StatsMenu statsMenu = new StatsMenu(db, locales);
        RankingMenu rankingMenu = new RankingMenu(db, locales);

        // Listener
        getServer().getPluginManager().registerEvents(new BlockListener(db), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(db), this);
        getServer().getPluginManager().registerEvents(new DeathListener(db, locales), this);
        getServer().getPluginManager().registerEvents(new PlaytimeListener(db, this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(db), this);
        getServer().getPluginManager().registerEvents(new StatsMenuListener(statsMenu, rankingMenu), this);

        // Commands
        getCommand("pelldata").setExecutor(new CommandHandler(db, locales));
        getCommand("pelldata").setTabCompleter(new CommandCompleter(db, locales));

        getLogger().info("Pelle is counting your blocks! Language: " + getConfig().getString("language"));
    }

    @Override
    public void onDisable() {
        getLogger().info("PellData has been disabled.");
    }
}
