package net.jadedmc.jadedchat.listeners;

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.features.channels.Channel;
import net.jadedmc.jadedchat.settings.Message;
import net.jadedmc.jadedchat.utils.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class AsyncPlayerChatListener implements Listener {
    private final JadedChat plugin;

    /**
     * To be able to access the configuration files, we need to pass an instance of the plugin to our listener.
     * @param plugin Instance of the plugin.
     */
    public AsyncPlayerChatListener(final JadedChat plugin) {
        this.plugin = plugin;
    }

    /**
     * Runs when the event is called.
     * @param event AsyncChatEvent.
     */
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        // Prevent conflicts with other plugins by exiting if the event is canceled.
        if(event.isCancelled()) {
            return;
        }

        // Get the player's current channel
        Player player = event.getPlayer();
        Channel channel = plugin.getChannelManager().getChannel(player);

        // Make sure they are actually in a channel.
        if(channel == null) {
            ChatUtils.chat(player, plugin.getSettingsManager().getMessage(Message.CHANNEL_NOT_IN_CHANNEL));
            return;
        }

        // Sends the chat message to their channel.
        channel.sendMessage(player, event.getMessage());
        event.setCancelled(true);
    }
}
