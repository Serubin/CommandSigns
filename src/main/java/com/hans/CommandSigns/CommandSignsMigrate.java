package com.hans.CommandSigns;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;

public class CommandSignsMigrate {
    private CommandSigns plugin;
    private Logger log;

    public CommandSignsMigrate(CommandSigns plugin) {
        this.plugin = plugin;
        this.log = plugin.log;
    }

    public boolean loadData(String fileName) {
        // Creates new file
        File migrateFile = new File(plugin.getDataFolder(), fileName);
        FileReader fStreamIn;
        if (!migrateFile.exists()) {
            log.info("Creating '" + fileName + "'...");
            try {
                migrateFile.createNewFile();
            } catch (IOException e) {
                log.warning("'" + fileName + "' could not be created!");
                e.printStackTrace();
            }
        }
        // Loads data
        try {
            fStreamIn = new FileReader(migrateFile);
            BufferedReader in = new BufferedReader(fStreamIn);

            String line = in.readLine();
            int lineNumber = 0;
            while (line != null) {
                String[] sign = line.split(",");
                String[] lines = sign[3].split("[LINEBREAK]");
                // Processes coords
                String[] coordString = sign[1].split(":");
                double[] coord = new double[coordString.length];
                for (int i = 0; i < coordString.length; i++) {
                    try {
                        coord[i] = Double.parseDouble(coordString[i]);
                    } catch (NumberFormatException ex) {
                        // TODO do something
                    }
                }
                World world = plugin.getServer().getWorld(sign[0]);
                lineNumber++;
                if (world != null) {
                    Location loc = new Location(world, coord[0], coord[2],
                            coord[3]);
                    plugin.addSign(new CommandSignsData(0, loc,
                            new CommandSignsText(lines)));
                } else {
log.warning("There was a problem loading line number " + Integer.toString(lineNumber) + ": World does not exist.");
                }
                line = in.readLine();
            }
            in.close();
            log.info(Integer.toString(lineNumber) + "signs migrated!");
        } catch (IOException e) {
            log.warning("[Warps] There was an error loading warp data...");
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
