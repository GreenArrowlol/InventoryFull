/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatMessageType
 *  net.md_5.bungee.api.chat.BaseComponent
 *  net.md_5.bungee.api.chat.TextComponent
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.dave.inventoryfull.libraries.chatcolor.messengers;

import me.dave.inventoryfull.libraries.chatcolor.ChatColorHandler;
import me.dave.inventoryfull.libraries.chatcolor.messengers.AbstractMessenger;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LegacyMessenger
extends AbstractMessenger {
    @Override
    public void sendMessage(@NotNull CommandSender recipient, @Nullable String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        if (recipient instanceof Player) {
            Player player = (Player)recipient;
            recipient.sendMessage(ChatColorHandler.translateAlternateColorCodes(message, player));
        } else {
            recipient.sendMessage(ChatColorHandler.translateAlternateColorCodes(message));
        }
    }

    @Override
    public void broadcastMessage(@Nullable String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        Bukkit.broadcastMessage((String)ChatColorHandler.translateAlternateColorCodes(message));
    }

    @Override
    public void sendActionBarMessage(@NotNull Player player, @Nullable String message) {
        if (message == null || message.isBlank()) {
            return;
        }
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, (BaseComponent)new TextComponent(ChatColorHandler.translateAlternateColorCodes(message)));
    }
}

