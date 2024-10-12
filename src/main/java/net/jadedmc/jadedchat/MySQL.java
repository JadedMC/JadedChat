package net.jadedmc.jadedchat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Manages the connection process to MySQL.
 */
public class MySQL {
    private final JadedChatPlugin plugin;
    private Connection connection;
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final boolean enabled;
    private final int port;

    /**
     * Loads the MySQL database connection info.
     * @param plugin Instance of the plugin.
     */
    public MySQL(JadedChatPlugin plugin) {
        this.plugin = plugin;
        host = plugin.getConfigManager().getConfig().getString("MySQL.host");
        database = plugin.getConfigManager().getConfig().getString("MySQL.database");
        username = plugin.getConfigManager().getConfig().getString("MySQL.username");
        password = plugin.getConfigManager().getConfig().getString("MySQL.password");
        port = plugin.getConfigManager().getConfig().getInt("MySQL.port");

        if(plugin.getConfigManager().getConfig().isSet("MySQL.enabled")) {
            enabled = plugin.getConfigManager().getConfig().getBoolean("MySQL.enabled");
        }
        else {
            enabled = false;
        }

    }

    /**
     * Close a connection.
     */
    public void closeConnection() {
        if(isConnected()) {
            try {
                connection.close();
            }
            catch(SQLException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Get the connection.
     * @return Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get if plugin is connected to the database.
     * @return Connected
     */
    private boolean isConnected() {
        return (connection != null);
    }

    /**
     * Open a MySQL Connection
     */
    public void openConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            synchronized(JadedChatPlugin.class) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false&characterEncoding=utf8", username, password);
            }

            // Prevents losing connection to MySQL.
            plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, ()-> {
                try {
                    connection.isValid(0);
                }
                catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }, 504000, 504000);

            // Generate tables
            PreparedStatement chat_logs = connection.prepareStatement("CREATE TABLE IF NOT EXISTS chat_logs (" +
                    "id INT AUTO_INCREMENT," +
                    "server VARCHAR(45) DEFAULT 'null'," +
                    "channel VARCHAR(45) DEFAULT 'global'," +
                    "uuid VARCHAR(45)," +
                    "username VARCHAR(16)," +
                    "message VARCHAR(256)," +
                    "time TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (id)" +
                    ");");
            chat_logs.execute();
        }
        catch(SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }

    }

    public boolean isEnabled() {
        return enabled;
    }
}