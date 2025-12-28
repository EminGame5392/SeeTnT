package ru.gdev.seetnt;

import net.md_5.bungee.api.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    public static String color(String input) {
        if (input == null || input.isEmpty()) return "";

        Matcher matcher = HEX_PATTERN.matcher(input);
        StringBuffer buffer = new StringBuffer();

        while (matcher.find()) {
            String hexCode = matcher.group(1);
            ChatColor color = ChatColor.of("#" + hexCode);
            matcher.appendReplacement(buffer, color.toString());
        }
        matcher.appendTail(buffer);

        return ChatColor.translateAlternateColorCodes('&', buffer.toString());
    }

    public static List<String> colorList(List<String> list) {
        if (list == null) return new ArrayList<>();
        List<String> colored = new ArrayList<>();
        for (String line : list) {
            colored.add(color(line));
        }
        return colored;
    }
}
