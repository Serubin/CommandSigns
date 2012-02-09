package com.hans.CommandSigns;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CommandSignsBlockListener implements Listener {
	private CommandSigns plugin;
	
	public CommandSignsBlockListener(CommandSigns plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) {
			return;
		}
		Block block  = event.getBlock();
		if(block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN) {
			CommandSignsLocation location = new CommandSignsLocation(block.getX(), block.getY(), block.getZ());
			if(plugin.activeSigns.containsKey(location)) {
				event.getPlayer().sendMessage("�cCommandSign text must be removed first.");
				event.setCancelled(true);
			}
		}
	}

}
