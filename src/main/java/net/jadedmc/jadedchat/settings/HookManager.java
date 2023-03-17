package net.jadedmc.jadedchat.settings;

import net.jadedmc.jadedchat.JadedChat;

/**
 * Manages various hooks into other plugins.
 */
public class HookManager {
    private final JadedChat plugin;
    private final boolean hasDiscordSRV;
    private final boolean hasLuckPerms;

    /**
     * Creates the HookManager.
     * @param plugin Instance of the plugin.
     */
    public HookManager(JadedChat plugin) {
        this.plugin = plugin;

        this.hasDiscordSRV = plugin.getServer().getPluginManager().isPluginEnabled("DiscordSRV");
        this.hasLuckPerms = plugin.getServer().getPluginManager().isPluginEnabled("LuckPerms");
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
        if(!plugin.getSettingsManager().getConfig().isSet("Hooks.DiscordSRV")) {
            return true;
        }

        // Otherwise return what the setting is set to.
        return plugin.getSettingsManager().getConfig().getBoolean("Hooks.DiscordSRV");
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
        if(!plugin.getSettingsManager().getConfig().isSet("Hooks.LuckPerms")) {
            return true;
        }

        // Otherwise return what the setting is set to.
        return plugin.getSettingsManager().getConfig().getBoolean("Hooks.LuckPerms");
    }
}