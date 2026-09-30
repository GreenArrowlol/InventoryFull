/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  me.clip.placeholderapi.PlaceholderAPI
 *  org.bukkit.entity.Player
 */
package me.dave.inventoryfull.libraries.chatcolor.parsers.custom;

import me.clip.placeholderapi.PlaceholderAPI;
import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.Parser;
import org.bukkit.entity.Player;

public class PlaceholderAPIParser
implements Parser {
    @Override
    public String parseString(String string) {
        return PlaceholderAPI.setPlaceholders(null, (String)string);
    }

    @Override
    public String parseString(String string, Player player) {
        return PlaceholderAPI.setPlaceholders((Player)player, (String)string);
    }
}

