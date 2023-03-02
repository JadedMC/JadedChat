package net.jadedmc.jadedchat.features.emotes;

import net.jadedmc.jadedchat.JadedChat;
import net.jadedmc.jadedchat.SettingsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads emotes from the configuration file
 * and stores them for future use.
 */
public class EmoteManager {
    private final JadedChat plugin;
    private final List<Emote> emotes = new ArrayList<>();

    /**
     * Creates the emote manager.
     * @param plugin Instance of the plugin.
     */
    public EmoteManager(JadedChat plugin) {
        this.plugin = plugin;
        registerEmotes();
    }

    /**
     * Get the list of emotes.
     * @return Emotes.
     */
    public List<Emote> getEmotes() {
        return emotes;
    }

    /**
     * Loops through config to create emote objects.
     */
    public void registerEmotes() {
        SettingsManager settings = plugin.getSettingsManager();
        ConfigurationSection section = settings.getEmotes().getConfigurationSection("emotes");

        for(String str : section.getKeys(false)) {
            String identifier = settings.getEmotes().getString("emotes." + str + ".identifier");
            String emote = settings.getEmotes().getString("emotes." + str + ".emote");
            String permission = settings.getEmotes().getString("emotes." + str + ".permission");
            emotes.add(new Emote(identifier, emote, permission));
        }
    }

    public Component replaceEmotes(Component input) {
        Component temp = input;

        for(Emote emote : emotes) {
            temp = temp.replaceText(TextReplacementConfig.builder().match(emote.getIdentifier()).replacement(MiniMessage.miniMessage().deserialize(emote.getEmote())).build());
        }

        return temp;
    }

    public Component replaceEmotes(Component input, Player player) {
        Component temp = input;

        for(Emote emote : emotes) {
            if(!player.hasPermission(emote.getPermissionNode())) {
               continue;
            }

            temp = temp.replaceText(TextReplacementConfig.builder().match(emote.getIdentifier()).replacement(MiniMessage.miniMessage().deserialize(emote.getEmote())).build());
        }

        return temp;
    }

    public String replaceEmotes(String input, Player player) {
        String output = input;

        for(Emote emote : emotes) {
            if(!player.hasPermission(emote.getPermissionNode())) {
                continue;
            }

            output = output.replace(emote.getIdentifier(), emote.getEmote());
        }

        return output;
    }

    public String replaceEmotes(String input) {
        String output = input;

        for(Emote emote : emotes) {
            output = output.replace(emote.getIdentifier(), emote.getEmote());
        }

        return output;
    }
}