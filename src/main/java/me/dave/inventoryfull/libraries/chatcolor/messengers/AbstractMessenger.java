/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.dave.inventoryfull.libraries.chatcolor.messengers;

import me.dave.inventoryfull.libraries.chatcolor.messengers.Messenger;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractMessenger
implements Messenger {
    @Override
    public void sendMessage(@NotNull CommandSender recipient, String ... messages) {
        this.sendMessage(recipient, String.join((CharSequence)" ", messages));
    }

    @Override
    public void sendMessage(CommandSender[] recipients, @Nullable String message) {
        for (CommandSender recipient : recipients) {
            this.sendMessage(recipient, message);
        }
    }

    @Override
    public void sendMessage(CommandSender[] recipients, String ... messages) {
        if (messages == null) {
            return;
        }
        String message = String.join((CharSequence)" ", messages);
        for (CommandSender recipient : recipients) {
            this.sendMessage(recipient, message);
        }
    }

    @Override
    public void broadcastMessage(String ... messages) {
        if (messages == null) {
            return;
        }
        this.broadcastMessage(String.join((CharSequence)" ", messages));
    }

    @Override
    public void sendActionBarMessage(@NotNull Player[] players, @Nullable String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        for (Player player : players) {
            this.sendActionBarMessage(player, message);
        }
    }
}

