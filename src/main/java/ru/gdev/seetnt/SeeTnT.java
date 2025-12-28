package ru.gdev.seetnt;

import org.bukkit.plugin.java.JavaPlugin;

public final class SeeTnT extends JavaPlugin {

    private static SeeTnT instance;
    private TNTManager tntManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        tntManager = new TNTManager(this);
        tntManager.loadTNTs();
        getServer().getPluginManager().registerEvents(new TNTListener(this), this);
        RestTNTCommand command = new RestTNTCommand(this);
        getCommand("seetnt").setExecutor(command);
        getCommand("seetnt").setTabCompleter(command);
    }

    @Override
    public void onDisable() {
    }

    public static SeeTnT getInstance() {
        return instance;
    }

    public TNTManager getTNTManager() {
        return tntManager;
    }
}
