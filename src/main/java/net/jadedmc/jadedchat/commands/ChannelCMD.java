package net.jadedmc.jadedchat.commands;

import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;
import net.jadedmc.jadedchat.features.channels.events.ChannelSwitchEvent;
import net.jadedmc.jadedchat.settings.Message;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.jadedmc.jadedchat.utils.StringUtils;
import org.bukkit.Bukkit;
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
 * This class runs the /channel command, which allows a player to switch chat channels.
 * aliases:
 * - chat
 * - ch
 */
public class ChannelCMD implements CommandExecutor, TabCompleter {
    private final JadedChatPlugin plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public ChannelCMD(JadedChatPlugin plugin) {
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
            ChatUtils.chat(player, plugin.settingsManager().getMessage(Message.CHANNEL_USAGE));
            return true;
        }

        // Makes sure the channel exists.
        if(plugin.channelManager().getChannel(args[0]) == null) {
            ChatUtils.chat(player, plugin.settingsManager().getMessage(Message.CHANNEL_DOES_NOT_EXIST));
            return true;
        }

        ChatChannel channel = plugin.channelManager().getChannel(args[0]);

        // Makes sure the player has access to the channel.
        if(!player.hasPermission(channel.permission()) && !channel.permission().equalsIgnoreCase("")) {
            ChatUtils.chat(player, plugin.settingsManager().getMessage(Message.CHANNEL_NO_PERMISSION));
            return true;
        }

        // Checks if the channel should be toggled or used.
        if(args.length == 1) {
            ChannelSwitchEvent event = new ChannelSwitchEvent(player, plugin.channelManager().getChannel(player), plugin.channelManager().getChannel(args[0]));
            Bukkit.getPluginManager().callEvent(event);

            // Exit if the even is cancelled.
            if(event.isCancelled()) {
                return true;
            }

            // Toggles the channel being used.
            plugin.channelManager().setChannel(player, event.getToChannel());
            ChatUtils.chat(player, plugin.settingsManager().getMessage(Message.CHANNEL_SWITCH).replace("<channel>", channel.displayName()));
        }
        else {
            // Gets the message from the arguments by creating a new array ignoring the username and turning it into a list.
            String message = StringUtils.join(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), " ");
            plugin.channelManager().getChannel(args[0]).sendMessage(plugin, player, message);
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

        // Lists all channels if the player hasn't picked one yet.
        if(args.length == 0) {
            List<String> channels = new ArrayList<>();

            // Find all channels the player has permission to use.
            for(ChatChannel channel : plugin.channelManager().getLoadedChannels()) {
                if(sender.hasPermission(channel.permission())) {
                    channels.add(channel.name());
                }
            }

            // Returns those channels.
            return channels;
        }

        // Only list channels that start with the first argument.
        if(args.length == 1) {
            List<String> channels = new ArrayList<>();
            String start = args[0];

            for(ChatChannel channel : plugin.channelManager().getLoadedChannels()) {
                if(sender.hasPermission(channel.permission()) && channel.name().toLowerCase().startsWith(start.toLowerCase())) {
                    channels.add(channel.name());
                }
            }

            return channels;
        }

        // Otherwise, send an empty list.
        return Collections.emptyList();
    }
}