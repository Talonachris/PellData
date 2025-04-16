package com.talona.pellData;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LocalesManager {

    private final Plugin plugin;
    private final String language;
    private final Map<String, YamlConfiguration> loadedLocales = new HashMap<>();

    public LocalesManager(Plugin plugin) {
        this.plugin = plugin;

        plugin.saveDefaultConfig();
        this.language = plugin.getConfig().getString("language", "en_us");

        // Beide Standard-Dateien kopieren, falls sie fehlen
        copyDefaultLocale("en_us");
        copyDefaultLocale("de_de");

        loadLocale("en_us");
        loadLocale(language);
    }

    private void loadLocale(String locale) {
        File localeFile = new File(plugin.getDataFolder(), "locales/" + locale + ".yml");

        if (!localeFile.exists()) {
            plugin.getLogger().info("LocalesManager: Erstelle Standarddatei für " + locale);
            localeFile.getParentFile().mkdirs();

            try (InputStream in = plugin.getResource("locales/" + locale + ".yml");
                 FileOutputStream out = new FileOutputStream(localeFile)) {
                if (in != null) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        loadedLocales.put(locale.toLowerCase(), YamlConfiguration.loadConfiguration(localeFile));
    }

    public String get(Player player, String key) {
        String locale = this.language.toLowerCase();
        String value = get(locale, key);
        if (value == null) value = get("en_us", key);
        if (value == null) value = "[Missing: " + key + "]";
        return value;
    }

    public String getPrefixed(Player player, String key) {
        String message = get(player, key);
        String prefix = get(player, "prefix");
        return prefix + " " + message;
    }

    private String get(String locale, String key) {
        YamlConfiguration config = loadedLocales.get(locale);
        return (config != null) ? config.getString(key) : null;
    }

    private void copyDefaultLocale(String locale) {
        File file = new File(plugin.getDataFolder(), "locales/" + locale + ".yml");

        if (!file.exists()) {
            plugin.getLogger().info("LocalesManager: Kopiere " + locale + ".yml...");
            file.getParentFile().mkdirs();

            try (InputStream in = plugin.getResource("locales/" + locale + ".yml");
                 FileOutputStream out = new FileOutputStream(file)) {

                if (in != null) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
            } catch (Exception e) {
                plugin.getLogger().warning("Fehler beim Kopieren von " + locale + ".yml");
                e.printStackTrace();
            }
        }
    }
}
