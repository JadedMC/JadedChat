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
package net.jadedmc.jadedchat.features.filter;

import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;
import net.jadedmc.jadedchat.features.filter.filters.RegexFilter;
import net.jadedmc.jadedchat.features.filter.filters.RepeatMessageFilter;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Manages filter objects.
 */
public class FilterManager {
    private final JadedChatPlugin plugin;
    private final List<Filter> filters = new ArrayList<>();

    /**
     * Creates the filter manager.
     * @param plugin Instance of the plugin.
     */
    public FilterManager(JadedChatPlugin plugin) {
        this.plugin = plugin;

        filters.add(new RegexFilter(plugin));
        filters.add(new RepeatMessageFilter(plugin));
    }

    /**
     * Check if a message passes the chat filters.
     * @param player Player who sent the message.
     * @param channel Channel the message was being sent in.
     * @param message Message to check.
     * @return Whether the message passes the filter.
     */
    public boolean passesFilter(Player player, ChatChannel channel, String message) {
        boolean passes = true;

        // Loops through each loaded filter.
        for(Filter filter: filters) {
            // Checks if the message passes the filter.
            if(!filter.passesFilter(player, message)) {
                passes = false;

                // If the fail is not silent, displays the fail message.
                if(filter.showFailMessage()) {
                    ChatUtils.chat(player, filter.getFailMessage());
                    return false;
                }
            }
        }

        // Runs if the message silently fails the filter.
        if(!passes) {
            // Sends the player their own message.
            // Checks for permissions to prevent sending the player the same message twice.
            Component playerMessage = channel.format(player).processMessage(plugin, player, message);
            if(!player.hasPermission("jadedchat.filter.view")) {
                ChatUtils.chat(player, playerMessage);
            }

            // Sends staff the filtered message.
            Component staffMessage = MiniMessage.miniMessage().deserialize(Objects.requireNonNull(plugin.settingsManager().getFilter().getString("FilteredPrefix"))).append(playerMessage);
            for(Player viewer : Bukkit.getOnlinePlayers()) {
                if(viewer.hasPermission("jadedchat.filter.view")) {
                    ChatUtils.chat(viewer, staffMessage);
                }
            }

            // Send the message to the console as well
            ChatUtils.chat(Bukkit.getConsoleSender(), Component.text().content("(filtered) [" + channel.name() + "] ").append(playerMessage).build());
        }

        return passes;
    }

    /**
     * Removes a player from all filters.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        // Loops through each filter to remove the player.
        for(Filter filter: filters) {
            filter.removePlayer(player);
        }
    }
}