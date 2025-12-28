package ru.gdev.seetnt;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final SeeTnT plugin;
    private FileConfiguration config;

    public ConfigManager(SeeTnT plugin) {
        this.plugin = plugin;
        reloadMessages();
    }

    public void reloadMessages() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();
    }

    public String getMessage(String key) {
        if (config == null) return "";
        String message = config.getString("messages." + key);
        return message != null ? message : "";
    }
}