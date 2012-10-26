package com.hans.CommandSigns;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSigns extends JavaPlugin {
    public final Logger log = Logger.getLogger("Minecraft");

    // plugin variables
    private MySQLDatabase db;

    // listeners
    private CommandSignsPlayerListener playerListener;
    private CommandSignsCommand commandExecutor;
    private CommandSignsBlockListener blockListener;
    private String version;
    private String name;
    private String tag;

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
        tag = "[" + name + "] ";
        log.info(name + " version " + version + " is loading...");
        PluginManager pm = getServer().getPluginManager();
        getConfig().options().copyDefaults(true);
        saveConfig();
        playerListener = new CommandSignsPlayerListener(this);
        commandExecutor = new CommandSignsCommand(this);
        blockListener = new CommandSignsBlockListener(this);
        db = new MySQLDatabase(this);

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

    /**
     * Logs with plugin tag at info level
     * 
     * @param line
     *            of text
     */
    public void logInfo(String line) {
        log.info(tag + line);
    }

    /**
     * Logs with plugin tag at warning level
     * 
     * @param line
     *            of text
     */
    public void logWarning(String line) {
        log.warning(tag + line);
    }

    /**
     * logs with debug tag
     * 
     * @param line
     *            of text
     */
    public void logDebug(String line) {
        if (debug) {
            log.info(tag + " DEBUG:" + line);
        }
    }

    /**
     * Adds data to database
     * 
     * @param data
     *            CommandSignsData object
     */
    public boolean addSign(CommandSignsData data) {
        if (db.addSign(data.getLocation(), data.getText().getText()))
            return true;
        else
            return false;
    }

    /**
     * Removes data from database
     * 
     * @param id
     *            Of commandsign
     */
    public boolean removeSign(int id) {
        if (db.removeSign(id))
            return true;
        else
            return false;
    }
}