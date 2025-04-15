package com.talona.pellData;

import org.bukkit.plugin.java.JavaPlugin;

public class PellData extends JavaPlugin {

    private DatabaseManager db;

    @Override
    public void onEnable() {
        getLogger().info("PellData has been enabled!");

        // Stelle sicher, dass das Plugin-Verzeichnis existiert
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Datenbank initialisieren (Datenbankdatei im Plugin-Ordner)
        db = new DatabaseManager(getDataFolder().getAbsolutePath() + "/data.db");

        // Listener registrieren
        getServer().getPluginManager().registerEvents(new BlockListener(db), this);
        getServer().getPluginManager().registerEvents(new MobKillListener(db), this);

        // CommandHandler registrieren
        getCommand("pelldata").setExecutor(new CommandHandler(db));
        // TabCompleter registrieren
        getCommand("pelldata").setTabCompleter(new CommandCompleter());
    }


    @Override
    public void onDisable() {
        getLogger().info("PellData has been disabled.");
    }
}
