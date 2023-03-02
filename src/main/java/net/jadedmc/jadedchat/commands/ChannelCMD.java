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

public class ChannelCMD implements CommandExecutor {
    private final JadedChat plugin;

    public ChannelCMD(JadedChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        // Only players should be able to use chat channels.
        if(!(sender instanceof Player player)) {
            return true;
        }

        // Make sure they're using the command properly.
        if(args.length < 1) {
            ChatUtils.chat(player, "&c&lUsage &8» &c/chat [channel] <message>");
            return true;
        }

        // Makes sure the channel exists.
        if(plugin.getChannelManager().getChannel(args[0]) == null) {
            ChatUtils.chat(player, "&c&lError &8» &cThat channel does not exist!");
            return true;
        }

        Channel channel = plugin.getChannelManager().getChannel(args[0]);

        // Makes sure the player has access to the channel.
        if(!player.hasPermission(channel.getPermissionNode())) {
            ChatUtils.chat(player, "&c&lError &8» &cYou do not have access to that channel.");
            return true;
        }

        // Checks if the channel should be toggled or used.
        if(args.length == 1) {
            // Toggles the channel being used.
            plugin.getChannelManager().setChannel(player, plugin.getChannelManager().getChannel(args[0]));
            ChatUtils.chat(player, "&a&lChat &8» &aChannel set to &7" + channel.getName() + "&a.");
        }
        else {
            // Gets the message from the arguments by creating a new array ignoring the username and turning it into a list.
            String message = StringUtils.join(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), "");
            plugin.getChannelManager().getChannel(args[0]).sendMessage(player, message);
        }

        return true;
    }
}
