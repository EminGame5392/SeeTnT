package ru.gdev.seetnt;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMetaUtil {

    private static final String TNT_ID_KEY = "resttnt_id";

    public static ItemStack createTNTItem(CustomTNT tnt, int amount, JavaPlugin plugin) {
        if (tnt == null || amount <= 0 || plugin == null) return null;

        ItemStack item = new ItemStack(Material.TNT, Math.min(amount, 64));
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return item;

        NamespacedKey key = new NamespacedKey(plugin, TNT_ID_KEY);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, tnt.getId());

        meta.setDisplayName(Utils.color(tnt.getDisplayName()));
        List<String> lore = tnt.getLore();
        if (lore != null && !lore.isEmpty()) {
            meta.setLore(lore.stream().map(Utils::color).collect(Collectors.toList()));
        }

        if (tnt.isGlow()) {
            meta.addEnchant(org.bukkit.enchantments.Enchantment.LUCK, 1, true);
            meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
        }

        item.setItemMeta(meta);
        return item;
    }

    public static String getCustomTNTId(ItemStack item, JavaPlugin plugin) {
        if (item == null || item.getType() != Material.TNT || !item.hasItemMeta()) return null;

        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, TNT_ID_KEY);
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.has(key, PersistentDataType.STRING) ? container.get(key, PersistentDataType.STRING) : null;
    }
}
