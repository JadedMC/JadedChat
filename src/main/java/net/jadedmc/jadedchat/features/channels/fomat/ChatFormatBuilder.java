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

public class ChatFormatBuilder {
    private final ChatFormat chatFormat;

    public ChatFormatBuilder(String id) {
        chatFormat = new ChatFormat(id);
    }

    public ChatFormatBuilder(ConfigurationSection config) {
        chatFormat = new ChatFormat(config.getName());

        if(config.isSet("settings.all")) chatFormat.allTags(config.getBoolean("settings.all"));
        if(config.isSet("settings.color")) chatFormat.color(config.getBoolean("settings.color"));
        if(config.isSet("settings.decorations")) chatFormat.decorations(config.getBoolean("settings.decorations"));
        if(config.isSet("settings.events")) chatFormat.events(config.getBoolean("settings.events"));

        ConfigurationSection sections = config.getConfigurationSection("segments");
        if(sections == null) {
            return;
        }

        for(String section : sections.getKeys(false)) {
            addSection(section, config.getString("segments." + section));
        }
    }

    public ChatFormat build() {
        return chatFormat;
    }

    public ChatFormatBuilder addSection(String sectionID, String section) {
        chatFormat.addSection(sectionID, section);
        return this;
    }

    public ChatFormatBuilder allTags(boolean allTags) {
        chatFormat.allTags(allTags);
        return this;
    }

    public ChatFormatBuilder color(boolean color) {
        chatFormat.color(color);
        return this;
    }

    public ChatFormatBuilder decorations(boolean decorations) {
        chatFormat.decorations();
        return this;
    }

    public ChatFormatBuilder events(boolean events) {
        chatFormat.events(events);
        return this;
    }
}