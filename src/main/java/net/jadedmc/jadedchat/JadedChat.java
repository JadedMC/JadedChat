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
package net.jadedmc.jadedchat;

import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;

import java.io.File;

public class JadedChat {
    private static JadedChatPlugin plugin;

    public static boolean useLuckPerms() {
        return true;
    }

    public static boolean isPaper() {

        try {
            Class.forName("com.destroystokyo.paper.ParticleBuilder");
        }
        catch (ClassNotFoundException ignored) {
            return false;
        }

        return true;
    }

    public static void setPlugin(JadedChatPlugin pl) {
        plugin = pl;
    }

    public static File getDataFolder() {
        return plugin.getDataFolder();
    }

    public static void loadChannel(ChatChannel channel) {
        plugin.channelManager().loadChannel(channel);
    }

    public static boolean channelExists(String channel) {
        return (plugin.channelManager().getChannel(channel) != null);
    }
}