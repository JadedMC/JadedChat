package net.jadedmc.jadedchat.emotes;

/**
 * A wrapper class for text replacements.
 * Used to add configurable emotes.
 */
public class Emote {
    private final String identifier;
    private final String emote;

    /**
     * Creates the emote.
     * @param identifier Text to be replaced.
     * @param emote What it is replaced with.
     */
    public Emote(String identifier, String emote) {
        this.identifier = identifier;
        this.emote = emote;
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
}