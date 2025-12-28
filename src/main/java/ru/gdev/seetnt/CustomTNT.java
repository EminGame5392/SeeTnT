package ru.gdev.seetnt;

import java.util.List;

public class CustomTNT {

    private final String id;
    private final String displayName;
    private final List<String> lore;
    private final int fuse;
    private final int radius;
    private final String type;
    private final boolean hologramEnabled;
    private final String hologramText;
    private final boolean bossbarEnabled;
    private final String bossbarText;
    private final boolean bossbarProgress;
    private final String bossbarColor;
    private final boolean actionbarEnabled;
    private final String actionbarText;
    private final boolean titleEnabled;
    private final String titleText;
    private final boolean glow;
    private final boolean whitelistEnabled;
    private final List<String> whitelistBlocks;
    private final boolean blacklistEnabled;
    private final List<String> blacklistBlocks;
    private final boolean autoIgnite;
    private final boolean explodeInWater;
    private final boolean explodeInLava;
    private final float power;

    public CustomTNT(String id, String displayName, List<String> lore, int fuse, int radius, String type,
                     boolean hologramEnabled, String hologramText,
                     boolean bossbarEnabled, String bossbarText, boolean bossbarProgress, String bossbarColor,
                     boolean actionbarEnabled, String actionbarText,
                     boolean titleEnabled, String titleText,
                     boolean glow,
                     boolean whitelistEnabled, List<String> whitelistBlocks,
                     boolean blacklistEnabled, List<String> blacklistBlocks,
                     boolean autoIgnite,
                     boolean explodeInWater, boolean explodeInLava,
                     float power) {
        this.id = id;
        this.displayName = displayName;
        this.lore = lore;
        this.fuse = fuse;
        this.radius = radius;
        this.type = type;
        this.hologramEnabled = hologramEnabled;
        this.hologramText = hologramText;
        this.bossbarEnabled = bossbarEnabled;
        this.bossbarText = bossbarText;
        this.bossbarProgress = bossbarProgress;
        this.bossbarColor = bossbarColor;
        this.actionbarEnabled = actionbarEnabled;
        this.actionbarText = actionbarText;
        this.titleEnabled = titleEnabled;
        this.titleText = titleText;
        this.glow = glow;
        this.whitelistEnabled = whitelistEnabled;
        this.whitelistBlocks = whitelistBlocks;
        this.blacklistEnabled = blacklistEnabled;
        this.blacklistBlocks = blacklistBlocks;
        this.autoIgnite = autoIgnite;
        this.explodeInWater = explodeInWater;
        this.explodeInLava = explodeInLava;
        this.power = power;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public List<String> getLore() { return lore; }
    public int getFuse() { return fuse; }
    public int getRadius() { return radius; }
    public String getType() { return type; }
    public boolean isHologramEnabled() { return hologramEnabled; }
    public String getHologramText() { return hologramText; }
    public boolean isBossbarEnabled() { return bossbarEnabled; }
    public String getBossbarText() { return bossbarText; }
    public boolean isBossbarProgress() { return bossbarProgress; }
    public String getBossbarColor() { return bossbarColor; }
    public boolean isActionbarEnabled() { return actionbarEnabled; }
    public String getActionbarText() { return actionbarText; }
    public boolean isTitleEnabled() { return titleEnabled; }
    public String getTitleText() { return titleText; }
    public boolean isGlow() { return glow; }
    public boolean isWhitelistEnabled() { return whitelistEnabled; }
    public List<String> getWhitelistBlocks() { return whitelistBlocks; }
    public boolean isBlacklistEnabled() { return blacklistEnabled; }
    public List<String> getBlacklistBlocks() { return blacklistBlocks; }
    public boolean isAutoIgnite() { return autoIgnite; }
    public boolean canExplodeInWater() { return explodeInWater; }
    public boolean canExplodeInLava() { return explodeInLava; }
    public float getPower() { return power; }
}
