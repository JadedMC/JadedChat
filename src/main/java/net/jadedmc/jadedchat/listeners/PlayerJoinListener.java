package net.jadedmc.jadedchat.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import net.jadedmc.jadedchat.JadedChat;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final JadedChat plugin;

    public PlayerJoinListener(JadedChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {

        if(plugin.getSettingsManager().getConfig().getBoolean("JoinMessage.override")) {

            if(!plugin.getSettingsManager().getConfig().isSet("JoinMessage.message")) {
                event.joinMessage(null);
                return;
            }

            String messageString = plugin.getSettingsManager().getConfig().getString("JoinMessage.message");

            if(messageString == null || messageString.equals("null")) {
                event.joinMessage(null);
                return;
            }

            messageString = PlaceholderAPI.setPlaceholders(event.getPlayer(), messageString);
            messageString = plugin.getEmoteManager().replaceEmotes(messageString);
            Component messageComponent = MiniMessage.miniMessage().deserialize(messageString);
            event.joinMessage(messageComponent);
        }
    }
}