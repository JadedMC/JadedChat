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
package net.jadedmc.jadedchat;

import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;

public class JadedChat {
    private static JadedChatPlugin plugin;

    /**
     * Check if a channel exists.
     * @param channel Channel name to check if it exists.
     * @return Whether the channel exists.
     */
    public static boolean channelExists(String channel) {
        return (plugin.channelManager().getChannel(channel) != null);
    }

    /**
     * Get a channel based on its name.
     * @param channelName Name of the channel.
     * @return ChatChannel object.
     */
    public static ChatChannel getChannel(String channelName) {
        return plugin.channelManager().getChannel(channelName);
    }

    /**
     * Get the channel a player is currently in.
     * @param player Player to get channel of.
     * @return The ChatChannel they are in.
     */
    public static ChatChannel getChannel(Player player) {
        return plugin.channelManager().getChannel(player);
    }

    /**
     * Get the JadedChat data folder. Used for saving channel config files.
     * <b>Internal Use Only</b>
     * @return Plugin data folder.
     */
    public static File getDataFolder() {
        return plugin.getDataFolder();
    }

    /**
     * Get the set default channel.
     * @return Default channel.
     */
    public static ChatChannel getDefaultChannel() {
        return plugin.channelManager().getDefaultChannel();
    }

    /**
     * Get a collection of all currently loaded channels.
     * @return All loaded channels.
     */
    public static Collection<ChatChannel> getLoadedChannels() {
        return plugin.channelManager().getLoadedChannels();
    }

    /**
     * Gets the configured server name.
     * @return Configured server name.
     */
    public String getServer() {
        return plugin.getConfigManager().getConfig().getString("server");
    }

    /**
     * Registers a channel with JadedChat.
     * @param channel Channel to register.
     */
    public static void loadChannel(ChatChannel channel) {
        plugin.channelManager().loadChannel(channel);
    }

    /**
     * Change the player's current channel.
     * @param player Player to change channel of.
     * @param channel New channel.
     */
    public static void setChannel(Player player, ChatChannel channel) {
        plugin.channelManager().setChannel(player, channel);
    }

    /**
     * Pass the JadedChat JavaPlugin to the API.
     * <b>Internal Use Only</b>
     * @param pl JadedChatPlugin instance.
     */
    public static void setPlugin(JadedChatPlugin pl) {
        plugin = pl;
    }

    /**
     * Get if the plugin should use LuckPerms.
     * <b>Internal use only</b>
     * @return Whether it should use luckperms.
     */
    public static boolean useLuckPerms() {
        return plugin.hookManager().useLuckPerms();
    }
}