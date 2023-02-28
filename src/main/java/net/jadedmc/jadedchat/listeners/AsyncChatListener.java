package net.jadedmc.jadedchat.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.channels.Channel;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
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

        for(Audience viewer : event.viewers()) {
            Player target = Bukkit.getPlayer(String.valueOf(viewer.get(Identity.UUID)));

            if(target != null) {
                if(!target.hasPermission(channel.getPermissionNode())) {
                    continue;
                }
            }

            viewer.sendMessage(channel.getFormat(player).processMessage(player, PlainTextComponentSerializer.plainText().serialize(event.message())));
        }
        event.setCancelled(true);
    }
}
