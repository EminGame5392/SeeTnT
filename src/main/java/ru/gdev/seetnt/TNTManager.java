package ru.gdev.seetnt;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class TNTManager {

    private final SeeTnT plugin;
    private final Map<String, CustomTNT> tnts = new HashMap<>();

    public TNTManager(SeeTnT plugin) {
        this.plugin = plugin;
    }

    public void loadTNTs() {
        tnts.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("tnts");
        if (section == null) return;

        for (String id : section.getKeys(false)) {
            ConfigurationSection tntSection = section.getConfigurationSection(id);
            if (tntSection == null) continue;

            CustomTNT tnt = new CustomTNT(
                    id,
                    tntSection.getString("display_name", "&cCustom TNT"),
                    tntSection.getStringList("lore"),
                    tntSection.getInt("fuse", 5),
                    tntSection.getInt("radius", 3),
                    tntSection.getString("type", "SPHERE"),
                    tntSection.getBoolean("hologram.enabled", false),
                    tntSection.getString("hologram.text", "&e{time} секунд"),
                    tntSection.getBoolean("bossbar.enabled", false),
                    tntSection.getString("bossbar.text", "&e{time} секунд"),
                    tntSection.getBoolean("bossbar.progress", false),
                    tntSection.getString("bossbar.color", "RED"),
                    tntSection.getBoolean("actionbar.enabled", false),
                    tntSection.getString("actionbar.text", "&e{time} секунд"),
                    tntSection.getBoolean("title.enabled", false),
                    tntSection.getString("title.text", "&e{time} секунд"),
                    tntSection.getBoolean("glow", false),
                    tntSection.getBoolean("whitelist_blocks.enabled", false),
                    tntSection.getStringList("whitelist_blocks.blocks"),
                    tntSection.getBoolean("blacklist_blocks.enabled", false),
                    tntSection.getStringList("blacklist_blocks.blocks"),
                    tntSection.getBoolean("auto_ignite", true),
                    tntSection.getBoolean("explode_in_water", true),
                    tntSection.getBoolean("explode_in_lava", true),
                    (float) tntSection.getDouble("power", 1.0)
            );
            tnts.put(id.toLowerCase(), tnt);
        }
    }

    public CustomTNT getTNT(String id) {
        return tnts.get(id.toLowerCase());
    }

    public Map<String, CustomTNT> getTNTs() {
        return tnts;
    }
}
