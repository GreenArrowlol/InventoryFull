/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.dave.inventoryfull.libraries.chatcolor.parsers.custom;

import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.Parser;
import org.bukkit.entity.Player;

public class LegacyCharParser
implements Parser {
    @Override
    public String parseString(String string) {
        return string.replaceAll("\u00a7", "&");
    }

    @Override
    public String parseString(String string, Player player) {
        return this.parseString(string);
    }
}

