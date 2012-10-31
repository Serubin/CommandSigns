package com.hans.CommandSigns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import org.bukkit.Location;
import org.bukkit.World;

public class MySQLDatabase {

    private CommandSigns plugin;
    private Connection conn;

    public MySQLDatabase(CommandSigns plugin) {
        this.plugin = plugin;
        createConnection(plugin.getConfig().getString("mysql.host"), plugin
                .getConfig().getString("mysql.database"), plugin.getConfig()
                .getString("mysql.username"),
                plugin.getConfig().getString("mysql.password"));
        createTable();
        loadData();

    }

    /**
     * Creates initial MySQL database connection
     * <p/>
     * Called from startSQL()
     */
    private void createConnection(String host, String database,
            String username, String password) {
        String sqlUrl = String.format("jdbc:mysql://%s/%s", host, database);

        Properties sqlStr = new Properties();
        sqlStr.put("user", username);
        sqlStr.put("password", password);
        try {
            conn = DriverManager.getConnection(sqlUrl, sqlStr);
            plugin.logInfo("Successfully connected to database!");
        } catch (SQLException e) {
            plugin.logInfo("There was a problem connecting the the MySQL database!");
            e.printStackTrace();
        }
    }

    /**
     * Creates tables if not exists.
     * <p/>
     * Creates 'signs' and 'text' table
     */
    private void createTable() {
        try {
            // signs table
            ResultSet rs = conn.getMetaData().getTables(null, null, "signs",
                    null);
            if (!rs.next()) {
                plugin.logInfo("Creating `Signs` table...");
                PreparedStatement ps = conn
                        .prepareStatement("CREATE TABLE IF NOT EXISTS `signs` ( "
                                + "`id` mediumint(8) unsigned NOT NULL AUTO_INCREMENT, "
                                + "`world` varchar(32) NOT NULL, "
                                + "`x` double NOT NULL, "
                                + "`y` double NOT NULL, "
                                + "`z` double NOT NULL, "
                                + "PRIMARY KEY (`id`)" + ");");
                ps.executeUpdate();
                ps.close();
            } else {
            }
            rs.close();

            // text table
            rs = conn.getMetaData().getTables(null, null, "text", null);
            if (!rs.next()) {
                plugin.logInfo("Creating `text` table...");
                PreparedStatement ps = conn
                        .prepareStatement("CREATE TABLE IF NOT EXISTS `text` ( "
                                + "`id` mediumint(8) unsigned NOT NULL, "
                                + "`text` varchar(255) NOT NULL, "
                                + "`line` tinyint(3) NOT NULL " + ");");
                ps.executeUpdate();
                ps.close();
            } else {
            }
            rs.close();
        } catch (SQLException e) {
            plugin.logWarning("There was a problem creating or initializing the tables!");
            e.printStackTrace();
        }
    }

    private boolean loadData() {
        // For Signs
        PreparedStatement ps = null;
        ResultSet rs = null;
        // For text
        PreparedStatement psText = null;
        ResultSet rsText = null;
        int id = 0;
        int ErrorLoad = 0;
        try {
            ps = conn.prepareStatement("SELECT * FROM `signs`;");
            rs = ps.executeQuery();
            while (rs.next()) {
                id = rs.getInt(1);
                String worldStr = rs.getString(2);
                int x = rs.getInt(3);
                int y = rs.getInt(4);
                int z = rs.getInt(5);
                plugin.logDebug("Getting sign id: " + Integer.toString(id)
                        + ", " + worldStr + ", " + Integer.toString(x) + ", "
                        + Integer.toString(y) + ", " + Integer.toString(z));
                // Gets text
                psText = conn
                        .prepareStatement("SELECT * FROM `text` WHERE `id`=? ORDER BY `line` ASC;");
                psText.setInt(1, id);
                rsText = psText.executeQuery();
                ArrayList<String> list = new ArrayList<String>();
                while (rsText.next()) {
                    list.add(rsText.getString(2));
                    plugin.logDebug(Integer.toString(id) + rsText.getString(2));
                }
                // Checks for valid world
                World world = plugin.getServer().getWorld(worldStr);
                if (world != null) {
                    HashMaps.addSignData(new CommandSignsData(id, new Location(
                            world, x, y, z), new CommandSignsText(list
                            .toArray(new String[list.size()]))));
                    plugin.logDebug("Sign added: "
                            + new Location(world, x, y, z).toString()
                            + "with id: " + Integer.toString(id));
                } else {
                    ErrorLoad++;
                }
            }
            // Error / Success reporting
            plugin.logInfo(Integer.toString(HashMaps.signNumber())
                    + " signs loaded successfully!");
            plugin.logInfo(Integer.toString(ErrorLoad)
                    + " signs were not loaded");
        } catch (SQLException e) {
            plugin.logWarning("There was an error loading data: ");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Adds sign to database and hashmaps
     * 
     * @param loc
     *            Location of sign
     * @param lines
     *            Lines to be added
     * @return boolean if errored
     */
    public boolean addSign(Location loc, String[] lines) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int id = 0;
        try {
            ps = conn
                    .prepareStatement(
                            "INSERT INTO `signs` (`world`, `x`, `y`, `z`) VALUES (?,?,?,?);",
                            Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, loc.getWorld().getName());
            ps.setDouble(2, loc.getX());
            ps.setDouble(3, loc.getY());
            ps.setDouble(4, loc.getZ());
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();

            if (rs.next()) {
                id = rs.getInt(1);
            }
            for (int i = 0; i < lines.length; i++) {
                if (lines[i] != null) {
                    ps = conn
                            .prepareStatement("INSERT INTO `text` (`id`, text.text, `line`) VALUES (?,?,?);");
                    ps.setInt(1, id);
                    ps.setString(2, lines[i]);
                    ps.setInt(3, i);
                    ps.executeUpdate();
                }
            }
            HashMaps.addSignData(new CommandSignsData(id, loc,
                    new CommandSignsText(lines)));
            plugin.logDebug("Sign added: " + loc.toString() + "with id: "
                    + Integer.toString(id));
        } catch (SQLException e) {
            plugin.logWarning("There was an error add a CommandSign: ");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Removes CommandSign from Database
     * 
     * @param id
     *            CommandSign id
     * @return boolean if errored
     */
    public boolean removeSign(int id) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement("DELETE FROM `signs` WHERE `id`=?;");
            ps.setInt(1, id);
            ps.executeUpdate();

            ps = conn.prepareStatement("DELETE FROM `text` WHERE `id`=?;");
            ps.setInt(1, id);
            ps.executeUpdate();

            HashMaps.removeSignData(id);
        } catch (SQLException e) {
            plugin.logWarning("There was an error removing a CommandSign: ");
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
