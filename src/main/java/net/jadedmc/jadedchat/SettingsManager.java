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
    private FileConfiguration emotes;
    private final File emotesFile;

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

        emotesFile = new File(plugin.getDataFolder(), "emotes.yml");
        if(!emotesFile.exists()) {
            plugin.saveResource("emotes.yml", false);
        }
        emotes = YamlConfiguration.loadConfiguration(emotesFile);
    }

    /**
     * Get the config.yml FileConfiguration.
     * @return config.yml FileConfiguration.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get the emotes.yml FileConfiguration.
     * @return emotes.yml FileConfiguration.
     */
    public FileConfiguration getEmotes() {
        return emotes;
    }

    /**
     * Update the configuration files.
     */
    public void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
        emotes = YamlConfiguration.loadConfiguration(emotesFile);
    }
}