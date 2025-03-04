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
import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This listens to the PlayerJoinEvent event, which is called every time a player joins the server.
 * We use this to modify the format of join messages.
 */
public class PlayerJoinListener implements Listener {
    private final JadedChatPlugin plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public PlayerJoinListener(JadedChatPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * Runs at the highest priority
     * @param event PlayerJoinEvent.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        // If this is the player's first time playing, displays the first join message if enabled.
        if(!event.getPlayer().hasPlayedBefore() && plugin.getConfigManager().getConfig().isSet("FirstJoinMessage.enabled") && plugin.getConfigManager().getConfig().getBoolean("FirstJoinMessage.enabled")) {
            Component joinMessage = ChatUtils.translateWithPlaceholders(plugin.getConfigManager().getConfig().getString("FirstJoinMessage.message"), event.getPlayer());
            Bukkit.spigot().broadcast(ChatUtils.translateToBaseComponent(joinMessage));
        }

        // We only want to modify the join message if the plugin is configured to.
        if(!plugin.getConfigManager().getConfig().getBoolean("JoinMessage.override")) {
            return;
        }

        // Makes sure there is no join message if that is what the plugin is configured for.
        if(!plugin.getConfigManager().getConfig().isSet("JoinMessage.message")) {
            event.setJoinMessage(null);
            return;
        }

        // Prevents conflict with over plugins by not overwriting join messages when they are null.
        if(event.getJoinMessage() == null) {
            return;
        }

        // Grabs the configured join message.
        String messageString = plugin.getConfigManager().getConfig().getString("JoinMessage.message");

        // Another check to make sure there is no join message if that is what is set up.
        if(messageString == null || messageString.equals("null")) {
            event.setJoinMessage(null);
            return;
        }

        // Formats the join message and applies it.
        messageString = PlaceholderAPI.setPlaceholders(event.getPlayer(), messageString);
        messageString = plugin.emoteManager().replaceEmotes(messageString);
        Component messageComponent = MiniMessage.miniMessage().deserialize(messageString);
        event.setJoinMessage(null);
        Bukkit.spigot().broadcast(ChatUtils.translateToBaseComponent(messageComponent));
    }
}