package com.talona.pellData;

import org.bukkit.plugin.java.JavaPlugin;

public class PellData extends JavaPlugin {

    private DatabaseManager db;

    @Override
    public void onEnable() {
        getLogger().info("PellData aktiviert ✅");

        // Plugin-Ordner anlegen, falls nicht vorhanden
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Datenbank initialisieren
        db = new DatabaseManager(getDataFolder().getAbsolutePath() + "/data.db");

        // Listener registrieren
        getServer().getPluginManager().registerEvents(new BlockListener(db), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(db), this);

        // Commands registrieren
        getCommand("pelldata").setExecutor(new CommandHandler(db));
        getCommand("pelldata").setTabCompleter(new CommandCompleter(db));
    }

    @Override
    public void onDisable() {
        getLogger().info("PellData deaktiviert ❌");
    }
}
