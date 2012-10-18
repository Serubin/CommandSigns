package com.hans.CommandSigns;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSigns extends JavaPlugin {
    public final Logger log = Logger.getLogger("Minecraft");

    // plugin variables
    public final HashMap<String, CommandSignsPlayerState> playerStates = new HashMap<String, CommandSignsPlayerState>();
    public final HashMap<Location, CommandSignsText> activeSigns = new HashMap<Location, CommandSignsText>();
    public final HashMap<String, CommandSignsText> playerText = new HashMap<String, CommandSignsText>();

    // listeners
    private final CommandSignsPlayerListener playerListener = new CommandSignsPlayerListener(
            this);
    private CommandSignsCommand commandExecutor = new CommandSignsCommand(this);
    private final CommandSignsBlockListener blockListener = new CommandSignsBlockListener(
            this);

    private String version;
    private String name;
    private String tag = "[" + name + "]";

    private boolean debug;

    @Override
    public void onDisable() {
        PluginDescriptionFile pdfFile = this.getDescription();
        this.log.info(pdfFile.getName() + " is disabled.");
    }

    @Override
    public void onEnable() {
        version = this.getDescription().getVersion();
        name = this.getDescription().getName();
        log.info(name + " version " + version + " is loading...");
        PluginManager pm = getServer().getPluginManager();
        getConfig().options().copyDefaults(true);
        saveConfig();

        getCommand("commandsigns").setExecutor(commandExecutor);

        pm.registerEvents(this.playerListener, this);
        pm.registerEvents(this.blockListener, this);
    }

    public boolean hasPermission(Player player, String string) {
        boolean permission = player.hasPermission(string);
        if (permission == false) {
            player.sendMessage(ChatColor.RED + "You don't have permission!");
        }
        return permission;
    }

    public void logInfo(String line) {
        log.info(tag + line);
    }
    public void logDebug(String line){
        if(debug){
            log.info(tag + " DEBUG:" + line);
        }
    }
}