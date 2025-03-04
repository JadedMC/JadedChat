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
package net.jadedmc.jadedchat;

import net.jadedmc.jadedchat.commands.*;
import net.jadedmc.jadedchat.features.channels.ChatChannelManager;
import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;
import net.jadedmc.jadedchat.features.channels.events.ChannelBungeeReceiveEvent;
import net.jadedmc.jadedchat.features.emotes.EmoteManager;
import net.jadedmc.jadedchat.features.filter.FilterManager;
import net.jadedmc.jadedchat.features.messaging.MessageManager;
import net.jadedmc.jadedchat.listeners.AsyncPlayerChatListener;
import net.jadedmc.jadedchat.listeners.PlayerJoinListener;
import net.jadedmc.jadedchat.listeners.PlayerQuitListener;
import net.jadedmc.jadedchat.listeners.ReloadListener;
import net.jadedmc.jadedchat.settings.HookManager;
import net.jadedmc.jadedchat.settings.ConfigManager;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

/**
 * This class is the main class of the plugin.
 * It links all parts together and registers them with the server.
 */
public class JadedChatPlugin extends JavaPlugin implements PluginMessageListener {
    private EmoteManager emoteManager;
    private ConfigManager configManager;
    private ChatChannelManager channelManager;
    private HookManager hookManager;
    private FilterManager filterManager;
    private MessageManager messageManager;
    private MySQL mySQL;

    @Override
    public void onEnable() {
        ChatUtils.initialize(this);
        JadedChat.setPlugin(this);

        // Load configuration files first.
        configManager = new ConfigManager(this);

        // Load other aspects later.
        channelManager = new ChatChannelManager(this);
        emoteManager = new EmoteManager(this);
        hookManager = new HookManager(this);
        filterManager = new FilterManager(this);
        messageManager = new MessageManager(this);
        mySQL= new MySQL(this);

        if(mySQL.isEnabled()) {
            mySQL.openConnection();
        }

        // Registers the listeners used by the plugin.
        Bukkit.getPluginManager().registerEvents(new AsyncPlayerChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        // Supports BetterReload if installed.
        if(this.hookManager.useBetterReload()) getServer().getPluginManager().registerEvents(new ReloadListener(this), this);

        // Registers the commands created by the plugin.
        getCommand("jadedchat").setExecutor(new JadedChatCMD(this));
        getCommand("channel").setExecutor(new ChannelCMD(this));
        getCommand("message").setExecutor(new MessageCMD(this));
        getCommand("reply").setExecutor(new ReplyCMD(this));
        getCommand("socialspy").setExecutor(new SocialSpyCMD(this));

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);

        // Enables bStats statistics tracking.
        new Metrics(this, 17832);
    }

    @Override
    public void onDisable() {
        ChatUtils.disable();
    }

    @Override
    public void onPluginMessageReceived(String channel, @NotNull Player player, byte[] bytes) {
        if (!channel.equalsIgnoreCase( "BungeeCord")) {
            return;
        }

        try {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
            String subChannel = in.readUTF();

            if(!subChannel.equalsIgnoreCase("jadedchat")) {
                return;
            }

            String[] receivedMessage = in.readUTF().split("~~", 4);
            long timeStamp = Long.parseLong(receivedMessage[0]);
            String chatChannel = receivedMessage[1];
            String data = receivedMessage[2];
            String chatMessage = receivedMessage[3];

            // Skip message if it was sent more than a second ago.
            if(System.currentTimeMillis() - timeStamp > 1000) {
                return;
            }

            ChatChannel channelObject = channelManager.getChannel(chatChannel);

            if(channelObject == null) {
                return;
            }

            ChannelBungeeReceiveEvent event = new ChannelBungeeReceiveEvent(channelObject, data, chatMessage);
            Bukkit.getPluginManager().callEvent(event);

            if(event.isCancelled()) {
                return;
            }

            channelObject.sendMessage(event.getViewers(), chatMessage);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Gets the current channel manager instance.
     * @return Channel manager.
     */
    public ChatChannelManager channelManager() {
        return channelManager;
    }

    /**
     * Gets the current emote manager instance.
     * @return Emote manager.
     */
    public EmoteManager emoteManager() {
        return emoteManager;
    }

    /**
     * Gets the current filter manager instance.
     * @return Filter manager.
     */
    public FilterManager filterManager() {
        return filterManager;
    }

    /**
     * Gets the current hook manager instance.
     * @return Hook manager.
     */
    public HookManager hookManager() {
        return hookManager;
    }

    /**
     * Gets the current message manager instance.
     * @return Message manager.
     */
    public MessageManager messageManager() {
        return messageManager;
    }

    /**
     * Gets the active mysql settings.
     * @return MySQL settings.
     */
    public MySQL getMySQL() {
        return mySQL;
    }

    /**
     * Gets the current config manager instance.
     * @return Config Manager.
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
}