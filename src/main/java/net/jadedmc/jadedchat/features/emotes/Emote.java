/*
 * This file is part of JadedChat, licensed under the MIT License.
 *
 *  Copyright (c) JadedMC
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */
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