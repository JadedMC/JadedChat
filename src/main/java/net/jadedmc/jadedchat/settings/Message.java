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
package net.jadedmc.jadedchat.settings;

/**
 * Represents a configurable plugin message.
 * Stores the path to the message in messages.yml, and the default.
 */
public enum Message {
    CHANNEL_DOES_NOT_EXIST("Channel.DoesNotExist", "<red><bold>Error</bold> <dark_gray>» <red>That channel does not exist!"),
    CHANNEL_NO_PERMISSION("Channel.NoPermission", "<red><bold>Error</bold> <dark_gray>» <red>You do not have access to that channel."),
    CHANNEL_NOT_IN_CHANNEL("Channel.NotInChannel", "<red><bold>Error</bold> <dark_gray>» <red>You are not currently in a channel!"),
    CHANNEL_SWITCH("Channel.Switch", "<green><bold>Chat</bold> <dark_gray>» <green>Channel set to <gray><channel><green>."),
    CHANNEL_USAGE("Channel.Usage", "<red><bold>Usage</bold> <dark_gray>» <red>/chat [channel] <message>")
    ,
    MESSAGE_USAGE("Message.Usage", "<red><bold>Usage</bold> <dark_gray>» <red>/msg [player] [message]"),
    MESSAGE_NOT_ONLINE("Message.NotOnline", "<red><bold>Error</bold> <dark_gray>» <red>That player isn't online!"),
    MESSAGE_SELF("Message.Self", "<red><bold>Error</bold> <dark_gray>» <red>You cannot message yourself!"),
    REPLY_USAGE("Reply.Usage", "<red><bold>Usage</bold> <dark_gray>» <red>/r [message]"),
    REPLY_NOT_ONLINE("Reply.NotOnline", "<red><bold>Error</bold> &8» <red>You have no one to reply to!"),
    JADEDCHAT_NO_PERMISSION("JadedChat.NoPermission", "<red><bold>Error</bold> <dark_gray>» <red>You do not have access to that command."),
    FILTER_REGEX("Filter.Regex", "<red><bold>Error</bold> <dark_gray>» <red>You cannot say that!"),
    FILTER_REPEAT_MESSAGE("Filter.RepeatMessage", "<red><bold>Error</bold> <dark_gray>» <red>You cannot say the same message twice!"),
    SOCIAL_SPY_DISABLED("SocialSpy.Disabled", "<green><bold>Chat</bold> <dark_gray>» <green>You have disabled social spy."),
    SOCIAL_SPY_ENABLED("SocialSpy.Enabled", "<green><bold>Chat</bold> <dark_gray>» <green>You have enabled social spy."),
    SOCIAL_SPY_NOT_A_PLAYER("SocialSpy.NotAPlayer", "<red><bold>Error</bold> <dark_gray>» <red>Only players can use that command!"),
    SOCIAL_SPY_NO_PERMISSION("SocialSpy.NoPermission", "<red><bold>Error</bold> <dark_gray>» <red>You do not have access to that command.");

    private final String messagePath;
    private final String defaultMessage;

    Message(String messagePath, String defaultMessage) {
        this.messagePath = messagePath;
        this.defaultMessage = defaultMessage;
    }

    /**
     * Get the default message.
     * @return Default message.
     */
    public String getDefaultMessage() {
        return defaultMessage;
    }

    /**
     * Gets the path to the message in messages.yml
     * @return Message path in String form.
     */
    public String getMessagePath() {
        return messagePath;
    }
}