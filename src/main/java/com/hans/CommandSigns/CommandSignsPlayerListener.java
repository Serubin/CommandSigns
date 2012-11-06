package com.hans.CommandSigns;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class CommandSignsPlayerListener implements Listener {
	private CommandSigns plugin;

	public CommandSignsPlayerListener(CommandSigns instance){
		plugin = instance;
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
	    CommandSignsSignClickEvent signClickEvent = new CommandSignsSignClickEvent(plugin);
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.PHYSICAL) {
			Block block = event.getClickedBlock();
			if(
			        block.getType()==Material.WALL_SIGN ||
			        block.getType()==Material.SIGN_POST ||
			        block.getType()==Material.STONE_PLATE  ||
			        block.getType()==Material.WOOD_PLATE ||
			        block.getType()==Material.STONE_BUTTON ||
			        block.getType()==Material.LEVER
			        ){
			    //Try to patch pressure pad kick when TP'd
			    if(block.getType()==Material.STONE_PLATE  ||
                    block.getType()==Material.WOOD_PLATE){
			        event.setCancelled(true);
			    }
				signClickEvent.onRightClick(event, block);
			}
		}
	}
}