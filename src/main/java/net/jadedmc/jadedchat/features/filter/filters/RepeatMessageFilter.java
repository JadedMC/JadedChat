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
package net.jadedmc.jadedchat.features.filter.filters;

import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.features.filter.Filter;
import net.jadedmc.jadedchat.settings.Message;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Filters messages that are sent multiple times in a row.
 */
public class RepeatMessageFilter extends Filter {
    private final JadedChatPlugin plugin;
    private final Map<Player, String> lastMessage = new HashMap<>();

    /**
     * Creates the filter.
     * @param plugin Instance of the plugin.
     */
    public RepeatMessageFilter(JadedChatPlugin plugin) {
        this.plugin = plugin;
        setSilentFail(false);
        setFailMessage(plugin.settingsManager().getMessage(Message.FILTER_REPEAT_MESSAGE));
    }

    /**
     * Checks if a message passes the filter.
     * @param player Player who sent the message.
     * @param message Message they sent.
     * @return If the message passes the filter.
     */
    @Override
    public boolean passesFilter(Player player, String message) {
        // Exit if the filter is disabled.
        if(!plugin.settingsManager().getFilter().getBoolean("RepeatMessageFilter.enabled")) {
            return true;
        }

        // Checks if the player has bypass permissions.
        if(player.hasPermission("jadedcore.bypass.repeatfilter")) {
            return true;
        }

        // Checks if the message is the first sent.
        if(lastMessage.get(player) == null) {
            lastMessage.put(player, message);
            return true;
        }

        // Checks if the message is too short or is different enough from the past message.
        if(message.length() <= 5 || (message.length() * 2) <= lastMessage.get(player).length() || (lastMessage.get(player).length() * 2) <= message.length()) {
            lastMessage.put(player, message);
            return true;
        }

        // Checks if the message and last message are similar to each other.
        if(message.contains(lastMessage.get(player)) || lastMessage.get(player).contains(message)) {
            return false;
        }

        // Stores the message.
        lastMessage.put(player, message);
        return true;
    }

    /**
     * Removes a player from the stored messages map.
     * Used when the player disconnects.
     * @param player Player to remove.
     */
    @Override
    public void removePlayer(Player player) {
        lastMessage.remove(player);
    }
}