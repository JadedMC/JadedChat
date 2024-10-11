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
package net.jadedmc.jadedchat.settings;

import net.jadedmc.jadedchat.JadedChatPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Manages various hooks into other plugins.
 */
public class HookManager {
    private final JadedChatPlugin plugin;
    private final boolean hasBetterReload;
    private final boolean hasDiscordSRV;
    private final boolean hasLuckPerms;

    /**
     * Creates the HookManager.
     * @param plugin Instance of the plugin.
     */
    public HookManager(@NotNull final JadedChatPlugin plugin) {
        this.plugin = plugin;

        this.hasDiscordSRV = plugin.getServer().getPluginManager().isPluginEnabled("DiscordSRV");
        this.hasLuckPerms = plugin.getServer().getPluginManager().isPluginEnabled("LuckPerms");

        this.hasBetterReload = plugin.getServer().getPluginManager().isPluginEnabled("BetterReload");
        if(this.hasBetterReload) plugin.getLogger().info("BetterReload detected. Enabling hook...");
    }

    /**
     * Get if the plugin should use BetterReload.
     * @return Whether the plugin should interface with BetterReload.
     */
    public boolean useBetterReload() {
        return this.hasBetterReload;
    }

    /**
     * Get whether the plugin should use DiscordSRV.
     * @return If the plugin should use DiscordSRV.
     */
    public boolean useDiscordSRV() {
        // return false if the server does not have DiscordSRV installed.
        if(!hasDiscordSRV) {
            return false;
        }

        // Return true if the DiscordSRV setting is not set.
        if(!plugin.settingsManager().getConfig().isSet("Hooks.DiscordSRV")) {
            return true;
        }

        // Otherwise return what the setting is set to.
        return plugin.settingsManager().getConfig().getBoolean("Hooks.DiscordSRV");
    }

    /**
     * Get whether the plugin should use LuckPerms.
     * @return If the plugin should use LuckPerms.
     */
    public boolean useLuckPerms() {
        // return false if the server does not have LuckPerms installed.
        if(!hasLuckPerms) {
            return false;
        }

        // Return true if the LuckPerms setting is not set.
        if(!plugin.settingsManager().getConfig().isSet("Hooks.LuckPerms")) {
            return true;
        }

        // Otherwise return what the setting is set to.
        return plugin.settingsManager().getConfig().getBoolean("Hooks.LuckPerms");
    }
}