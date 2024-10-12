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

import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;
import net.jadedmc.jadedchat.features.channels.fomat.ChatFormat;
import net.jadedmc.jadedchat.settings.Message;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.jadedmc.jadedchat.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * This class runs the /jadedchat command, which is the main admin command for the plugin.
 * aliases:
 * - jc
 */
public class JadedChatCMD implements CommandExecutor, TabCompleter {
    private final JadedChatPlugin plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public JadedChatCMD(JadedChatPlugin plugin) {
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

        // Makes sure the sender has permission to use the command.
        if(!sender.hasPermission("jadedchat.admin")) {
            ChatUtils.chat(sender, plugin.getConfigManager().getMessage(Message.JADEDCHAT_NO_PERMISSION));
            return true;
        }

        // Makes sure the command has an argument.
        if(args.length == 0) {
            args = new String[]{"help"};
        }

        // Get the sub command used.
        String subCommand = args[0].toLowerCase();

        // Runs the used sub command.
        switch (subCommand) {
            // Reloads all plugin configuration files.
            case "reload" -> {
                plugin.getConfigManager().reload();
                plugin.channelManager().loadChannels();
                plugin.emoteManager().registerEmotes();
                ChatUtils.chat(sender, "<green><bold>JadedChat</bold> <dark_gray>» <green>Configuration files reloaded successfully!");
            }

            // Displays all currently loaded channels.
            case "list", "channels" -> {
                ChatUtils.chat(sender, "<green><bold>JadedChat</bold> <dark_gray>» <green>Currently Loaded Channels:");
                for (ChatChannel channel : plugin.channelManager().getLoadedChannels()) {
                    ChatUtils.chat(sender, "  <dark_gray>➤ <gray><hover:show_text:\"<green>Click to switch channels</green>\"><click:suggest_command:/channel " + channel.name() + ">" + channel.displayName() + "</click></hover>");
                }
            }

            // Displays the plugin's current version.
            case "version" ->
                    ChatUtils.chat(sender, "<green><bold>JadedChat</bold> <dark_gray>» <green>Current version: <white>" + plugin.getDescription().getVersion());
            case "test" -> {
                if (args.length < 4) {
                    ChatUtils.chat(sender, "<red><bold>Usage</bold> <dark_gray>» <red>/jc test [channel] [format] [message]");
                    return true;
                }
                ChatChannel channel = plugin.channelManager().getChannel(args[1]);
                if (channel == null) {
                    ChatUtils.chat(sender, "<red><bold>Error</bold> <dark_gray>» <red>That channel does not exist!");
                    return true;
                }
                ChatFormat format = channel.format(args[2]);
                if (format == null) {
                    ChatUtils.chat(sender, "<red><bold>Error</bold> <dark_gray>» <red>That format does not exist!");
                    return true;
                }
                String message = StringUtils.join(Arrays.copyOfRange(args, 3, args.length), " ");
                channel.sendTestMessage(plugin, (Player) sender, format, message);
            }

            // Displays the help menu.
            default -> {
                ChatUtils.chat(sender, "<green><bold>JadedChat Commands");
                ChatUtils.chat(sender, "<green>/jc channels <dark_gray>» <white>Lists all loaded channels.");
                ChatUtils.chat(sender, "<green>/jc reload <dark_gray>» <white>Reloads all configuration files.");
                ChatUtils.chat(sender, "<green>/jc test <dark_gray>» <white>Send a message using a specific channel and format.");
                ChatUtils.chat(sender, "<green>/jc version <dark_gray>» <white>Displays the plugin version.");
            }
        }

        return true;
    }

    /**
     * Processes command tab completion.
     * @param sender Command sender.
     * @param cmd Command.
     * @param label Command label.
     * @param args Arguments of the command.
     * @return Tab completion.
     */
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {

        // Lists all subcommands if the player hasn't picked one yet.
        if(args.length < 2) {
            return Arrays.asList("channels", "help", "reload", "test", "version");
        }

        // Runs tab complete for the "test" sub command.
        if(args[0].equalsIgnoreCase("test")) {
            // List all available channels to test.
            if(args.length < 3) {
                List<String> channels = new ArrayList<>();
                plugin.channelManager().getLoadedChannels().forEach(channel -> channels.add(channel.name()));
                return channels;
            }

            // Display all formats for the selected channel.
            if(args.length < 4) {
                ChatChannel channel = plugin.channelManager().getChannel(args[1]);

                if(channel == null) {
                    return Collections.emptyList();
                }

                List<String> formats = new ArrayList<>();
                channel.formats().forEach(format -> formats.add(format.id()));
                return formats;
            }
        }

        // Otherwise, send an empty list.
        return Collections.emptyList();
    }
}