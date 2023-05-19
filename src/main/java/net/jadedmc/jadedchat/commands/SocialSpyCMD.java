package net.jadedmc.jadedchat.commands;

import net.jadedmc.jadedchat.JadedChatPlugin;
import net.jadedmc.jadedchat.settings.Message;
import net.jadedmc.jadedchat.utils.ChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SocialSpyCMD implements CommandExecutor {
    private final JadedChatPlugin plugin;

    public SocialSpyCMD(JadedChatPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        // Makes sure the sender is a player, and not the console.
        if(!(sender instanceof Player)) {
            ChatUtils.chat(sender, plugin.settingsManager().getMessage(Message.SOCIAL_SPY_NOT_A_PLAYER));
            return true;
        }

        Player player = (Player) sender;

        // Makes sure the player has permission to use the command.
        if(!player.hasPermission("jadedchat.socialspy")) {
            ChatUtils.chat(player, plugin.settingsManager().getMessage(Message.SOCIAL_SPY_NO_PERMISSION));
            return true;
        }

        // Toggles social spy for the player.
        plugin.messageManager().toggleSocialSpy(player);

        // Sends a message to the player telling them what they did.
        if(plugin.messageManager().isSpying(player)) {
            ChatUtils.chat(player, plugin.settingsManager().getMessage(Message.SOCIAL_SPY_ENABLED));
        }
        else {
            ChatUtils.chat(player, plugin.settingsManager().getMessage(Message.SOCIAL_SPY_DISABLED));
        }

        return true;
    }
}