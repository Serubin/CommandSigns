package com.hans.CommandSigns;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WaitCommand implements CommandExecutor {

    private CommandSigns plugin;

    public WaitCommand(CommandSigns plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        int max_time = plugin.getConfig().getInt("options.wait.max_time");
        boolean count = false;
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (plugin.hasPermission(player, "CommandSigns.use.*")) {
                int waitTime = 0;
                if (args.length > 1) {
                    return false;
                } else {
                    try {
                        waitTime = Integer.getInteger(args[0]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + args[0]
                                + " is not a valid number!");
                        return true;
                    }
                    if (args.length == 2) {
                        count = true;
                    }
                    if (waitTime > max_time) {
                        waitTime = waitTime * 1000;
                        wait(waitTime, player, count);
                    } else {
                        return false;
                    }
                }
            } else {
                return false;

            }
        }
        return false;
    }

    private boolean wait(int time, Player player, boolean count) {
        
        return false;
    }
}
