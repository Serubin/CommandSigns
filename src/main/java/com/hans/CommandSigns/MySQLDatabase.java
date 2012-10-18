package com.hans.CommandSigns;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

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

    }

    /**
     * Creates initial MySQL database connection
     * <p/>
     * Called from startSQL()
     */
    protected void createConnection(String host, String database,
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
    protected void createTable() {
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
                                + "`x` float NOT NULL, "
                                + "`y` float NOT NULL, "
                                + "`z` float NOT NULL, "
                                + "PRIMARY KEY (`id`)"
                                + ");");
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
                                + "`line` tinyint(11) NOT NULL, "
                                + "PRIMARY KEY (`id`)" + ");");
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
}
