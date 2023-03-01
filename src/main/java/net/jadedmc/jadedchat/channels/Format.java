package net.jadedmc.jadedchat.channels;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.utils.ConfigUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

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
                    .resolver(StandardTags.hoverEvent());
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
        TextComponent.Builder component = Component.text();

        // Loop through each section of the format.
        for(String section : sections) {
            // Makes sure we don't process placeholders sent in the chat message.
            if(section.contains("<message>")) {
                message = plugin.getEmoteManager().replaceEmotes(message, player);
                component.append(MiniMessage.miniMessage().deserialize(section, Placeholder.component("message", miniMessage.deserialize(message))));
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