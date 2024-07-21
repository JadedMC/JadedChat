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
package net.jadedmc.jadedchat.features.messaging;

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * This class manages all active conversations. This allows /reply to work.
 */
public class MessageManager {
    private final JadedChatPlugin plugin;
    private final Map<Player, Player> conversations = new HashMap<>();
    private final Set<Player> spying = new HashSet<>();

    public MessageManager(JadedChatPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get all players with social spy enabled.
     * @return Players using social spy.
     */
    public Set<Player> getSpying() {
        return spying;
    }

    /**
     * Get if a player is spying on private messages.
     * @param player Player to check.
     * @return Whether they are spying on private messages.
     */
    public boolean isSpying(Player player) {
        return spying.contains(player);
    }

    /**
     * Makes it so that /r will message between 2 players..
     * @param messenger Messenger.
     * @param receiver Receiver.
     */
    public void setReplyTarget(Player messenger, Player receiver){
        conversations.put(messenger, receiver);
        conversations.put(receiver, messenger);
    }

    /**
     * Get who /r should message.
     * @param messenger Player who is replying.
     * @return Who to reply to.
     */
    public Player getReplyTarget(Player messenger){
        return conversations.get(messenger);
    }

    /**
     * Processes a private message and displays it to everyone.
     * @param sender Player who is sending the message.
     * @param receiver Player who is recieveing the message.
     * @param message The message being sent.
     */
    public void processMessage(Player sender, Player receiver, String message) {
        FileConfiguration configuration = plugin.settingsManager().getConfig();
        Component toSender = generateComponent(configuration.getConfigurationSection("PrivateMessages.SenderMessage.segments"), sender, receiver, message);
        Component toReceiver = generateComponent(configuration.getConfigurationSection("PrivateMessages.ReceiverMessage.segments"), sender, receiver, message);
        Component toSpy = generateComponent(configuration.getConfigurationSection("PrivateMessages.SpyMessage.segments"), sender, receiver, message);

        ChatUtils.chat(sender, toSender);
        {
            if(configuration.isSet("PrivateMessages.SenderMessage.sounds")) {
                List<String> sounds = configuration.getStringList("PrivateMessages.SenderMessage.sounds");

                for(String soundID : sounds) {
                    Sound sound = Sound.valueOf(soundID);
                    sender.playSound(sender.getLocation(), sound, 0.5F, 1F);
                }
            }
        }

        ChatUtils.chat(receiver, toReceiver);
        {
            if(configuration.isSet("PrivateMessages.ReceiverMessage.sounds")) {
                List<String> sounds = configuration.getStringList("PrivateMessages.ReceiverMessage.sounds");

                for(String soundID : sounds) {
                    Sound sound = Sound.valueOf(soundID);
                    receiver.playSound(receiver.getLocation(), sound, 0.5F, 1F);
                }
            }
        }

        for(Player stalker : getSpying()) {

            // Skips the "stalker" if they are part of the message to prevent double-sending.
            if(stalker.equals(sender) || stalker.equals(receiver)) {
                continue;
            }

            ChatUtils.chat(stalker, toSpy);

            {
                if(configuration.isSet("PrivateMessages.SpyMessage.sounds")) {
                    List<String> sounds = configuration.getStringList("PrivateMessages.SpyMessage.sounds");

                    for(String soundID : sounds) {
                        Sound sound = Sound.valueOf(soundID);
                        stalker.playSound(stalker.getLocation(), sound, 0.5F, 1F);
                    }
                }
            }
        }

        // Creates a conversation between the two players so /reply works.
        setReplyTarget(sender, receiver);
    }

    /**
     * Remove a player from the conversations maps.
     * This is done when they leave the server.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        // Removes the player.
        conversations.remove(player);

        // Loops through all the other conversations.
        for(Player other : conversations.keySet()) {
            // Checks if the player is the value of another player.
            if(conversations.get(other).equals(player)) {
                // If they are, removes that conversation too.
                conversations.remove(other);
            }
        }
    }

    /**
     * Toggle social spy for a player.
     * @param player Player to toggle social spy for.
     */
    public void toggleSocialSpy(Player player) {
        if(isSpying(player)) {
            spying.remove(player);
        }
        else {
            spying.add(player);
        }
    }

    /**
     * Generates the Component of a private message.
     * @param config Configuration Section of the private message config.
     * @param sender The player who is sending the message.
     * @param receiver The player who is receiving the message.
     * @param message The message being sent.
     * @return The component form of the message.
     */
    private Component generateComponent(ConfigurationSection config, Player sender, Player receiver, String message) {
        if(config == null) {
            return null;
        }

        // Checks which tags we should process in a sender's message.
        TagResolver.Builder tagsResolverBuilder = TagResolver.builder();
        if(sender.hasPermission("jadedchat.message.colors")) {
            tagsResolverBuilder.resolver(StandardTags.color())
                    .resolver(StandardTags.rainbow())
                    .resolver(StandardTags.gradient());
        }
        if(sender.hasPermission("jadedchat.message.decorations")) {
            tagsResolverBuilder.resolver(StandardTags.decorations())
                    .resolver(StandardTags.font());
        }
        if(sender.hasPermission("jadedchat.message.events")) {
            tagsResolverBuilder.resolver(StandardTags.clickEvent())
                    .resolver(StandardTags.hoverEvent())
                    .resolver(StandardTags.insertion())
                    .resolver(StandardTags.selector());
        }
        MiniMessage miniMessage = MiniMessage.builder().tags(tagsResolverBuilder.build()).build();

        // Creates the component.
        TextComponent.Builder output = Component.text();

        for(String section : config.getKeys(false)) {
            String value = config.getString(section);

            if(value == null) {
                continue;
            }

            value = value.replace("<sender>", sender.getName()).replace("<receiver>", receiver.getName());
            value = plugin.emoteManager().replaceEmotes(value);

            message = plugin.emoteManager().replaceEmotes(message, sender);

            Component component = MiniMessage.miniMessage().deserialize(value, Placeholder.component("message", miniMessage.deserialize(message)));
            output.append(component);
        }

        return output.build();
    }
}