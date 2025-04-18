package com.talona.pellData;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LocalesManager {

    private final Plugin plugin;
    private final String language;
    private final Map<String, YamlConfiguration> loadedLocales = new HashMap<>();

    public LocalesManager(Plugin plugin) {
        this.plugin = plugin;

        plugin.saveDefaultConfig();
        this.language = plugin.getConfig().getString("language", "en_us").toLowerCase();

        File customFile = new File(plugin.getDataFolder(), "locales/custom.yml");
        if (!customFile.exists()) {
            plugin.saveResource("locales/custom.yml", false);
        }

        if (language.equals("custom")) {
            loadLocale("custom");
        } else {
            copyDefaultLocale("en_us");
            copyDefaultLocale("de_de");
            loadLocale("en_us");
            loadLocale(language);
        }
    }

    private void loadLocale(String locale) {
        File localeFile = new File(plugin.getDataFolder(), "locales/" + locale + ".yml");
        if (!localeFile.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(localeFile);
        loadedLocales.put(locale, config);
    }

    private void copyDefaultLocale(String locale) {
        File localeFile = new File(plugin.getDataFolder(), "locales/" + locale + ".yml");
        if (!localeFile.exists()) {
            plugin.getLogger().info("[PellData] LocalesManager: Copying " + locale + ".yml...");
            plugin.saveResource("locales/" + locale + ".yml", false);
        }
    }

    public String get(Player player, String key) {
        YamlConfiguration langFile = loadedLocales.get(language);
        if (langFile == null) return key;

        String msg = langFile.getString(key);
        if (msg == null) {
            YamlConfiguration fallback = loadedLocales.get("en_us");
            msg = fallback != null ? fallback.getString(key, key) : key;
        }

        String prefix = langFile.getString("prefix", "&e[Pelldata] &r");
        msg = msg.replace("{prefix}", prefix);

        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public List<String> getList(Player player, String key) {
        YamlConfiguration langFile = loadedLocales.get(language);
        if (langFile == null) return List.of();

        List<String> list = langFile.getStringList(key);
        if (list == null || list.isEmpty()) {
            YamlConfiguration fallback = loadedLocales.get("en_us");
            list = fallback != null ? fallback.getStringList(key) : List.of();
        }

        return list.stream()
                .map(line -> line.replace("{prefix}", getPrefix()))
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
    }

    public String getPrefixed(Player player, String key) {
        return get(player, key);
    }

    private String getPrefix() {
        YamlConfiguration langFile = loadedLocales.get(language);
        return ChatColor.translateAlternateColorCodes('&',
                langFile != null ? langFile.getString("prefix", "&e[Pelldata] &r") : "&e[Pelldata] &r");
    }
}
