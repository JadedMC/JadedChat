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

import org.bukkit.configuration.ConfigurationSection;

/**
 * Facilitates the creation of chat formats.
 */
public class ChatFormatBuilder {
    private final ChatFormat chatFormat;

    /**
     * Creates a new Chat Format from scratch.
     * @param id Id of the format you want to make.
     */
    public ChatFormatBuilder(String id) {
        chatFormat = new ChatFormat(id);
    }

    /**
     * Creates a Chat Format from a configuration section.
     * @param config Configuration section to load the format from.
     */
    public ChatFormatBuilder(ConfigurationSection config) {
        chatFormat = new ChatFormat(config.getName());

        // Load format settings.
        if(config.isSet("settings.all")) chatFormat.allTags(config.getBoolean("settings.all"));
        if(config.isSet("settings.color")) chatFormat.colorTags(config.getBoolean("settings.color"));
        if(config.isSet("settings.decorations")) chatFormat.decorationTags(config.getBoolean("settings.decorations"));
        if(config.isSet("settings.events")) chatFormat.eventTags(config.getBoolean("settings.events"));

        // Makes sure the format has "sections".
        ConfigurationSection sections = config.getConfigurationSection("segments");
        if(sections == null) {
            return;
        }

        // Loads all "sections".
        for(String section : sections.getKeys(false)) {
            addSection(section, config.getString("segments." + section));
        }
    }

    /**
     * Retrieve the built chat format.
     * @return Built Chat Format.
     */
    public ChatFormat build() {
        return chatFormat;
    }

    /**
     * Adds a section to the format.
     * @param sectionID ID of the section.
     * @param section The section itself.
     * @return Chat Format Builder.
     */
    public ChatFormatBuilder addSection(String sectionID, String section) {
        chatFormat.addSection(sectionID, section);
        return this;
    }

    /**
     * Set if the format should be able to use all MiniMessage tags.
     * @param allTags Whether the format can use all tags.
     * @return Chat Format Builder.
     */
    public ChatFormatBuilder allTags(boolean allTags) {
        chatFormat.allTags(allTags);
        return this;
    }

    /**
     * Set if the format should be able to use color tags.
     * Deprecated. Use colorTags() instead.
     * @param color Whether the format can use color tags.
     * @return Chat Format Builder.
     */
    @Deprecated
    public ChatFormatBuilder color(boolean color) {
        chatFormat.colorTags(color);
        return this;
    }

    /**
     * Set if the format should be able to use color tags.
     * @param colorTags Whether the format can use color tags.
     * @return Chat Format Builder.
     */
    public ChatFormatBuilder colorTags(boolean colorTags) {
        chatFormat.colorTags(colorTags);
        return this;
    }

    /**
     * Set if the format should be able to use decoration tags.
     * Depricated. Use decorationTags() instead.
     * @param decorations Whether the format can use decoration tags.
     * @return Chat Format Builder.
     */
    @Deprecated
    public ChatFormatBuilder decorations(boolean decorations) {
        chatFormat.decorationTags();
        return this;
    }

    /**
     * Set if the format should be able to use decoration tags.
     * @param decorationTags Whether the format can use decoration tags.
     * @return Chat Format Builder.
     */
    public ChatFormatBuilder decorationTags(boolean decorationTags) {
        chatFormat.decorationTags(decorationTags);
        return this;
    }

    /**
     * Set if the format should be able to use event tags.
     * This includes hover and click events.
     * Depricated. Use eventTags() instead.
     * @param events Whether the format can use event tags.
     * @return Chat Format Builder.
     */
    @Deprecated
    public ChatFormatBuilder events(boolean events) {
        chatFormat.eventTags(events);
        return this;
    }

    /**
     * Set if the format should be able to use event tags.
     * This includes hover and click events.
     * @param eventTags Whether the format can use event tags.
     * @return Chat Format Builder.
     */
    public ChatFormatBuilder eventTags(boolean eventTags) {
        chatFormat.eventTags(eventTags);
        return this;
    }
}