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

import net.jadedmc.jadedchat.JadedChat;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Manages the creation and use of chat channels.
 */
public class ChannelManager {
    private final Map<String, Channel> allChannels = new HashMap<>();
    private final Map<UUID, String> playerChannels = new HashMap<>();
    private Channel defaultChannel;

    /**
     * Creates the Channel Manager.
     * @param plugin Instance of the plugin.
     */
    public ChannelManager(JadedChat plugin) {
        File[] formatFiles = new File(plugin.getDataFolder(), "channels").listFiles();
        if(formatFiles == null) {
            return;
        }

        // Loop through each file an load it's channel.
        for(File file : formatFiles) {
            Channel channel = new Channel(plugin, file);
            allChannels.put(channel.getName(), channel);

            for(String alias : channel.getAliases()) {
                allChannels.put(alias, channel);
            }

            if(channel.isDefaultChannel()) {
                defaultChannel = channel;
            }
        }
    }

    /**
     * Get a channel based off its name or aliases.
     * @param name Name (or alias) of the channel.
     * @return Corresponding channel.
     */
    public Channel getChannel(String name) {
        name = name.toUpperCase();

        if(!allChannels.containsKey(name)) {
            return null;
        }

        return allChannels.get(name);
    }

    /**
     * Get the channel a player is currently in.
     * @param player Playet to get the channel of.
     * @return The channel they are currently using. Returns the default channel if not saved.
     */
    public Channel getChannel(Player player) {
        if(playerChannels.containsKey(player.getUniqueId())) {
            return getChannel(playerChannels.get(player.getUniqueId()));
        }

        return defaultChannel;
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
    public void setChannel(Player player, Channel channel) {
        playerChannels.put(player.getUniqueId(), channel.getName());
    }
}