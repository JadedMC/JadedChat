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

import better.reload.api.ReloadEvent;
import net.jadedmc.jadedchat.JadedChatPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

/**
 * Listens to the ReloadEvent, which runs every time the server is "reloaded" using the BetterReload plugin.
 * <a href="https://github.com/amnoah/BetterReload">BetterReload GitHub</a>
 */
public class ReloadListener implements Listener {
    private final JadedChatPlugin plugin;

    /**
     * Creates the listener.
     * @param plugin Instance of the plugin.
     */
    public ReloadListener(@NotNull final JadedChatPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the server is "reloaded".
     * @param event ReloadEvent.
     */
    @EventHandler
    public void onReload(@NotNull final ReloadEvent event) {
        plugin.getConfigManager().reloadConfig();
        plugin.channelManager().loadChannels();
        plugin.emoteManager().registerEmotes();
    }
}