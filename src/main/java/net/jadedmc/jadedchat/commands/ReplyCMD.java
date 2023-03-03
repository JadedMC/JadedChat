package net.jadedmc.jadedchat.commands;

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.utils.ChatUtils;
import net.jadedmc.jadedchat.utils.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class ReplyCMD implements CommandExecutor {
    private final JadedChat plugin;

    public ReplyCMD(JadedChat plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // Only players should be able to message each other.
        if(!(sender instanceof Player player)) {
            return true;
        }

        // Make sure they're using the command properly.
        if(args.length < 1) {
            ChatUtils.chat(player, "<red><bold>Usage</bold> <dark_gray>» <red>/r [message]");
            return true;
        }

        // Get the target using the Message Manager.
        Player target = plugin.getMessageManager().getReplyTarget(player);

        // Make sure there is a target.
        if(target == null) {
            ChatUtils.chat(player, "<red><bold>Error</bold> &8» <red>You have no one to reply to!");
            return true;
        }

        // Gets the message from the arguments by taking the args and turning it into a list.
        String message = StringUtils.join(Arrays.asList(args), " ");

        // Processes the private message.
        plugin.getMessageManager().processMessage(player, target, message);

        return true;
    }
}
