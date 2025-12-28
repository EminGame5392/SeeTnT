package ru.gdev.seetnt;

import net.md_5.bungee.api.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtil {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String color(String text) {
        if (text == null) return "";
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder builder = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            builder.append(text, lastEnd, matcher.start());
            builder.append(ChatColor.of("#" + matcher.group(1)));
            lastEnd = matcher.end();
        }
        builder.append(text.substring(lastEnd));
        return ChatColor.translateAlternateColorCodes('&', builder.toString());
    }
}
