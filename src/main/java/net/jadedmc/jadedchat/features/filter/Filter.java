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
package net.jadedmc.jadedchat.features.filter;

import org.bukkit.entity.Player;

/**
 * Represents a check to see if a message should be sent in chat.
 */
public abstract class Filter {
    private boolean silentFail;
    private String failMessage;

    /**
     * Creates the filter.
     */
    public Filter() {
        silentFail = true;
        failMessage = "";
    }

    /**
     * Get the message that should be sent if a message fails the check.
     * @return Fail message.
     */
    public String getFailMessage() {
        return failMessage;
    }

    /**
     * Set the message that should be sent if a message fails the check.
     * @param failMessage Fail message.
     */
    public void setFailMessage(String failMessage) {
        this.failMessage = failMessage;
    }

    /**
     * Sets if the check should send a message if a message fails.
     * @param silentFail Whether the check has a silent fail.
     */
    public void setSilentFail(boolean silentFail) {
        this.silentFail = silentFail;
    }

    /**
     * Get if the check should show a message when a message fails it.
     * @return If there is a fail message.
     */
    public boolean showFailMessage() {
        return !silentFail;
    }

    /**
     * Check if a message passes the filter.
     * @param player Player who sent the message.
     * @param message Message that was sent.
     * @return Whether the message passes.
     */
    public abstract boolean passesFilter(Player player, String message);

    /**
     * Removes a player from the filter.
     * Used when a player disconnects from the server.
     * @param player Player to remove from the filter.
     */
    public void removePlayer(Player player) {}
}
