package com.hans.CommandSigns;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandSigns extends JavaPlugin {
    public final Logger log = Logger.getLogger("Minecraft");

    // plugin variables
    // TODO rework hashmaps and data storage
    private final HashMap<String, CommandSignsPlayerState> playerStates = new HashMap<String, CommandSignsPlayerState>();
    private final HashMap<Location, Integer> activeSignIds = new HashMap<Location, Integer>();
    private final HashMap<Integer, CommandSignsData> activeSigns = new HashMap<Integer, CommandSignsData>();
    private final HashMap<String, CommandSignsText> playerText = new HashMap<String, CommandSignsText>();

    private MySQLDatabase db = null;

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
     * Get PlayerState from player name
     * 
     * @param player
     * @return CommandSignsPlayerState
     */
    public CommandSignsPlayerState getPlayerStates(String player) {
        return playerStates.get(player);
    }

    /**
     * Add PlayerState
     * 
     * @param player
     * @param playerState
     */
    public void addPlayerState(String player,
            CommandSignsPlayerState playerState) {
        playerStates.put(player, playerState);
    }

    /**
     * Remove playerState
     * 
     * @param player
     */
    public void removePlayerState(String player) {
        playerStates.remove(player);
    }

    /**
     * Get PlayerText from player name
     * 
     * @param player
     * @return CommandSignsText
     */
    public CommandSignsText getPlayerText(String player) {
        return playerText.get(player);
    }

    /**
     * Add PlayerText
     * 
     * @param player
     * @param playerState
     */
    public void addPlayerText(String player, CommandSignsText text) {
        playerText.put(player, text);
    }

    /**
     * Remove playerText
     * 
     * @param player
     */
    public void removePlayerText(String player) {
        playerText.remove(player);
    }

    /**
     * Does PlayerText contain key
     * 
     * @param player
     * @return boolean
     */
    public boolean playerTextContainsKey(String player) {
        return playerText.containsKey(player);
    }
}