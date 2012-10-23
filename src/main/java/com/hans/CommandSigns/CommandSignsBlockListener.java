package com.hans.CommandSigns;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class CommandSignsBlockListener implements Listener {
    private CommandSigns plugin;

    public CommandSignsBlockListener(CommandSigns plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        Block block = event.getBlock();
        if (block.getType() == Material.SIGN_POST
                || block.getType() == Material.WALL_SIGN) {

            if (plugin.signCheck(block.getLocation())) {
                event.getPlayer().sendMessage(
                        ChatColor.RED
                                + "CommandSign text must be removed first.");
                event.setCancelled(true);
            }
        }
    }

}
