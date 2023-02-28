package net.jadedmc.jadedchat.channels;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

/**
 * Represents a section of chat separated from others.
 */
public class Channel {
    private final String name;
    private final List<String> aliases = new ArrayList<>();
    private final String permission;
    private final boolean defaultChannel;
    private final FileConfiguration rawConfig;
    private final Map<String, Format> formats = new LinkedHashMap<>();

    /**
     * Creates the channel object.
     * @param file Configuration file of the channel.
     */
    public Channel(File file) {
        rawConfig = YamlConfiguration.loadConfiguration(file);

        // Loads channel settings.
        this.name = rawConfig.getString("name");
        this.permission = rawConfig.getString("permission");
        this.defaultChannel = rawConfig.getBoolean("default");

        // Loads formats.
        ConfigurationSection allFormats = rawConfig.getConfigurationSection("formats");
        if(allFormats != null) {
            for(String formatID : allFormats.getKeys(false)) {
                ConfigurationSection formatSection = allFormats.getConfigurationSection(formatID);

                // Makes sure the format isn't null.
                if(formatSection == null) {
                    continue;
                }

                // Stores the format.
                formats.put(formatID, new Format(formatSection));
            }
        }
    }

    /**
     * Gets a list of aliases for the channel.
     * Used primarily when switching channels.
     * @return Collection of all aliases.
     */
    public Collection<String> getAliases() {
        return aliases;
    }

    /**
     * Get a format based off its id.
     * @param format ID of the format.
     * @return Associated format.
     */
    public Format getFormat(String format) {
        return formats.get(format);
    }

    /**
     * Get the format a player should be using when chatting in the channel.
     * @param player Player to get the format of.
     * @return Proper format.
     */
    public Format getFormat(Player player) {
        // Sets the default chat format to "default" in case no permissions found.
        String format = "default";

        // Loops through all the chat formats to find the highest format allowed.
        for(String str : formats.keySet()) {
            if(player.hasPermission("format." + str)) {
                format = str;
                break;
            }
        }

        return getFormat(format);
    }

    /**
     * Gets the name of the channel.
     * @return Channel display name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the permission node required to use the channel.
     * @return Permission node in string form, empty if there is none.
     */
    public String getPermissionNode() {
        return permission;
    }

    /**
     * Gets the raw FileConfiguration for the channel.
     * Can be used by other plugins to get and save custom settings.
     * @return FileConfiguration.
     */
    public FileConfiguration getRawConfig() {
        return rawConfig;
    }

    /**
     * Get if the channel is the default channel.
     * @return Whether the channel is the default channel.
     */
    public boolean isDefaultChannel() {
        return defaultChannel;
    }
}