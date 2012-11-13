package com.hans.CommandSigns;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Location;

public class HashMaps {

    private final static HashMap<String, CommandSignsPlayerState> playerStates = new HashMap<String, CommandSignsPlayerState>();
    private final static HashMap<String, Integer> activeSignIds = new HashMap<String, Integer>();
    private final static HashMap<Integer, CommandSignsData> activeSigns = new HashMap<Integer, CommandSignsData>();
    private final static HashMap<String, CommandSignsText> playerText = new HashMap<String, CommandSignsText>();
    private final static HashMap<String, String> playerSignNames = new HashMap<String, String>();
    private final static HashMap<String, CommandSignsData> signEdit = new HashMap<String, CommandSignsData>();

    /**
     * Get PlayerState from player name
     * 
     * @param player
     * @return CommandSignsPlayerState
     */
    public static CommandSignsPlayerState getPlayerStates(String player) {
        return playerStates.get(player);
    }

    /**
     * Add PlayerState
     * 
     * @param player
     * @param playerState
     */
    public static void addPlayerState(String player,
            CommandSignsPlayerState playerState) {
        playerStates.put(player, playerState);
    }

    /**
     * Remove playerState
     * 
     * @param player
     */
    public static void removePlayerState(String player) {
        playerStates.remove(player);
    }

    /**
     * Check if player text exists
     * 
     * @param player
     * @return boolean
     */
    public static boolean checkPlayerText(String player) {
        return playerText.containsKey(player);
    }

    /**
     * Get PlayerText from player name
     * 
     * @param player
     * @return CommandSignsText
     */
    public static CommandSignsText getPlayerText(String player) {
        return playerText.get(player);
    }

    /**
     * Add PlayerText
     * 
     * @param player
     * @param playerState
     */
    public static void addPlayerText(String player, CommandSignsText text) {
        playerText.put(player, text);
    }

    /**
     * Remove playerText
     * 
     * @param player
     */
    public static void removePlayerText(String player) {
        playerText.remove(player);
    }

    /**
     * Does PlayerText contain key
     * 
     * @param player
     * @return boolean
     */
    public static boolean playerTextContainsKey(String player) {
        return playerText.containsKey(player);
    }

    /**
     * Get sign id
     * 
     * @param loc Location of sign
     * @return Sign id
     */
    public static int getSignId(Location loc) {
        return activeSignIds.get(formatLoc(loc));
    }

    /**
     * Gets sign text from location
     * 
     * @param loc Sign location
     * @return Sign text
     */
    public static CommandSignsText getSignText(Location loc) {
        return activeSigns.get(getSignId(loc)).getText();
    }

    /**
     * Checks if commandsigns exists
     * 
     * @param loc Location of commandsign
     * @return boolean
     */
    public static boolean signCheck(Location loc) {
        return activeSignIds.containsKey(formatLoc(loc));
    }

    /**
     * Checks if commandsigns exists
     * 
     * @param loc String - Location of commandsign
     * @return boolean
     */
    public static boolean signCheck(String loc) {
        return activeSignIds.containsKey(loc);
    }

    /**
     * Get total signs
     * 
     * @return int
     */
    public static int signNumber() {
        return activeSigns.size();
    }

    /**
     * Checks if commandsign exists
     * 
     * @param id Id of commandsign
     * @return boolean
     */
    public boolean signCheck(int id) {
        return activeSigns.containsKey(id);
    }

    /**
     * Add sign data to hashmaps, both activeSigns and ActiveSignIds
     * <p/>
     * Should only be called by MySQLDatabase
     * 
     * @param data
     */
    public static void addSignData(CommandSignsData data) {
        activeSigns.put(data.getId(), data);
        Location loc = data.getLocation();
        activeSignIds.put(formatLoc(loc), data.getId());
    }

    /**
     * Adds id and location to hashmap activeSignsIds
     * <p/>
     * Should only be called by MySQLDatabase
     * 
     * @param id
     * @param loc
     */
    public static void addSignData(int id, Location loc) {
        activeSignIds.put(formatLoc(loc), id);
    }

    /**
     * Add sign data to hashmaps
     * <p/>
     * Should only be called by MySQLDatabase
     * 
     * @param id
     * @param loc
     * @param lines
     */
    public static void addSignData(int id, Location loc, String[] lines) {
        activeSigns.put(id, new CommandSignsData(id, loc, new CommandSignsText(
                lines)));
    }

    /**
     * Removes sign data from hashmaps
     * <p/>
     * Should only be called by MySQLDatabase
     * 
     * @param id Sign id
     */
    public static void removeSignData(int id) {
        activeSignIds.remove(formatLoc(activeSigns.get(id).getLocation()));
        activeSigns.remove(id);
    }

    /**
     * Initializes sign edit - adds sign data.
     * 
     * @param player
     * @param loc of the sign being edited
     */
    public static void initEdit(String player, Location loc) {
        signEdit.put(player, new CommandSignsData(getSignId(loc), loc,
                getSignText(loc)));
    }

    public static boolean comfirmEdit(String player) {
        if (signEdit.containsKey(player))
            return true;
        else
            return false;
    }

    /**
     * Return text to manipulate
     * 
     * @param player
     * @return Text
     */
    public static CommandSignsText getEditText(String player) {
        return signEdit.get(player).getText();
    }

    /**
     * Gets location of signs
     * <p/>
     * Always check if sign exists before using
     * 
     * @param player Player name
     * @return String formated location
     */
    public static String getEditLoc(String player) {
        return formatLoc(signEdit.get(player).getLocation());
    }

    /**
     * Set text line by line
     * 
     * @param player Name using .getName()
     * @param index Index of line
     * @param line Line of text
     */
    public static void setTextEdit(String player, int index, String line) {
        CommandSignsData data = signEdit.get(player);
        CommandSignsText text = data.getText();
        text.setLine(index, line);
        data.setText(text.getText());
        signEdit.put(player, data);
    }

    /**
     * Set text from array
     * 
     * @param player Naame using .getName()
     * @param lines array of text
     */
    public static void setTextEdit(String player, String[] lines) {
        CommandSignsData data = signEdit.get(player);
        data.setText(lines);
        signEdit.put(player, data);
    }

    /**
     * Returns CommandSigns data
     * 
     * @param player
     * @return
     */
    public static CommandSignsData getEditData(String player) {
        return signEdit.get(player);
    }

    /**
     * Removes playerState and signEdit data
     * <p/>
     * Should only be called from MySQLDatabase
     * 
     * @param player
     */
    public static void updateText(String player) {
        playerStates.remove(player);
        signEdit.remove(player);
    }

    /**
     * Get sign named to be added to data
     * 
     * @param player
     * @return Sign name
     */
    public static String getSignName(String player) {
        return playerSignNames.get(player);
    }

    /**
     * Set sign name to be added
     * 
     * @param player Player name
     * @param name Sign name
     */
    public static void setSignName(String player, String name) {
        playerSignNames.put(player, name);
    }

    /**
     * Remove sign name from player
     * 
     * @param player
     */
    public static void removeSignName(String player) {
        playerSignNames.remove(player);
    }

    /**
     * Active Signs to string
     * 
     * @return string
     */
    public static String activeSignsToString() {
        StringBuffer str = new StringBuffer();
        str.append("Active sign data: [");
        for (CommandSignsData data : activeSigns.values()) {
            str.append(data.toString() + ", ");
        }
        str.append("]");
        return str.toString();
    }

    /**
     * Active Sign Ids to string
     * 
     * @return string
     */
    public static String activeSignsIdsToString() {
        StringBuffer str = new StringBuffer();
        str.append("Active sign ids: [");
        for (Entry<String, Integer> entry : activeSignIds.entrySet()) {
            str.append("[" + entry.getKey() + ", " + entry.getValue() + "], ");
        }
        str.append("]");
        return str.toString();
    }

    /**
     * Formats location to string from
     * 
     * @param loc
     * @return String of loc
     */
    public static String formatLoc(Location loc) {
        return loc.getWorld() + ":" + loc.getBlockX() + ":" + loc.getBlockY()
                + ":" + loc.getBlockZ();
    }
}
// TODO add comments add edit