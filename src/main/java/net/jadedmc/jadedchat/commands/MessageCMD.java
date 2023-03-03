package net.jadedmc.jadedchat.commands;

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.jadedmc.jadedchat.utils.StringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MessageCMD implements CommandExecutor {
    private final JadedChat plugin;

    public MessageCMD(JadedChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Only players should be able to message each other.
        if(!(sender instanceof Player player)) {
            return true;
        }

        // Make sure they're using the command properly.
        if(args.length < 2) {
            ChatUtils.chat(player, "<red><bold>Usage</bold> <dark_gray>» <red>/msg [player] [message]");
            return true;
        }

        // Gets the target player.
        Player target = plugin.getServer().getPlayer(args[0]);

        // Make sure they're online.
        if(target == null) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>That player isn't online!");
            return true;
        }

        // Make sure the target isn't the player.
        if(target.equals(player)) {
            ChatUtils.chat(player, "<red><bold>Error</bold> <dark_gray>» <red>You cannot message yourself!");
            return true;
        }

        // Gets the message from the arguments by creating a new array ignoring the username and turning it into a list.
        String message = StringUtils.join(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), " ");

        // Processes the private message.
        plugin.getMessageManager().processMessage(player, target, message);

        return true;
    }
}
