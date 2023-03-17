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
import net.jadedmc.jadedchat.features.channels.Channel;
import net.jadedmc.jadedchat.features.channels.ChannelManager;
import net.jadedmc.jadedchat.features.emotes.EmoteManager;
import net.jadedmc.jadedchat.features.filter.FilterManager;
import net.jadedmc.jadedchat.listeners.AsyncChatListener;
import net.jadedmc.jadedchat.listeners.PlayerJoinListener;
import net.jadedmc.jadedchat.listeners.PlayerQuitListener;
import net.jadedmc.jadedchat.features.messaging.MessageManager;
import net.jadedmc.jadedchat.settings.HookManager;
import net.jadedmc.jadedchat.settings.SettingsManager;
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
public final class JadedChat extends JavaPlugin implements PluginMessageListener {
    private ChannelManager channelManager;
    private EmoteManager emoteManager;
    private FilterManager filterManager;
    private HookManager hookManager;
    private MessageManager messageManager;
    private SettingsManager settingsManager;

    /**
     * Runs when the server is started.
     */
    @Override
    public void onEnable() {
        // Load configuration files first.
        settingsManager = new SettingsManager(this);

        // Load other aspects later.
        channelManager = new ChannelManager(this);
        emoteManager = new EmoteManager(this);
        filterManager = new FilterManager(this);
        messageManager = new MessageManager(this);
        hookManager = new HookManager(this);

        // Registers the listeners used by the plugin.
        Bukkit.getPluginManager().registerEvents(new AsyncChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);

        // Registers the commands created by the plugin.
        getCommand("jadedchat").setExecutor(new JadedChatCMD(this));
        getCommand("channel").setExecutor(new ChannelCMD(this));
        getCommand("message").setExecutor(new MessageCMD(this));
        getCommand("reply").setExecutor(new ReplyCMD(this));
        getCommand("socialspy").setExecutor(new SocialSpyCMD(this));

        // Enables bStats statistics tracking.
        new Metrics(this, 17832);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
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

            String[] receivedMessage = in.readUTF().split("~~", 2);
            String chatChannel = receivedMessage[0];
            Channel channelObject = channelManager.getChannel(chatChannel);

            if(channelObject == null) {
                return;
            }

            String chatMessage = receivedMessage[1];
            channelObject.sendMessage(chatMessage);
        }
        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Gets the current channel manager instance.
     * @return Channel manager.
     */
    public ChannelManager getChannelManager() {
        return channelManager;
    }

    /**
     * Gets the current emote manager instance.
     * @return Emote manager.
     */
    public EmoteManager getEmoteManager() {
        return emoteManager;
    }

    /**
     * Gets the current filter manager instance.
     * @return Filter manager.
     */
    public FilterManager getFilterManager() {
        return filterManager;
    }

    /**
     * Gets the current hook manager instance.
     * @return Hook manager.
     */
    public HookManager getHookManager() {
        return hookManager;
    }

    /**
     * Gets the current message manager instance.
     * @return Message manager.
     */
    public MessageManager getMessageManager() {
        return messageManager;
    }

    /**
     * Gets the current settings manager instance.
     * @return Settings Manager.
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
