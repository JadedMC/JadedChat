package net.jadedmc.jadedchat;

import net.jadedmc.jadedchat.channels.ChannelManager;
import net.jadedmc.jadedchat.commands.ChannelCMD;
import net.jadedmc.jadedchat.commands.MessageCMD;
import net.jadedmc.jadedchat.commands.ReplyCMD;
import net.jadedmc.jadedchat.emotes.EmoteManager;
import net.jadedmc.jadedchat.listeners.AsyncChatListener;
import net.jadedmc.jadedchat.listeners.PlayerJoinListener;
import net.jadedmc.jadedchat.listeners.PlayerQuitListener;
import net.jadedmc.jadedchat.messaging.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class JadedChat extends JavaPlugin {
    private ChannelManager channelManager;
    private EmoteManager emoteManager;
    private MessageManager messageManager;
    private SettingsManager settingsManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        settingsManager = new SettingsManager(this);

        channelManager = new ChannelManager(this);
        emoteManager = new EmoteManager(this);
        messageManager = new MessageManager(this);

        Bukkit.getPluginManager().registerEvents(new AsyncChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        getCommand("channel").setExecutor(new ChannelCMD(this));
        getCommand("message").setExecutor(new MessageCMD(this));
        getCommand("reply").setExecutor(new ReplyCMD(this));

        new Metrics(this, 17832);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public EmoteManager getEmoteManager() {
        return emoteManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
