/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */
package me.dave.inventoryfull.libraries.chatcolor.messengers;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface Messenger {
    public void sendMessage(@NotNull CommandSender var1, @Nullable String var2);

    public void sendMessage(@NotNull CommandSender var1, String ... var2);

    public void sendMessage(CommandSender[] var1, @Nullable String var2);

    public void sendMessage(CommandSender[] var1, String ... var2);

    public void broadcastMessage(@Nullable String var1);

    public void broadcastMessage(String ... var1);

    public void sendActionBarMessage(@NotNull Player var1, @Nullable String var2);

    public void sendActionBarMessage(@NotNull Player[] var1, @Nullable String var2);
}

