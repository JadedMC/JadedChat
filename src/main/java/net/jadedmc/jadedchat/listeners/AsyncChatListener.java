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
package net.jadedmc.jadedchat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.features.channels.Channel;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 * This listens to the AsyncChatEvent event, which is called every time a player send a chat message.
 * We use this to grab the chat message being sent and format it properly.
 */
public class AsyncChatListener implements Listener {
    private final JadedChat plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public AsyncChatListener(final JadedChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event AsyncChatEvent.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncChatEvent event) {
        // Prevent conflicts with other plugins by exiting if the event is canceled.
        if(event.isCancelled()) {
            return;
        }

        // Get the player's current channel
        Player player = event.getPlayer();
        Channel channel = plugin.getChannelManager().getChannel(player);

        // Make sure they are actually in a channel.
        if(channel == null) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You are not currently in a channel!");
            return;
        }

        // Sends the chat message to their channel.
        channel.sendMessage(player, PlainTextComponentSerializer.plainText().serialize(event.message()));
        event.setCancelled(true);
    }
}
