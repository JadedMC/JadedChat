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
package net.jadedmc.jadedchat.settings;

import net.jadedmc.jadedchat.JadedChatPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Manages the configurable settings in the plugin.
 */
public class ConfigManager {
    private FileConfiguration config;
    private final File configFile;
    private FileConfiguration emotes;
    private final File emotesFile;
    private FileConfiguration messages;
    private File messagesFile;
    private FileConfiguration filter;
    private File filterFile;

    /**
     * Loads or Creates configuration files.
     * @param plugin Instance of the plugin.
     */
    public ConfigManager(@NotNull final JadedChatPlugin plugin) {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        if(!configFile.exists()) {
            plugin.saveResource("config.yml", false);
            plugin.saveResource("channels/global.yml", false);
            plugin.saveResource("channels/staff.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(configFile);

        emotesFile = new File(plugin.getDataFolder(), "emotes.yml");
        if(!emotesFile.exists()) {
            plugin.saveResource("emotes.yml", false);
        }
        emotes = YamlConfiguration.loadConfiguration(emotesFile);

        messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if(!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        filterFile = new File(plugin.getDataFolder(), "filter.yml");
        if(!filterFile.exists()) {
            plugin.saveResource("filter.yml", false);
        }
        filter = YamlConfiguration.loadConfiguration(filterFile);
    }

    /**
     * Get the config.yml FileConfiguration.
     * @return config.yml FileConfiguration.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get the emotes.yml FileConfiguration.
     * @return emotes.yml FileConfiguration.
     */
    public FileConfiguration getEmotes() {
        return emotes;
    }

    /**
     * Get the filter.yml FileConfiguration.
     * @return filter.yml FileConfiguration.
     */
    public FileConfiguration getFilter() {
        return filter;
    }

    /**
     * Get a configurable message based off it's Message enum.
     * @param message Message num to get message of.
     * @return Resulting configured message.
     */
    public String getMessage(final Message message) {
        if(messages.isSet(message.getMessagePath())) {
            return messages.getString(message.getMessagePath());
        }

        return message.getDefaultMessage();
    }

    /**
     * Update the configuration files.
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        emotes = YamlConfiguration.loadConfiguration(emotesFile);
        filter = YamlConfiguration.loadConfiguration(filterFile);
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }
}