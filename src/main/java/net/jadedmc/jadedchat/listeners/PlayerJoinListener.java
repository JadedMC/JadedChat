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

import me.clip.placeholderapi.PlaceholderAPI;
import net.jadedmc.jadedchat.JadedChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This listens to the PlayerJoinEvent event, which is called every time a player joins the server.
 * We use this to modify the format of join messages.
 */
public class PlayerJoinListener implements Listener {
    private final JadedChat plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public PlayerJoinListener(JadedChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * Runs at the highest priority
     * @param event PlayerJoinEvent.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        // We only want to modify the join message if the plugin is configured to.
        if(!plugin.getSettingsManager().getConfig().getBoolean("JoinMessage.override")) {
            return;
        }

        // Makes sure there is no join message if that is what the plugin is configured for.
        if(!plugin.getSettingsManager().getConfig().isSet("JoinMessage.message")) {
            event.joinMessage(null);
            return;
        }

        // Grabs the configured join message.
        String messageString = plugin.getSettingsManager().getConfig().getString("JoinMessage.message");

        // Another check to make sure there is no join message if that is what is set up.
        if(messageString == null || messageString.equals("null")) {
            event.joinMessage(null);
            return;
        }

        // Formats the join message and applies it.
        messageString = PlaceholderAPI.setPlaceholders(event.getPlayer(), messageString);
        messageString = plugin.getEmoteManager().replaceEmotes(messageString);
        Component messageComponent = MiniMessage.miniMessage().deserialize(messageString);
        event.joinMessage(messageComponent);
    }
}