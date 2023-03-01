package net.jadedmc.jadedchat.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jadedmc.jadedchat.JadedChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final JadedChat plugin;

    public PlayerQuitListener(JadedChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        plugin.getChannelManager().removePlayer(event.getPlayer());
        plugin.getMessageManager().removePlayer(event.getPlayer());

        if(plugin.getSettingsManager().getConfig().getBoolean("QuitMessage.override")) {

            if(!plugin.getSettingsManager().getConfig().isSet("QuitMessage.message")) {
                event.quitMessage(null);
                return;
            }

            String messageString = plugin.getSettingsManager().getConfig().getString("QuitMessage.message");

            if(messageString == null || messageString.equals("null")) {
                event.quitMessage(null);
                return;
            }

            messageString = PlaceholderAPI.setPlaceholders(event.getPlayer(), messageString);
            messageString = plugin.getEmoteManager().replaceEmotes(messageString);
            Component messageComponent = MiniMessage.miniMessage().deserialize(messageString);
            event.quitMessage(messageComponent);
        }
    }
}