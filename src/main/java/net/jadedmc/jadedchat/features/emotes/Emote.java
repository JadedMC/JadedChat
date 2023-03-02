package net.jadedmc.jadedchat.features.emotes;

/**
 * A wrapper class for text replacements.
 * Used to add configurable emotes.
 */
public class Emote {
    private final String identifier;
    private final String emote;
    private final String permission;

    /**
     * Creates the emote.
     * @param identifier Text to be replaced.
     * @param emote What it is replaced with.
     */
    public Emote(String identifier, String emote, String permission) {
        this.identifier = identifier;
        this.emote = emote;
        this.permission = permission;
    }

    /**
     * Gets the replacement text.
     * @return Replacement text.
     */
    public String getEmote() {
        return emote;
    }

    /**
     * Gets the text to be replaced.
     * @return Text to be replaced.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Get the required permission node to use the emote.
     * @return Required permission node.
     */
    public String getPermissionNode() {
        return permission;
    }
}