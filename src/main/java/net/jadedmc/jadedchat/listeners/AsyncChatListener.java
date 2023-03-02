package net.jadedmc.jadedchat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.features.channels.Channel;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener {
    private final JadedChat plugin;

    public AsyncChatListener(final JadedChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChat(AsyncChatEvent event) {
        // Exit if the event was cancelled by another plugin.
        if(event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();
        Channel channel = plugin.getChannelManager().getChannel(player);
        channel.sendMessage(player, PlainTextComponentSerializer.plainText().serialize(event.message()));
        event.setCancelled(true);
    }
}
