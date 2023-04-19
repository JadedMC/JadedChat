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
package net.jadedmc.jadedchat.features.channels;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.jadedmc.jadedchat.utils.ConfigUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a format used in a channel.
 */
public class Format {
    private final JadedChat plugin;
    private final List<String> sections = new ArrayList<>();
    private final MiniMessage miniMessage;

    private final boolean color;
    private final boolean decorations;
    private final boolean events;

    /**
     * Creates the Format object.
     * @param section Configuration Section of the format.
     */
    public Format(JadedChat plugin, ConfigurationSection section) {
        this.plugin = plugin;

        // Load format settings
        color = ConfigUtils.getBoolean(section, "settings.color", false);
        decorations = ConfigUtils.getBoolean(section, "settings.decorations", false);
        events = ConfigUtils.getBoolean(section, "settings.events", false);

        // Load the sections for the format.
        ConfigurationSection formatSections = section.getConfigurationSection("segments");
        if(formatSections != null) {
            for(String sectionName : formatSections.getKeys(false)) {
                sections.add(formatSections.getString(sectionName));
            }
        }

        TagResolver.Builder tagsResolverBuilder = TagResolver.builder();
        if(color) {
            tagsResolverBuilder.resolver(StandardTags.color())
                    .resolver(StandardTags.rainbow())
                    .resolver(StandardTags.gradient());
        }
        if(decorations) {
            tagsResolverBuilder.resolver(StandardTags.decorations())
                    .resolver(StandardTags.font());
        }
        if(events) {
            tagsResolverBuilder.resolver(StandardTags.clickEvent())
                    .resolver(StandardTags.hoverEvent())
                    .resolver(StandardTags.insertion())
                    .resolver(StandardTags.selector());
        }

        miniMessage = MiniMessage.builder().tags(tagsResolverBuilder.build()).build();
    }

    /**
     * Processes a message sent by a player using the format.
     * @param player Player sending the message.
     * @param message The message sent.
     * @return Component of the message applied to the format.
     */
    public Component processMessage(Player player, String message) {

        // Replace legacy chat colors.
        if(color || decorations) {
            message = ChatUtils.replaceLegacy(message);
        }

        TextComponent.Builder component = Component.text();
        TextComponent.Builder itemComponent = Component.text();

        // Enables displaying the held item in chat if the player has permission.
        if(player.hasPermission("jadedchat.showitem")) {
            if(player.getInventory().getItemInHand().getType() != Material.AIR) {
                ItemStack itemStack = player.getInventory().getItemInHand();

                String nbtString = "";
                if(itemStack.getItemMeta() != null) {
                    nbtString += itemStack.getItemMeta().getAsString();
                }

                String miniMessageString = "<hover:show_item:" + itemStack.getType().toString().toLowerCase() + ":" + itemStack.getAmount() + ": '" + nbtString + "'>";
                miniMessageString += "<name></hover>";
                itemComponent.append(MiniMessage.miniMessage().deserialize(miniMessageString, Placeholder.component("name", itemStack.displayName())));
            }
            else {
                itemComponent.append(ChatUtils.translate("<hover:show_text:Air><white>[Air]</white></hover>"));
            }
        }
        else {
            itemComponent.content("<item>");
        }

        // Loop through each section of the format.
        for(String section : sections) {
            // Makes sure we don't process placeholders sent in the chat message.
            if(section.contains("<message>")) {
                section = section.replace("<message_raw>", message);
                message = plugin.getEmoteManager().replaceEmotes(message, player);
                component.append(MiniMessage.miniMessage().deserialize(section, Placeholder.component("message", miniMessage.deserialize(message, Placeholder.component("item", itemComponent.build())))));
            }
            else {
                // Processes placeholders for the section.
                component.append(plugin.getEmoteManager().replaceEmotes(MiniMessage.miniMessage().deserialize(PlaceholderAPI.setPlaceholders(player, section))));
            }
        }

        // Returns the final component.
        return component.build();
    }
}