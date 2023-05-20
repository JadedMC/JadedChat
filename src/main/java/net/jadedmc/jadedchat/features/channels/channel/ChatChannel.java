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
package net.jadedmc.jadedchat.features.channels.channel;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import github.scarsz.discordsrv.DiscordSRV;
import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.features.channels.events.ChannelBungeeSendEvent;
import net.jadedmc.jadedchat.features.channels.events.ChannelMessageSendEvent;
import net.jadedmc.jadedchat.features.channels.fomat.ChatFormat;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Represents a section of chat separated from others.
 */
public class ChatChannel {
    private final String name;
    private String displayName;
    private final Map<String, ChatFormat> chatFormats = new LinkedHashMap<>();
    private final Collection<String> aliases = new ArrayList<>();
    private String permission = "";
    private boolean isDefaultChannel = false;
    private boolean useBungeecord = false;
    private boolean useDiscordSRV = false;
    private int chatRange = -1;

    /**
     * Creates the ChatChannel.
     * @param name Name of the channel.
     */
    public ChatChannel(String name) {
        this.name = name;
        displayName = name;
    }

    /**
     * Adds an alias to the channel.
     * Used in /channel.
     * @param alias Alias to add.
     */
    public void addAlias(String alias) {
        aliases.add(alias);
    }

    /**
     * Adds a chat format to the channel.
     * @param chatFormat Chat format to add.
     */
    public void addChatFormat(ChatFormat chatFormat) {
        chatFormats.put(chatFormat.id(), chatFormat);
    }

    /**
     * Get the aliases of the channel.
     * Used in /channel.
     * @return List of all channel aliases.
     */
    public Collection<String> aliases() {
        return aliases;
    }

    /**
     * Get how many blocks a message should "travel".
     * @return Range of the channel.
     */
    public int chatRange() {
        return chatRange;
    }

    /**
     * Set how far chat messages should "travel" in a world.
     * Set for messages to be global.
     * Defaults to -1.
     * @param chatRange How far (in blocks) a message should "travel".
     */
    public void chatRange(int chatRange) {
        this.chatRange = chatRange;
    }

    /**
     * Set the display name of the channel.
     * Appears in /channel.
     * @return Display name of the channel.
     */
    public String displayName() {
        return displayName;
    }

    /**
     * Set the display name of the channel.
     * Appears in /channel.
     * @param displayName Display name of the channel.
     */
    public void displayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Get a format based off its id.
     * @param format ID of the format.
     * @return Associated format.
     */
    public ChatFormat format(String format) {
        return chatFormats.get(format);
    }

    /**
     * Get the format a player should be using when chatting in the channel.
     * @param player Player to get the format of.
     * @return Proper format.
     */
    public ChatFormat format(Player player) {
        // Sets the default chat format to "default" in case no permissions found.
        String format = "default";

        // Looks for a format matching the player's group if LuckPerms is enabled.
        if(JadedChat.useLuckPerms()) {
            // Get the player's current primary group.
            LuckPerms luckperms = LuckPermsProvider.get();
            String group = luckperms.getUserManager().getUser(player.getUniqueId()).getPrimaryGroup();

            // Check if the group has a format.
            if(chatFormats.containsKey(group)) {
                return format(group);
            }
        }

        // Loops through all the chat formats to find the highest format allowed.
        for(String str : chatFormats.keySet()) {
            if(player.hasPermission("format." + str)) {
                format = str;
                break;
            }
        }

        return format(format);
    }

    /**
     * Get if the channel should be the default channel.
     * @return Whether the channel should be the default channel.
     */
    public boolean isDefaultChannel() {
        return isDefaultChannel;
    }

    /**
     * Set whether the channel is the default channel.
     * Defaults to false.
     * @param isDefaultChannel If the channel should be the default channel.
     */
    public void isDefaultChannel(boolean isDefaultChannel) {
        this.isDefaultChannel = isDefaultChannel;
    }

    /**
     * Get the name of the channel.
     * This acts as the id of the channel.
     * @return Name of the channel.
     */
    public String name() {
        return name;
    }

    /**
     * Get the required permission node to use the channel.
     * @return Required permission node.
     */
    public String permission() {
        return permission;
    }

    /**
     * Set the permission node required to use the channel.
     * Defaults to an empty string.
     * @param permission Required permission node.
     */
    public void permission(String permission) {
        this.permission = permission;
    }

    public void saveToFile(String filePath) {
        try {
            File file = new File(JadedChat.getDataFolder(), "/channels/" + filePath);
            if(file.exists()) {
                file.delete();
            }

            file.createNewFile();

            FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
            configuration.set("name", name);
            configuration.set("displayName", displayName);
            configuration.set("aliases", aliases);
            configuration.set("permission", permission);

            configuration.set("settings.default", isDefaultChannel);
            configuration.set("settings.bungeecord", useBungeecord);
            configuration.set("settings.DiscordSRV", useDiscordSRV);
            configuration.set("settings.range", chatRange);

            for(String chatFormatID : chatFormats.keySet()) {
                ChatFormat chatFormat = chatFormats.get(chatFormatID);

                configuration.set("formats." + chatFormatID + ".settings.color", chatFormat.color());
                configuration.set("formats." + chatFormatID + ".settings.decorations", chatFormat.decorations());
                configuration.set("formats." + chatFormatID + ".settings.events", chatFormat.events());

                for(String sectionID : chatFormat.sections().keySet()) {
                    String section = chatFormat.sections().get(sectionID);
                    configuration.set("formats." + chatFormatID + ".segments." + sectionID, section);
                }
            }

            configuration.save(file);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Sends a message to the channel.
     * @param player Player who sent the message.
     * @param message Message the player is sending.
     */
    public void sendMessage(JadedChatPlugin plugin, Player player, String message) {
        // Checks if the message passes the chat filter.
        if(!plugin.filterManager().passesFilter(player, this, message)) {
            return;
        }

        ChannelMessageSendEvent messageEvent = new ChannelMessageSendEvent(player, this, message);
        Bukkit.getPluginManager().callEvent(messageEvent);

        // Exit if the message sent event is cancelled.
        if(messageEvent.isCancelled()) {
            return;
        }

        // Creates the formatted component of the message.
        Component messageComponent = format(player).processMessage(plugin, player, message);

        // Send the message to all channel viewers.
        viewers(player).forEach(viewer -> ChatUtils.chat(viewer, messageComponent));

        // Send the message to the console as well
        ChatUtils.chat(Bukkit.getConsoleSender(), Component.text().content("[" + name + "] ").append(messageComponent).build());

        // Sends the message through DiscordSRV if enabled.
        if(plugin.hookManager().useDiscordSRV() && useDiscordSRV) {
            DiscordSRV.getPlugin().getMainTextChannel().sendMessage(MiniMessage.miniMessage().stripTags(MiniMessage.miniMessage().serialize(messageComponent)));
        }

        // Sends the message through bungeecord if bungeecord is enabled for the channel.
        if(useBungeecord) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
                ChannelBungeeSendEvent bungeeEvent = new ChannelBungeeSendEvent(this, player, message);
                Bukkit.getPluginManager().callEvent(bungeeEvent);

                if(bungeeEvent.isCancelled()) {
                    return;
                }

                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("Forward");
                out.writeUTF("ALL");
                out.writeUTF("jadedchat");
                out.writeUTF(name.toLowerCase() + "~~" + bungeeEvent.getData() + "~~" + MiniMessage.miniMessage().serialize(messageComponent));

                // Sends the message to bungeecord, to send back to all online servers.
                player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            }, 0);
        }
    }

    /**
     * Sends a message to the channel without a Player object.
     * Used when processing bungeecord messages.
     * @param message Full message being sent.
     */
    public void sendMessage(Collection<Player> viewers, String message) {
        // Creates the formatted component of the message.
        Component component = MiniMessage.miniMessage().deserialize(message);

        // Send the message to all channel viewers.
        viewers.forEach(viewer -> ChatUtils.chat(viewer, component));

        // Send the message to the console as well
        ChatUtils.chat(Bukkit.getConsoleSender(), Component.text().content("(Bungee) [" + name + "] ").append(component).build());
    }

    /**
     * Get if the channel should broadcast to Bungeecord.
     * @return Whether the channel uses Bungeecord.
     */
    public boolean useBungeecord() {
        return useBungeecord;
    }

    /**
     * Set if the channel should broadcast to Bungeecord.
     * Defaults to false.
     * @param useBungeecord Whether the channel should use Bungeecord.
     */
    public void useBungeecord(boolean useBungeecord) {
        this.useBungeecord = useBungeecord;
    }

    /**
     * Get if the channel should broadcast to DiscordSRV.
     * @return Whether the channel uses DiscordSRV.
     */
    public boolean useDiscordSRV() {
        return useDiscordSRV;
    }

    /**
     * Set if the channel should broadcast to DiscordSRV.
     * Defaults to false.
     * @param useDiscordSRV Whether the channel should use DiscordSRV.
     */
    public void useDiscordSRV(boolean useDiscordSRV) {
        this.useDiscordSRV = useDiscordSRV;
    }

    /**
     * Gets a collection of all players who should be able to view a message sent by a player in the channel.
     * @param sender Player who sent the message. Can be null.
     * @return All players who can see that message.
     */
    public Collection<Player> viewers(Player sender) {
        Collection<Player> hasPermission = new ArrayList<>();

        // If the permission is empty, return all online players.
        if(permission.equalsIgnoreCase("")) {
            hasPermission.addAll(Bukkit.getOnlinePlayers());
        }
        else {
            // Checks each player to see if they have the proper permission.
            for(Player player : Bukkit.getOnlinePlayers()) {
                // If so, add them as a viewer.
                if(player.hasPermission(permission)) {
                    hasPermission.add(player);
                }
            }
        }


        Collection<Player> inRange = new ArrayList<>();

        // If the range is less than one, add all players with permission.
        if(chatRange < 1) {
            inRange.addAll(hasPermission);
        }
        else {
            // If not, loop through all players with permission and check if they are in range.
            for(Player player : hasPermission) {
                if(player.getLocation().distance(sender.getLocation()) <= chatRange) {
                    inRange.add(player);
                }
            }
        }

        return inRange;
    }
}