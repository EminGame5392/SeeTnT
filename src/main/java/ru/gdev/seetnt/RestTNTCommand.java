package ru.gdev.seetnt;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;

public class RestTNTCommand implements CommandExecutor, TabCompleter {

    private final SeeTnT plugin;

    public RestTNTCommand(SeeTnT plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Utils.color("&c/resttnt reload | give <ник> <tnt_id> <кол-во>"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            plugin.reloadConfig();
            plugin.getTNTManager().loadTNTs();
            sender.sendMessage(Utils.color("&aКонфигурация перезагружена!"));
            return true;
        }

        if (args[0].equalsIgnoreCase("give")) {
            if (args.length < 4) {
                sender.sendMessage(Utils.color("&c/resttnt give <ник> <tnt_id> <кол-во>"));
                return true;
            }
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(Utils.color("&cИгрок не найден"));
                return true;
            }
            CustomTNT tnt = plugin.getTNTManager().getTNT(args[2]);
            if (tnt == null) {
                sender.sendMessage(Utils.color("&cTNT не найден"));
                return true;
            }
            int amount;
            try {
                amount = Integer.parseInt(args[3]);
            } catch (Exception e) {
                sender.sendMessage(Utils.color("&cНекорректное число"));
                return true;
            }
            ItemStack item = ItemMetaUtil.createTNTItem(tnt, amount, plugin);
            target.getInventory().addItem(item);
            sender.sendMessage(Utils.color("&aВыдано TNT " + args[2] + " x" + amount + " игроку " + target.getName()));
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return filter(Arrays.asList("reload", "give"), args[0]);
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            return filter(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), args[1]);
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("give")) {
            return filter(new ArrayList<>(plugin.getTNTManager().getTNTs().keySet()), args[2]);
        }
        return Collections.emptyList();
    }

    private List<String> filter(List<String> base, String start) {
        return base.stream().filter(s -> s.toLowerCase().startsWith(start.toLowerCase())).collect(Collectors.toList());
    }
}
