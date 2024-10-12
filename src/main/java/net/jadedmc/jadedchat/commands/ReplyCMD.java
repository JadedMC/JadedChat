package net.jadedmc.jadedchat.commands;

import net.jadedmc.jadedchat.JadedChatPlugin;
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
    private final JadedChatPlugin plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public ReplyCMD(JadedChatPlugin plugin) {
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
        if(!(sender instanceof Player player)) {
            return true;
        }

        // Make sure they're using the command properly.
        if(args.length < 1) {
            ChatUtils.chat(player, plugin.getConfigManager().getMessage(Message.REPLY_USAGE));
            return true;
        }

        // Get the target using the Message Manager.
        Player target = plugin.messageManager().getReplyTarget(player);

        // Make sure there is a target.
        if(target == null) {
            ChatUtils.chat(player, plugin.getConfigManager().getMessage(Message.REPLY_NOT_ONLINE));
            return true;
        }

        // Gets the message from the arguments by taking the args and turning it into a list.
        String message = StringUtils.join(Arrays.asList(args), " ");

        // Processes the private message.
        plugin.messageManager().processMessage(player, target, message);

        return true;
    }
}