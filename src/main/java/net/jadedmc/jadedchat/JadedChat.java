package net.jadedmc.jadedchat;

import net.jadedmc.jadedchat.channels.ChannelManager;
import net.jadedmc.jadedchat.emotes.EmoteManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class JadedChat extends JavaPlugin {
    private ChannelManager channelManager;
    private EmoteManager emoteManager;
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager();

        channelManager = new ChannelManager();
        emoteManager = new EmoteManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
