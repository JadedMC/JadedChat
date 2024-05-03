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
package net.jadedmc.jadedchat.features.channels.events;

import net.jadedmc.jadedchat.features.channels.channel.ChatChannel;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Collection;

public class ChannelMessageSendEvent  extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    private boolean cancelled = false;
    private final Player player;
    private final ChatChannel channel;
    private final String message;
    private final Component formattedMessage;
    private Collection<Player> viewers;

    public ChannelMessageSendEvent(Player player, ChatChannel channel, String message, Component formattedMessage) {
        this.player = player;
        this.channel = channel;
        this.message = message;
        this.formattedMessage = formattedMessage;

        viewers = channel.viewers(player);
    }

    public ChatChannel getChannel() {
        return channel;
    }

    public Component getFormattedMessage() {
        return formattedMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public String getMessage() {
        return message;
    }

    public Player getPlayer() {
        return player;
    }

    public Collection<Player> getViewers() {
        return viewers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setViewers(Collection<Player> viewers) {
        this.viewers = viewers;
    }
}