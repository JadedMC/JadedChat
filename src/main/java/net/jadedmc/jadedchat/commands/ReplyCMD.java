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
import net.jadedmc.jadedchat.settings.Message;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.jadedmc.jadedchat.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * This class runs the reply command, which allows a player to easily reply to a private message.
 * aliases:
 * - /respond
 * - /r
 */
public class ReplyCMD implements CommandExecutor {
    private final JadedChat plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public ReplyCMD(JadedChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the command is executed.
     * @param sender Source of the command
     * @param cmd Command which was executed
     * @param label Alias of the command which was used
     * @param args Passed command arguments
     * @return If the command was successful.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Only players should be able to message each other.
        if(!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        // Make sure they're using the command properly.
        if(args.length < 1) {
            ChatUtils.chat(player, plugin.getSettingsManager().getMessage(Message.REPLY_USAGE));
            return true;
        }

        // Get the target using the Message Manager.
        Player target = plugin.getMessageManager().getReplyTarget(player);

        // Make sure there is a target.
        if(target == null) {
            ChatUtils.chat(player, plugin.getSettingsManager().getMessage(Message.REPLY_NOT_ONLINE));
            return true;
        }

        // Gets the message from the arguments by taking the args and turning it into a list.
        String message = StringUtils.join(Arrays.asList(args), " ");

        // Processes the private message.
        plugin.getMessageManager().processMessage(player, target, message);

        return true;
    }
}
