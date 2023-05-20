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

import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;
import net.jadedmc.jadedchat.features.channels.channel.ChatChannelBuilder;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class ChatChannelManager {
    private final JadedChatPlugin plugin;
    private final Map<String, ChatChannel> channelIDs = new HashMap<>();
    private final Collection<ChatChannel> loadedChannels = new HashSet<>();
    private final Map<UUID, String> playerChannels = new HashMap<>();
    private ChatChannel defaultChannel;

    public ChatChannelManager(JadedChatPlugin plugin) {
        this.plugin = plugin;
        loadChannels();
    }

    public void loadChannel(ChatChannel channel) {
        loadedChannels.add(channel);
        channelIDs.put(channel.name(), channel);

        // Adds the aliases of the channel to the map to speed up lookups.
        for(String alias : channel.aliases()) {
            channelIDs.put(alias, channel);
        }

        if(channel.isDefaultChannel()) {
            defaultChannel = channel;
        }
    }

    /**
     * Loads all channels set up in the 'channels' folder.
     */
    public void loadChannels() {
        // Makes sure the loaded channels are empty.
        channelIDs.clear();
        loadedChannels.clear();

        // Grabs all channel configuration files.
        File[] formatFiles = new File(plugin.getDataFolder(), "channels").listFiles();
        if(formatFiles == null) {
            return;
        }

        // Loop through each file an load it's channel.
        for(File file : formatFiles) {
            ChatChannel channel = new ChatChannelBuilder(file).build();
            loadChannel(channel);
        }
    }

    /**
     * Get a channel based off its name or aliases.
     * @param name Name (or alias) of the channel.
     * @return Corresponding channel.
     */
    public ChatChannel getChannel(String name) {
        name = name.toUpperCase();

        if(!channelIDs.containsKey(name)) {
            return null;
        }

        return channelIDs.get(name);
    }

    /**
     * Get the channel a player is currently in.
     * @param player Player to get the channel of.
     * @return The channel they are currently using. Returns the default channel if not saved.
     */
    public ChatChannel getChannel(Player player) {
        if(playerChannels.containsKey(player.getUniqueId())) {
            return getChannel(playerChannels.get(player.getUniqueId()));
        }

        return defaultChannel;
    }

    /**
     * Get the default channel chat should use.
     * @return Default chat channel.
     */
    public ChatChannel getDefaultChannel() {
        return defaultChannel;
    }

    /**
     * Gets a collection of all currently loaded channels.
     * @return All currently loaded channels.
     */
    public Collection<ChatChannel> getLoadedChannels() {
        return loadedChannels;
    }

    /**
     * Remove a player from the player channels map.
     * Used when they log off.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        playerChannels.remove(player.getUniqueId());
    }

    /**
     * Change the channel the player is currently using.
     * @param player Player to change the channel of.
     * @param channel Channel to change them to.
     */
    public void setChannel(Player player, ChatChannel channel) {
        playerChannels.put(player.getUniqueId(), channel.name());
    }
}