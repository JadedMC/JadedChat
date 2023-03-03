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
package net.jadedmc.jadedchat.utils;

import org.bukkit.configuration.ConfigurationSection;

/**
 * A collection of utilities to make working with configuration files easier.
 */
public class ConfigUtils {

    /**
     * Get a configured boolean with a pre-defined default.
     * @param section Configuration Section to get the value from.
     * @param key Key to get value of.
     * @param defaultValue Default value if the key is not set.
     * @return Configured boolean.
     */
    public static boolean getBoolean(ConfigurationSection section, String key, boolean defaultValue) {
        // Makes sure the configuration section exists.
        if(section == null) {
            return defaultValue;
        }

        // Makes sure the key exists.
        if(!section.isSet(key)) {
            return defaultValue;
        }

        // Returns the configured value.
        return section.getBoolean(key);
    }

    /**
     * Get a configured int with a pre-defined default.
     * @param section Configuration Section to get the value from.
     * @param key Key to get value of.
     * @param defaultValue Default value if the key is not set.
     * @return Configured boolean.
     */
    public static int getInt(ConfigurationSection section, String key, int defaultValue) {
        // Makes sure the configuration section exists.
        if(section == null) {
            return defaultValue;
        }

        // Makes sure the key exists.
        if(section.isSet(key)) {
            return defaultValue;
        }

        // Returns the configured value.
        return section.getInt(key);
    }

    /**
     * Get a configured String with a pre-defined default.
     * @param section Configuration Section to get the value from.
     * @param key Key to get value of.
     * @param defaultValue Default value if the key is not set.
     * @return Configured boolean.
     */
    public static String getString(ConfigurationSection section, String key, String defaultValue) {
        // Makes sure the configuration section exists.
        if(section == null) {
            return defaultValue;
        }

        // Makes sure the key exists.
        if(section.isSet(key)) {
            return defaultValue;
        }

        // Returns the configured value.
        return section.getString(key);
    }
}
