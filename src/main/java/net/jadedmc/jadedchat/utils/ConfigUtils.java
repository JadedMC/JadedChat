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
        if(section.isSet(key)) {
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
