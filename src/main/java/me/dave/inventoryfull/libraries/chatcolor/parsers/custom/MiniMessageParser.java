/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.kyori.adventure.text.minimessage.MiniMessage
 *  net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
 *  org.bukkit.entity.Player
 */
package me.dave.inventoryfull.libraries.chatcolor.parsers.custom;

import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.Parser;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;

public class MiniMessageParser
implements Parser {
    private static final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public String parseString(String string) {
        string = string.replace('\u00a7', '&');
        return LegacyComponentSerializer.builder().hexColors().build().serialize(miniMessage.deserialize(string));
    }

    @Override
    public String parseString(String string, Player player) {
        return this.parseString(string);
    }
}

