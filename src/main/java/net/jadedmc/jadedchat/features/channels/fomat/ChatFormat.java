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
package net.jadedmc.jadedchat.features.channels.fomat;

import me.clip.placeholderapi.PlaceholderAPI;
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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;

/**
 * Represents a format used in a channel.
 */
public class ChatFormat {
    private final HashMap<String, String> sections = new LinkedHashMap<>();
    private MiniMessage miniMessage;
    private final String id;

    // MiniMessage settings
    private boolean all = false;
    private boolean color = false;
    private boolean decorations = false;
    private boolean events = false;

    /**
     * Creates the ChatFormat.
     * @param id Id of the chat format.
     */
    public ChatFormat(String id) {
        this.id = id;
        updateMiniMessage();
    }

    /**
     * Add a section to the chat format.
     * @param section Section to add.
     */
    public void addSection(String id, String section) {
        sections.put(id, section);
    }

    /**
     * Get if the format should be able to use all MiniMessage tags.
     * Note: This overrides all other tag settings.
     * @return Whether all tags should be usable.
     */
    public boolean allTags() {
        return all;
    }

    /**
     * Get of the format should be able to use all MiniMessage tags.
     * Note: This will override all other tag settings.
     * @param all Whether all tags should be usable.
     */
    public void allTags(boolean all) {
        this.all = all;
    }

    /**
     * Get if the chat format should be able to use colors.
     * @return Whether colors should be usable.
     */
    public boolean color() {
        return color;
    }

    /**
     * Set if the chat format should be able to use colors.
     * @param color Whether colors should be usable.
     */
    public void color(boolean color) {
        this.color = color;
        updateMiniMessage();
    }

    /**
     * Get if the chat format should be able to use decorations.
     * @return Whether decorations should be usable.
     */
    public boolean decorations() {
        return decorations;
    }

    /**
     * Set if the chat format should be able to use decorations.
     * @param decorations Whether decorations should be usable.
     */
    public void decorations(boolean decorations) {
        this.decorations = decorations;
        updateMiniMessage();
    }

    /**
     * Get if the chat format should be able to use events.
     * @return Whether events should be usable.
     */
    public boolean events() {
        return events;
    }

    /**
     * Set if the chat format should be able to use events.
     * @param events Whether events should be usable.
     */
    public void events(boolean events) {
        this.events = events;
        updateMiniMessage();
    }

    /**
     * Gets the id of the ChatFormat.
     * @return ChatFormat id.
     */
    public String id() {
        return id;
    }

    /**
     * Get the MiniMessage object of the format.
     * @return MiniMessage object.
     */
    public MiniMessage miniMessage() {
        return miniMessage;
    }

    /**
     * Get the sections of the format.
     * @return All sections.
     */
    public HashMap<String, String> sections() {
        return sections;
    }

    /**
     * Processes a message sent by a player using the format.
     * @param player Player sending the message.
     * @param message The message sent.
     * @return Component of the message applied to the format.
     */
    public Component processMessage(JadedChatPlugin plugin, Player player, String message) {

        // Replace legacy chat colors.
        if(color || decorations || all) {
            message = ChatUtils.replaceLegacy(message);
        }

        TextComponent.Builder component = Component.text();
        TextComponent.Builder itemComponent = Component.text();

        // Enables displaying the held item in chat if the player has permission.
        if(player.hasPermission("jadedchat.showitem") && JadedChat.isPaper()) {
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
        for(String sectionID : sections.keySet()) {
            String section = sections.get(sectionID);

            // Makes sure we don't process placeholders sent in the chat message.
            if(section.contains("<message>")) {
                section = section.replace("<message_raw>", message);
                message = plugin.emoteManager().replaceEmotes(message, player);
                component.append(MiniMessage.miniMessage().deserialize(section, Placeholder.component("message", miniMessage.deserialize(message, Placeholder.component("item", itemComponent.build())))));
            }
            else {
                // Processes placeholders for the section.
                component.append(plugin.emoteManager().replaceEmotes(MiniMessage.miniMessage().deserialize(ChatUtils.replaceLegacy(PlaceholderAPI.setPlaceholders(player, section)), Placeholder.parsed("server", Objects.requireNonNull(plugin.settingsManager().getConfig().getString("server"))))));
            }
        }

        // Returns the final component.
        return component.build();
    }

    /**
     * Updates the format MiniMessage object.
     */
    public void updateMiniMessage() {
        if(all) {
            miniMessage = MiniMessage.miniMessage();
            return;
        }

        TagResolver.Builder tagsResolverBuilder = TagResolver.builder();

        if(color) {
            tagsResolverBuilder.resolver(StandardTags.color())
                    .resolver(StandardTags.rainbow())
                    .resolver(StandardTags.gradient());
        }
        if(decorations) {
            tagsResolverBuilder.resolver(StandardTags.decorations())
                    .resolver(StandardTags.font())
                    .resolver(StandardTags.reset());
        }
        if(events) {
            tagsResolverBuilder.resolver(StandardTags.clickEvent())
                    .resolver(StandardTags.hoverEvent())
                    .resolver(StandardTags.insertion())
                    .resolver(StandardTags.selector())
                    .resolver(StandardTags.translatable())
                    .resolver(StandardTags.newline());
        }

        miniMessage = MiniMessage.builder().tags(tagsResolverBuilder.build()).build();
    }
}