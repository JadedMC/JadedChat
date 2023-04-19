/*
 * This file is part of JadedChat, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
package net.jadedmc.jadedchat.features.channels;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import github.scarsz.discordsrv.DiscordSRV;
import net.jadedmc.jadedchat.JadedChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

/**
 * Represents a section of chat separated from others.
 */
public class Channel {
    private final JadedChat plugin;
    private final String name;
    private final List<String> aliases = new ArrayList<>();
    private final String permission;
    private final boolean defaultChannel;
    private final boolean useBungeecord;
    private final boolean useDiscordSRV;
    private final FileConfiguration rawConfig;
    private final Map<String, Format> formats = new LinkedHashMap<>();
    private int range = -1;

    /**
     * Creates the channel object.
     * @param file Configuration file of the channel.
     */
    public Channel(JadedChat plugin, File file) {
        this.plugin = plugin;
        rawConfig = YamlConfiguration.loadConfiguration(file);

        // Loads channel settings.
        this.name = rawConfig.getString("name").toUpperCase();
        this.permission = rawConfig.getString("permission");

        // Sets if the channel should be the default channel.
        if(!rawConfig.isSet("settings.default")) {
            this.defaultChannel = true;
        }
        else {
            this.defaultChannel = rawConfig.getBoolean("settings.default");
        }

        // Sets if the channel should use BungeeCord.
        if(!rawConfig.isSet("settings.bungeecord")) {
            this.useBungeecord = false;
        }
        else {
            this.useBungeecord = rawConfig.getBoolean("settings.bungeecord");
        }

        // Sets if the channel should use DiscordSRV.
        if(!rawConfig.isSet("settings.DiscordSRV")) {
            this.useDiscordSRV = false;
        }
        else {
            this.useDiscordSRV = rawConfig.getBoolean("settings.DiscordSRV");
        }

        if(rawConfig.isSet("settings.range")) {
            this.range = rawConfig.getInt("settings.range");
        }

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

        // Looks for a format matching the player's group if LuckPerms is enabled.
        if(plugin.getHookManager().useLuckPerms()) {
            // Get the player's current primary group.
            LuckPerms luckperms = LuckPermsProvider.get();
            String group = luckperms.getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();

            // Check if the group has a format.
            if(formats.containsKey(group)) {
                return getFormat(group);
            }
        }

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
     * Gets a collection of all players who should be able to view a message sent by a player in the channel.
     * @param sender Player who sent the message. Can be null.
     * @return All players who can see that message.
     */
    public Collection<Player> getViewers(Player sender) {
        Collection<Player> hasPermission = new ArrayList<>();

        // If the permission is empty, return all online players.
        if(permission.equalsIgnoreCase("")) {
            hasPermission.addAll(Bukkit.getOnlinePlayers());
        }
        else {
            // Checks each player to see if they have the proper permission.
            for(Player player : Bukkit.getOnlinePlayers()) {
                // If so, add them as a viewer.
                if(player.hasPermission(permission)) {
                    hasPermission.add(player);
                }
            }
        }


        Collection<Player> inRange = new ArrayList<>();

        // If the range is less than one, add all players with permission.
        if(range < 1) {
            inRange.addAll(hasPermission);
        }
        else {
            // If not, loop through all players with permission and check if they are in range.
            for(Player player : hasPermission) {
                if(player.getLocation().distance(sender.getLocation()) <= range) {
                    inRange.add(player);
                }
            }
        }

        return inRange;
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
        // Checks if the message passes the chat filter.
        if(!plugin.getFilterManager().passesFilter(player, this, message)) {
            return;
        }

        // Creates the formatted component of the message.
        Component messageComponent = getFormat(player).processMessage(player, message);

        // Send the message to all channel viewers.
        getViewers(player).forEach(viewer -> viewer.sendMessage(messageComponent));

        // Send the message to the console as well
        Bukkit.getServer().getConsoleSender().sendMessage(Component.text().content("[" + name + "] ").append(messageComponent).build());

        // Sends the message through bungeecord if bungeecord is enabled for the channel.
        if(useBungeecord) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF("Forward");
            out.writeUTF("ALL");
            out.writeUTF("jadedchat");
            out.writeUTF(name.toLowerCase() + "~~" + MiniMessage.miniMessage().serialize(messageComponent));

            // Sends the message to bungeecord, to send back to all online servers.
            player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
        }

        // Sends the message through DiscordSRV if enabled.
        if(plugin.getHookManager().useDiscordSRV() && useDiscordSRV) {
            DiscordSRV.getPlugin().getMainTextChannel().sendMessage(MiniMessage.miniMessage().stripTags(MiniMessage.miniMessage().serialize(messageComponent)));
        }
    }

    /**
     * Sends a message to the channel without a Player object.
     * Used when processing bungeecord messages.
     * @param message Full message being sent.
     */
    public void sendMessage(String message) {
        // Creates the formatted component of the message.
        Component component = MiniMessage.miniMessage().deserialize(message);

        // Send the message to all channel viewers.
        getViewers(null).forEach(viewer -> viewer.sendMessage(component));

        // Send the message to the console as well
        Bukkit.getServer().getConsoleSender().sendMessage(Component.text().content("(Bungee) [" + name + "] ").append(component).build());
    }
}