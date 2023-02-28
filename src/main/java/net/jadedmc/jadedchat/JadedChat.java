package net.jadedmc.jadedchat;

import net.jadedmc.jadedchat.channels.ChannelManager;
import net.jadedmc.jadedchat.emotes.EmoteManager;
import net.jadedmc.jadedchat.listeners.AsyncChatListener;
import net.jadedmc.jadedchat.listeners.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class JadedChat extends JavaPlugin {
    private ChannelManager channelManager;
    private EmoteManager emoteManager;
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);

        channelManager = new ChannelManager(this);
        //emoteManager = new EmoteManager(this);

        Bukkit.getPluginManager().registerEvents(new AsyncChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
