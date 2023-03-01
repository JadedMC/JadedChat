package net.jadedmc.jadedchat.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Some methods to make sending chat messages easier.
 */
public class ChatUtils {
    private final static int CENTER_PX = 154;

    /**
     * A quick way to send a CommandSender a colored message.
     * @param sender CommandSender to send message to.
     * @param message The message being sent.
     */
    public static void chat(CommandSender sender, String message) {
        sender.sendMessage(translate(message));
    }

    /**
     * Translates a String to a colorful String using methods in the BungeeCord API.
     * @param message Message to translate.
     * @return Translated Message.
     */
    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}