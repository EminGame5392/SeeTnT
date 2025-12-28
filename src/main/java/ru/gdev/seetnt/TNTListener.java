package ru.gdev.seetnt;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TNTListener implements Listener {

    private final SeeTnT plugin;
    private final Map<UUID, TNTData> activeTNT = new HashMap<>();
    private final Map<Location, String> placedTNTs = new HashMap<>();
    private final ProtocolManager protocolManager;

    public TNTListener(SeeTnT plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.SPAWN_ENTITY) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() != PacketType.Play.Server.SPAWN_ENTITY) return;
                if (event.getPacket().getEntityTypeModifier().read(0) != EntityType.PRIMED_TNT) return;
            }
        });
    }

    @EventHandler
    public void onPlace(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        ItemStack item = event.getItem();
        Block block = event.getClickedBlock();
        if (item == null || block == null) return;
        if (item.getType() != Material.TNT) return;

        String id = ItemMetaUtil.getCustomTNTId(item, plugin);
        if (id == null) return;
        CustomTNT customTNT = plugin.getTNTManager().getTNT(id);
        if (customTNT == null) return;

        event.setCancelled(true);

        Block placedBlock = block.getRelative(event.getBlockFace());
        placedBlock.setType(Material.TNT);
        placedTNTs.put(placedBlock.getLocation(), id);

        if (customTNT.isAutoIgnite()) {
            placedTNTs.remove(placedBlock.getLocation());
            placedBlock.setType(Material.AIR);
            TNTPrimed tnt = placedBlock.getWorld().spawn(placedBlock.getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
            tnt.setFuseTicks(customTNT.getFuse() * 20);
            activeTNT.put(tnt.getUniqueId(), new TNTData(tnt, customTNT, startBossBar(customTNT)));
            new TNTCountdown(tnt.getUniqueId()).runTaskTimer(plugin, 0L, 20L);
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed)) return;

        TNTPrimed tnt = (TNTPrimed) event.getEntity();
        Location loc = tnt.getLocation().getBlock().getLocation();

        String id = placedTNTs.remove(loc);
        if (id == null) return;

        CustomTNT customTNT = plugin.getTNTManager().getTNT(id);
        if (customTNT == null) return;

        tnt.setFuseTicks(customTNT.getFuse() * 20);
        activeTNT.put(tnt.getUniqueId(), new TNTData(tnt, customTNT, startBossBar(customTNT)));
        new TNTCountdown(tnt.getUniqueId()).runTaskTimer(plugin, 0L, 20L);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (!(event.getEntity() instanceof TNTPrimed)) return;
        UUID id = event.getEntity().getUniqueId();
        if (!activeTNT.containsKey(id)) return;

        TNTData data = activeTNT.remove(id);
        if (data.bossBar != null) data.bossBar.removeAll();
        event.blockList().clear();

        Material fluid = data.tnt.getLocation().getBlock().getType();
        boolean isInWater = fluid == Material.WATER || fluid == Material.KELP || fluid == Material.SEAGRASS || fluid == Material.BUBBLE_COLUMN;
        boolean isInLava = fluid == Material.LAVA;

        if ((isInWater && !data.customTNT.canExplodeInWater()) || (isInLava && !data.customTNT.canExplodeInLava())) {
            return;
        }

        if (data.customTNT.getType().equalsIgnoreCase("SPHERE")) {
            explodeSphere(data.tnt.getLocation().getBlock(), data.customTNT);
        } else {
            explodeCube(data.tnt.getLocation().getBlock(), data.customTNT);
        }
    }


    private BossBar startBossBar(CustomTNT tnt) {
        if (!tnt.isBossbarEnabled()) return null;
        BarColor color;
        try {
            color = BarColor.valueOf(tnt.getBossbarColor().toUpperCase());
        } catch (IllegalArgumentException e) {
            color = BarColor.RED;
        }
        BossBar bossBar = Bukkit.createBossBar(Utils.color(tnt.getBossbarText().replace("{time}", String.valueOf(tnt.getFuse()))), color, BarStyle.SEGMENTED_10);
        for (Player player : Bukkit.getOnlinePlayers()) {
            bossBar.addPlayer(player);
        }
        return bossBar;
    }

    private class TNTCountdown extends BukkitRunnable {

        private final UUID id;

        public TNTCountdown(UUID id) {
            this.id = id;
        }

        @Override
        public void run() {
            if (!activeTNT.containsKey(id)) {
                cancel();
                return;
            }
            TNTData data = activeTNT.get(id);
            if (data.tnt.isDead() || !data.tnt.isValid()) {
                if (data.bossBar != null) data.bossBar.removeAll();
                activeTNT.remove(id);
                cancel();
                return;
            }

            if (data.time <= 0) {
                if (data.bossBar != null) data.bossBar.removeAll();
                activeTNT.remove(id);
                if (data.tnt.isValid()) {
                    data.tnt.remove();
                }
                cancel();
                return;
            }

            String timeString = String.valueOf(data.time);

            if (data.customTNT.isHologramEnabled()) {
                data.tnt.setCustomName(Utils.color(data.customTNT.getHologramText().replace("{time}", timeString)));
                data.tnt.setCustomNameVisible(true);
            }
            if (data.bossBar != null) {
                data.bossBar.setTitle(Utils.color(data.customTNT.getBossbarText().replace("{time}", timeString)));
                if (data.customTNT.isBossbarProgress()) {
                    data.bossBar.setProgress(Math.max(0, (double) data.time / data.customTNT.getFuse()));
                }
            }
            if (data.customTNT.isActionbarEnabled()) {
                String text = Utils.color(data.customTNT.getActionbarText().replace("{time}", timeString));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText(text));
                }
            }
            if (data.customTNT.isTitleEnabled()) {
                String text = Utils.color(data.customTNT.getTitleText().replace("{time}", timeString));
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(text, "", 0, 20, 0);
                }
            }

            data.time--;
        }
    }

    private void explodeSphere(Block center, CustomTNT tnt) {
        int radius = tnt.getRadius();
        int r2 = radius * radius;
        float power = tnt.getPower();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x * x + y * y + z * z <= r2) {
                        Block block = center.getRelative(x, y, z);
                        if (canBreak(block, tnt) && Math.random() <= power) {
                            block.breakNaturally();
                        }
                    }
                }
            }
        }
    }


    private void explodeCube(Block center, CustomTNT tnt) {
        int radius = tnt.getRadius();
        float power = tnt.getPower();
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Block block = center.getRelative(x, y, z);
                    if (canBreak(block, tnt) && Math.random() <= power) {
                        block.breakNaturally();
                    }
                }
            }
        }
    }


    private boolean canBreak(Block block, CustomTNT tnt) {
        if (block.getType() == Material.AIR) return false;
        if (tnt.isWhitelistEnabled()) {
            return tnt.getWhitelistBlocks().contains(block.getType().name());
        }
        if (tnt.isBlacklistEnabled()) {
            return !tnt.getBlacklistBlocks().contains(block.getType().name());
        }
        return true;
    }

    private static class TNTData {
        public final TNTPrimed tnt;
        public final CustomTNT customTNT;
        public final BossBar bossBar;
        public int time;

        public TNTData(TNTPrimed tnt, CustomTNT customTNT, BossBar bossBar) {
            this.tnt = tnt;
            this.customTNT = customTNT;
            this.bossBar = bossBar;
            this.time = customTNT.getFuse();
        }
    }
}
