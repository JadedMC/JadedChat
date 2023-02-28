package net.jadedmc.jadedchat;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Manages the configurable settings in the plugin.
 */
public class SettingsManager {
    private FileConfiguration config;
    private final File configFile;

    /**
     * Loads or Creates configuration files.
     * @param plugin Instance of the plugin.
     */
    public SettingsManager(JadedChat plugin) {
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        configFile = new File(plugin.getDataFolder(), "config.yml");
        plugin.saveConfig();

        File globalChannel = new File(plugin.getDataFolder(), "/channels/global.yml");
        if(!globalChannel.exists()) {
            plugin.saveResource("channels/global.yml", false);
        }
    }

    /**
     * Get the config.yml FileConfiguration.
     * @return config.yml FileConfiguration.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Update the configuration files.
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
}