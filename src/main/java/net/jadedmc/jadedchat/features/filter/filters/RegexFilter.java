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
package net.jadedmc.jadedchat.features.filter.filters;

import net.jadedmc.jadedchat.features.filter.Filter;
import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.settings.Message;
import org.bukkit.entity.Player;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Filters chat based off configured regex statements.
 */
public class RegexFilter extends Filter {
    private final JadedChatPlugin plugin;

    /**
     * Creates the filter.
     * @param plugin Instance of the plugin.
     */
    public RegexFilter(JadedChatPlugin plugin) {
        this.plugin = plugin;
        setSilentFail(plugin.settingsManager().getFilter().getBoolean("RegexFilter.silent"));
        setFailMessage(plugin.settingsManager().getMessage(Message.FILTER_REGEX));
    }

    /**
     * Checks if a message passes the filter.
     * @param player Player who sent the message.
     * @param message Message they sent.
     * @return If the message passes the filter.
     */
    @Override
    public boolean passesFilter(Player player, String message) {
        // Exit if the filter is disabled.
        if(!plugin.settingsManager().getFilter().getBoolean("RegexFilter.enabled")) {
            return true;
        }

        // Checks if the player has bypass permissions.
        if(player.hasPermission("jadedcore.bypass.regexfilter")) {
            return true;
        }

        // Remove misc characters before filtering.
        String toFilter = message;
        for(String toRemove : plugin.settingsManager().getFilter().getStringList("MiscCharacters.characters")) {
            toFilter = toFilter.replace(toRemove, "");
        }

        // Loops through each regex statement in the configured list.
        for(String filter : plugin.settingsManager().getFilter().getStringList("RegexFilter.filter")) {
            Pattern pattern = Pattern.compile(filter);
            Matcher matcher = pattern.matcher(toFilter);

            // Checks if there is a match.
            if(matcher.find()) {
                // If so, the message fails.
                return false;
            }
        }

        // The message passes.
        return true;
    }
}