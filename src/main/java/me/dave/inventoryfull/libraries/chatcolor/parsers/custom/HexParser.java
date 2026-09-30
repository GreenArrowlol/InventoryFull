/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.entity.Player
 */
package me.dave.inventoryfull.libraries.chatcolor.parsers.custom;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.Parser;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class HexParser
implements Parser {
    private static final Pattern hexPattern = Pattern.compile("&#[a-fA-F0-9]{6}");

    @Override
    public String parseString(String string) {
        string = string.replace('\u00a7', '&');
        Matcher match = hexPattern.matcher(string);
        while (match.find()) {
            String color = string.substring(match.start() + 1, match.end());
            string = string.replace("&" + color, ChatColor.of((String)color).toString());
            match = hexPattern.matcher(string);
        }
        return ChatColor.translateAlternateColorCodes((char)'&', (String)string);
    }

    @Override
    public String parseString(String string, Player player) {
        return this.parseString(string);
    }

    public static String parseToMiniMessage(String string) {
        Matcher match = hexPattern.matcher(string);
        while (match.find()) {
            String color = string.substring(match.start() + 1, match.end());
            string = string.replace("&" + color, "<reset><" + color + ">");
            match = hexPattern.matcher(string);
        }
        return string;
    }
}

