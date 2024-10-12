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
 * This class runs the /message command, which allows a player to privately message another.
 * aliases:
 *   - /msg
 *   - /whisper
 *   - /w
 *   - /tell
 *   - /pm
 *   - /dm
 */
public class MessageCMD implements CommandExecutor {
    private final JadedChatPlugin plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public MessageCMD(JadedChatPlugin plugin) {
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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Only players should be able to message each other.
        if(!(sender instanceof Player player)) {
            return true;
        }

        // Make sure they're using the command properly.
        if(args.length < 2) {
            ChatUtils.chat(player, plugin.getConfigManager().getMessage(Message.MESSAGE_USAGE));
            return true;
        }

        // Gets the target player.
        Player target = plugin.getServer().getPlayer(args[0]);

        // Make sure they're online.
        if(target == null) {
            ChatUtils.chat(player, plugin.getConfigManager().getMessage(Message.MESSAGE_NOT_ONLINE));
            return true;
        }

        // Make sure the target isn't the player.
        if(target.equals(player)) {
            ChatUtils.chat(player, plugin.getConfigManager().getMessage(Message.MESSAGE_SELF));
            return true;
        }

        // Gets the message from the arguments by creating a new array ignoring the username and turning it into a list.
        String message = StringUtils.join(Arrays.asList(Arrays.copyOfRange(args, 1, args.length)), " ");

        // Processes the private message.
        plugin.messageManager().processMessage(player, target, message);

        return true;
    }
}