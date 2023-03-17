package net.jadedmc.jadedchat.commands;

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.features.channels.Channel;
import net.jadedmc.jadedchat.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

/**
 * This class runs the /jadedchat command, which is the main admin command for the plugin.
 * aliases:
 * - jc
 */
public class JadedChatCMD implements CommandExecutor {
    private final JadedChat plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public JadedChatCMD(JadedChat plugin) {
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
            ChatUtils.chat(sender, "<red><bold>Error</bold> <dark_gray>» <red>You do not have access to that command.");
            return true;
        }

        // Makes sure the command has an argument.
        if(args.length == 0) {
            return true;
        }

        // Get the sub command used.
        String subCommand = args[0].toLowerCase();

        // Runs the used sub command.
        switch (subCommand) {
            // Reloads all plugin configuration files.
            case "reload" -> {
                plugin.getSettingsManager().reload();
                plugin.getChannelManager().loadChannels();
                ChatUtils.chat(sender, "<green><bold>JadedChat</bold> <dark_gray>» <green>Configuration files reloaded successfully!");
            }
            // Displays all currently loaded channels.
            case "channels" -> {
                ChatUtils.chat(sender, "<green><bold>JadedChat</bold> <dark_gray>» <green>Currently Loaded Channels:");

                for(Channel channel : plugin.getChannelManager().getLoadedChannels()) {
                    ChatUtils.chat(sender, "  <dark_gray>➤ <gray><hover:show_text:\"<green>Click to switch channels</green>\"><click:suggest_command:/channel " + channel.getName() + ">" + channel.getName() + "</click></hover>");
                }
            }
        }

        return true;
    }
}