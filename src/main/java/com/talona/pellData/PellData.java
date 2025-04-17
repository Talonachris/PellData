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

        // PlaceholderAPI registrieren
        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            getLogger().info(locales.get(null, "plugin.placeholderapi.found"));
            new PellPlaceholders(db).register();
        } else {
            getLogger().warning(locales.get(null, "plugin.placeholderapi.not_found"));
        }

        // Listener
        getServer().getPluginManager().registerEvents(new PvPKillListener(db), this);
        getServer().getPluginManager().registerEvents(new BlockListener(db), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(db), this);
        getServer().getPluginManager().registerEvents(new DeathListener(db, locales), this);
        getServer().getPluginManager().registerEvents(new PlaytimeListener(db, this), this);
        getServer().getPluginManager().registerEvents(new ChatListener(db), this);

        // Commands
        getCommand("pelldata").setExecutor(new CommandHandler(db, locales));
        getCommand("pelldata").setTabCompleter(new CommandCompleter(db, locales));

        getLogger().info("Pelle is counting your blocks! Sprache: " + getConfig().getString("language"));
    }

    @Override
    public void onDisable() {
        getLogger().info(locales.get(null, "plugin.shutdown"));
    }
}
