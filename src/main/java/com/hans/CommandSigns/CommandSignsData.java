package com.hans.CommandSigns;

import org.bukkit.Location;

public class CommandSignsData {
    private int id;
    private Location loc;
    private CommandSignsText text;

    public CommandSignsData(int id, Location loc, CommandSignsText text) {
        this.id = id;
        this.loc = loc;
        this.text = text;
    }

    /**
     * Gets id
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets location
     * 
     * @return location
     */
    public Location getLocation() {
        return loc;
    }

    /**
     * Set location
     * 
     * @param loc
     *            Location
     */
    public void setWorld(Location loc) {
        this.loc = loc;
    }

    /**
     * Gets text
     * 
     * @return text
     */
    public CommandSignsText getText() {
        return text;
    }

    /**
     * Sets text from array
     * 
     * @param lines
     *            Array of text
     */
    public void setText(String[] lines) {
        this.text.setLine(lines);
    }

    /**
     * Sets text from index
     * 
     * @param index
     * @param line
     *            text
     */
    public void setText(int index, String line) {
        this.text.setLine(index, line);
    }
}
