package net.jadedmc.jadedchat.features.channels;

import net.jadedmc.jadedchat.JadedChat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
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
    public Channel(JadedChat plugin, File file) {
        rawConfig = YamlConfiguration.loadConfiguration(file);

        // Loads channel settings.
        this.name = rawConfig.getString("name").toUpperCase();
        this.permission = rawConfig.getString("permission");
        this.defaultChannel = rawConfig.getBoolean("default");

        // Add the aliases.
        for(String alias : rawConfig.getStringList("aliases")) {
            aliases.add(alias.toUpperCase());
        }

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
                formats.put(formatID, new Format(plugin, formatSection));
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

    /**
     * Sends a message to the channel.
     * @param player Player who sent the message.
     * @param message Message the player is sending.
     */
    public void sendMessage(Player player, String message) {
        // Creates the formatted component of the message.
        Component messageComponent = getFormat(player).processMessage(player, message);

        // Send the message to all players online.
        for(Player target : Bukkit.getServer().getOnlinePlayers()) {
            // Makes sure the player should be able to see the channel.
            if(!target.hasPermission(getPermissionNode())) {
                continue;
            }

            // Sends the message to the player.
            target.sendMessage(messageComponent);
        }

        // Send the message to the console as well
        Bukkit.getServer().getConsoleSender().sendMessage(Component.text().content("[" + name + "] ").append(messageComponent).build());

        // TODO: DiscordSRV Support
        // TODO: Bungeecord Support
    }
}