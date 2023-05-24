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
package net.jadedmc.jadedchat.features.channels.channel;

import net.jadedmc.jadedchat.features.channels.fomat.ChatFormat;
import net.jadedmc.jadedchat.features.channels.fomat.ChatFormatBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * Utility class that allows you to easily create ChatChannel objects.
 */
public class ChatChannelBuilder {
    private final ChatChannel channel;

    /**
     * Starts a ChatChannelBuilder using just the id of the channel.
     * @param name Name (ID) of the channel.
     */
    public ChatChannelBuilder(String name) {
        channel = new ChatChannel(name);
    }

    /**
     * Creates a ChatChannelBuilder using a channel configuration file.
     * @param file Configuration File.
     */
    public ChatChannelBuilder(File file) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        channel = new ChatChannel(config.getString("name"));

        if(config.isSet("displayName")) setDisplayName(config.getString("displayName"));
        if(config.isSet("permission")) setPermission(config.getString("permission"));

        // Settings
        if(config.isSet("settings.bungeecord")) useBungeecord(config.getBoolean("settings.bungeecord")) ;
        if(config.isSet("settings.default")) setDefaultChannel(config.getBoolean("settings.default"));
        if(config.isSet("settings.DiscordSRV")) useDiscordSRV(config.getBoolean("settings.DiscordSRV"));
        if(config.isSet("settings.range")) chatRange(config.getInt("settings.range"));

        // Load channel aliases
        if(config.isSet("aliases")) addAliases(config.getStringList("aliases"));

        // Loads formats from the config file.
        {
            ConfigurationSection formatsSection = config.getConfigurationSection("formats");
            if(formatsSection == null) {
                return;
            }

            for(String format : formatsSection.getKeys(false)) {
                addChatFormat(new ChatFormatBuilder(config.getConfigurationSection("formats." + format)).build());
            }
        }
    }

    /**
     * Adds an alias to the channel.
     * Used in the /channel command.
     * @param alias Alias to add to the channel.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder addAlias(String alias) {
        channel.addAlias(alias);
        return this;
    }

    /**
     * Adds a collection of aliases to the channel.
     * Used in the /channel command.
     * @param aliases Aliases to add to the channel.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder addAliases(Collection<String> aliases) {
        aliases.forEach(channel::addAlias);
        return this;
    }

    /**
     * Adds an array of aliases to the channel.
     * Used in the /channel command.
     * @param aliases Aliases to add to the channel.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder addAliases(String... aliases) {
        Arrays.stream(aliases).toList().forEach(channel::addAlias);
        return this;
    }

    /**
     * Adds a ChatFormat to the channel.
     * @param chatFormat Format to add to the channel.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder addChatFormat(ChatFormat chatFormat) {
        channel.addChatFormat(chatFormat);
        return this;
    }

    /**
     * Adds a collection of ChatFormats to the channel.
     * @param chatFormats ChatFormats to add to the channel.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder addChatFormats(Collection<ChatFormat> chatFormats) {
        chatFormats.forEach(channel::addChatFormat);
        return this;
    }

    /**
     * Adds an array of ChatFormats to the channel.
     * @param chatFormats ChatFormats to add to the channel
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder addChatFormats(ChatFormat... chatFormats) {
        Arrays.stream(chatFormats).toList().forEach(channel::addChatFormat);
        return this;
    }

    /**
     * Create the ChatChannel object.
     * @return ChatChannel.
     */
    public ChatChannel build() {
        return channel;
    }

    /**
     * Set the range (in blocks) chat messages should "travel".
     * Use -1 to disable.
     * Defaults to -1.
     * @param chatRange Chat range (in blocks).
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder chatRange(int chatRange) {
        channel.chatRange(chatRange);
        return this;
    }

    /**
     * Set if the channel messages should only show to players in the same world
     * @param perWorld If messages should be per-world.
     * @return
     */
    public ChatChannelBuilder perWorld(boolean perWorld) {
        channel.perWorld(perWorld);
        return this;
    }

    /**
     * Set if the channel should be the default channel.
     * Defaults to false.
     * @param isDefaultChannel If the channel should be default.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder setDefaultChannel(boolean isDefaultChannel) {
        channel.isDefaultChannel(isDefaultChannel);
        return this;
    }

    /**
     * Sets the display name of the channel.
     * Supports color codes using MiniMessage.
     * Defaults to the channel id.
     * @param displayName Display Name of the channel.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder setDisplayName(String displayName) {
        channel.displayName(displayName);
        return this;
    }

    /**
     * Set the permission node required to use the channel.
     * Defaults to an empty string.
     * @param permission Permission node a player would need.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder setPermission(String permission) {
        channel.permission(permission);
        return this;
    }

    /**
     * Set if the channel should broadcast messages through bungeecord.
     * @param useBungeecord Whether the channel should use bungeecord.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder useBungeecord(boolean useBungeecord) {
        channel.useBungeecord(useBungeecord);
        return this;
    }

    /**
     * Set if the channel should broadcast messages through DiscordSRV.
     * @param useDiscordSRV Whether the channel should use DiscordSRV.
     * @return ChatChannelBuilder.
     */
    public ChatChannelBuilder useDiscordSRV(boolean useDiscordSRV) {
        channel.useDiscordSRV(useDiscordSRV);
        return this;
    }
}