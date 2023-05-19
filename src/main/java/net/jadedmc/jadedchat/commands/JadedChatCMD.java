package net.jadedmc.jadedchat.commands;

import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;
import net.jadedmc.jadedchat.settings.Message;
import net.jadedmc.jadedchat.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

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
            ChatUtils.chat(sender, plugin.settingsManager().getMessage(Message.JADEDCHAT_NO_PERMISSION));
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
            case "reload":
                plugin.settingsManager().reload();
                plugin.channelManager().loadChannels();
                plugin.emoteManager().registerEmotes();
                ChatUtils.chat(sender, "<green><bold>JadedChat</bold> <dark_gray>» <green>Configuration files reloaded successfully!");
                break;

            // Displays all currently loaded channels.
            case "channels":
                ChatUtils.chat(sender, "<green><bold>JadedChat</bold> <dark_gray>» <green>Currently Loaded Channels:");

                for(ChatChannel channel : plugin.channelManager().getLoadedChannels()) {
                    ChatUtils.chat(sender, "  <dark_gray>➤ <gray><hover:show_text:\"<green>Click to switch channels</green>\"><click:suggest_command:/channel " + channel.name() + ">" + channel.displayName() + "</click></hover>");
                }
                break;

            // Displays the plugin's current version.
            case "version":
                ChatUtils.chat(sender, "<green><bold>JadedChat</bold> <dark_gray>» <green>Current version: <white>" + plugin.getDescription().getVersion());
                break;

            // Displays the help menu.
            default:
                ChatUtils.chat(sender, "<green><bold>JadedChat Commands");
                ChatUtils.chat(sender, "<green>/jc channels <dark_gray>» <white>Lists all loaded channels.");
                ChatUtils.chat(sender, "<green>/jc reload <dark_gray>» <white>Reloads all configuration files.");
                ChatUtils.chat(sender, "<green>/jc version <dark_gray>» <white>Displays the plugin version.");
                break;
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
            return Arrays.asList("channels", "help", "reload", "version");
        }

        // Otherwise, send an empty list.
        return Collections.emptyList();
    }
}