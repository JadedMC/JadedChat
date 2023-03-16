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
package net.jadedmc.jadedchat.commands;

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.features.channels.Channel;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.jadedmc.jadedchat.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * This class runs the /channel command, which allows a player to switch chat channels.
 * aliases:
 * - chat
 * - ch
 */
public class ChannelCMD implements CommandExecutor {
    private final JadedChat plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public ChannelCMD(JadedChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender Source of the command
     * @param command Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return If the command was successful.
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Only players should be able to use chat channels.
        if(!(sender instanceof Player player)) {
            return true;
        }

        // Make sure they're using the command properly.
        if(args.length < 1) {
            ChatUtils.chat(player, "<red><bold>Usage</bold> <dark_gray>» <red>/chat [channel] <message>");
            return true;
        }

        // Makes sure the channel exists.
        if(plugin.getChannelManager().getChannel(args[0]) == null) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>That channel does not exist!");
            return true;
        }

        Channel channel = plugin.getChannelManager().getChannel(args[0]);

        // Makes sure the player has access to the channel.
        if(!player.hasPermission(channel.getPermissionNode())) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You do not have access to that channel.");
            return true;
        }

        // Checks if the channel should be toggled or used.
        if(args.length == 1) {
            // Toggles the channel being used.
            plugin.getChannelManager().setChannel(player, plugin.getChannelManager().getChannel(args[0]));
            ChatUtils.chat(player, "<green><bold>Chat</bold> <dark_gray>» <green>Channel set to <gray>" + channel.getName() + "<green>.");
        }
        else {
            // Gets the message from the arguments by creating a new array ignoring the username and turning it into a list.
            String message = StringUtils.join(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), " ");
            plugin.getChannelManager().getChannel(args[0]).sendMessage(player, message);
        }

        return true;
    }
}
