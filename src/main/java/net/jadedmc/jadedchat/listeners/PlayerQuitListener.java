package net.jadedmc.jadedchat.listeners;

import net.jadedmc.jadedchat.JadedChat;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final JadedChat plugin;

    public PlayerQuitListener(JadedChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getChannelManager().removePlayer(event.getPlayer());
    }
}