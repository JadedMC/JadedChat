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
package net.jadedmc.jadedchat.features.emotes;

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.settings.SettingsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads emotes from the configuration file
 * and stores them for future use.
 */
public class EmoteManager {
    private final JadedChat plugin;
    private final List<Emote> emotes = new ArrayList<>();
    private boolean enableEmotes;

    /**
     * Creates the emote manager.
     * @param plugin Instance of the plugin.
     */
    public EmoteManager(JadedChat plugin) {
        this.plugin = plugin;
        registerEmotes();
    }

    /**
     * Get the list of emotes.
     * @return Emotes.
     */
    public List<Emote> getEmotes() {
        return emotes;
    }

    /**
     * Loops through config to create emote objects.
     */
    public void registerEmotes() {
        emotes.clear();

        SettingsManager settings = plugin.getSettingsManager();

        // Set if emotes should be enabled.
        if(!settings.getEmotes().isSet("enable")) {
            enableEmotes = true;
        }
        enableEmotes = settings.getEmotes().getBoolean("enabled");

        ConfigurationSection section = settings.getEmotes().getConfigurationSection("emotes");

        for(String str : section.getKeys(false)) {
            String identifier = settings.getEmotes().getString("emotes." + str + ".identifier");
            String emote = settings.getEmotes().getString("emotes." + str + ".emote");
            String permission = settings.getEmotes().getString("emotes." + str + ".permission");
            emotes.add(new Emote(identifier, emote, permission));
        }
    }

    /**
     * Replaces registered emotes in a Component.
     * @param input Component to replace emotes in.
     * @return Reformatted Component.
     */
    public Component replaceEmotes(Component input) {
        Component output = input;

        for(Emote emote : emotes) {
            output = output.replaceText(TextReplacementConfig.builder().match(emote.getIdentifier()).replacement(MiniMessage.miniMessage().deserialize(emote.getEmote())).build());
        }

        return output;
    }

    /**
     * Replaces registered emotes in a Component, filtered by permissions.
     * @param input Component to replace emotes in.
     * @param player Player to check permissions of.
     * @return Reformatted Component.
     */
    public Component replaceEmotes(Component input, Player player) {
        Component output = input;

        for(Emote emote : emotes) {
            if(!player.hasPermission(emote.getPermissionNode())) {
               continue;
            }

            output = output.replaceText(TextReplacementConfig.builder().match(emote.getIdentifier()).replacement(MiniMessage.miniMessage().deserialize(emote.getEmote())).build());
        }

        return output;
    }

    /**
     * Replaces registered emotes in a String, filtered by permissions.
     * @param input String to replace emotes in.
     * @param player Player to check permissions of.
     * @return Reformatted String.
     */
    public String replaceEmotes(String input, Player player) {
        String output = input;

        for(Emote emote : emotes) {
            if(!player.hasPermission(emote.getPermissionNode())) {
                continue;
            }

            output = output.replace(emote.getIdentifier(), emote.getEmote());
        }

        return output;
    }

    /**
     * Replaces registered emotes in a String.
     * @param input String to replace emotes in.
     * @return Reformatted String.
     */
    public String replaceEmotes(String input) {
        String output = input;

        for(Emote emote : emotes) {
            output = output.replace(emote.getIdentifier(), emote.getEmote());
        }

        return output;
    }
}