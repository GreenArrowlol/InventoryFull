/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.md_5.bungee.api.ChatColor
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.PluginManager
 */
package me.dave.inventoryfull.libraries.chatcolor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.dave.inventoryfull.libraries.chatcolor.messengers.LegacyMessenger;
import me.dave.inventoryfull.libraries.chatcolor.messengers.Messenger;
import me.dave.inventoryfull.libraries.chatcolor.messengers.MiniMessageMessenger;
import me.dave.inventoryfull.libraries.chatcolor.parsers.Parsers;
import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.HexParser;
import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.LegacyCharParser;
import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.MiniMessageParser;
import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.Parser;
import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.PlaceholderAPIParser;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ChatColorHandler {
    private static final String LOGGER_PREFIX = "[ChatColorHandler] ";
    private static final Pattern HEX_PATTERN = Pattern.compile("&#[a-fA-F0-9]{6}");
    private static Messenger messenger;

    public static void sendMessage(@NotNull CommandSender recipient, @Nullable String message) {
        messenger.sendMessage(recipient, message);
    }

    public static void sendMessage(@NotNull CommandSender recipient, String ... messages) {
        messenger.sendMessage(recipient, messages);
    }

    public static void sendMessage(CommandSender[] recipients, @Nullable String message) {
        messenger.sendMessage(recipients, message);
    }

    public static void sendMessage(CommandSender[] recipients, String ... messages) {
        messenger.sendMessage(recipients, messages);
    }

    public static void broadcastMessage(@Nullable String message) {
        messenger.broadcastMessage(message);
    }

    public static void broadcastMessage(String ... messages) {
        messenger.broadcastMessage(messages);
    }

    public static void sendActionBarMessage(@NotNull Player player, @Nullable String message) {
        messenger.sendActionBarMessage(player, message);
    }

    public static void sendActionBarMessage(@NotNull Player[] players, @Nullable String message) {
        messenger.sendActionBarMessage(players, message);
    }

    public static String translateAlternateColorCodes(@Nullable String string) {
        return ChatColorHandler.translateAlternateColorCodes(string, null, null);
    }

    public static String translateAlternateColorCodes(@Nullable String string, List<Class<? extends Parser>> ignoredParsers) {
        if (string == null || string.isBlank()) {
            return "";
        }
        return Parsers.parseString(string, null, ignoredParsers);
    }

    public static String translateAlternateColorCodes(@Nullable String string, Player player) {
        return ChatColorHandler.translateAlternateColorCodes(string, player, null);
    }

    public static String translateAlternateColorCodes(@Nullable String string, Player player, List<Class<? extends Parser>> ignoredParsers) {
        if (string == null || string.isBlank()) {
            return "";
        }
        return Parsers.parseString(string, player, ignoredParsers);
    }

    public static List<String> translateAlternateColorCodes(@Nullable List<String> strings) {
        return ChatColorHandler.translateAlternateColorCodes(strings, null, null);
    }

    public static List<String> translateAlternateColorCodes(@Nullable List<String> strings, List<Class<? extends Parser>> ignoredParsers) {
        return ChatColorHandler.translateAlternateColorCodes(strings, null, ignoredParsers);
    }

    public static List<String> translateAlternateColorCodes(@Nullable List<String> strings, Player player) {
        return ChatColorHandler.translateAlternateColorCodes(strings, player, null);
    }

    public static List<String> translateAlternateColorCodes(@Nullable List<String> strings, Player player, List<Class<? extends Parser>> ignoredParsers) {
        if (strings == null || strings.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<String> outputList = new ArrayList<String>();
        for (String string : strings) {
            outputList.add(ChatColorHandler.translateAlternateColorCodes(string, player));
        }
        return outputList;
    }

    public static String stripColor(@Nullable String string) {
        if (string == null || string.isBlank()) {
            return "";
        }
        string = string.replaceAll("\u00a7", "&");
        Matcher match = HEX_PATTERN.matcher(string);
        while (match.find()) {
            String color = string.substring(match.start() + 1, match.end());
            string = string.replace("&" + color, "" + ChatColor.of((String)color));
            match = HEX_PATTERN.matcher(string);
        }
        return ChatColor.stripColor((String)ChatColor.translateAlternateColorCodes((char)'&', (String)string));
    }

    public static List<String> stripColor(@Nullable List<String> strings) {
        if (strings == null || strings.isEmpty()) {
            return Collections.emptyList();
        }
        ArrayList<String> outputList = new ArrayList<String>();
        for (String string : strings) {
            outputList.add(ChatColorHandler.stripColor(string));
        }
        return outputList;
    }

    static {
        Parsers.register(new LegacyCharParser(), 100);
        Parsers.register(new HexParser(), 70);
        try {
            // PaperConfig class doesnt exist anymore, check for minimessage instead
            Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            Parsers.register(new MiniMessageParser(), 80);
            messenger = new MiniMessageMessenger();
            Bukkit.getLogger().info("[ChatColorHandler] Server running on PaperMC (or fork). MiniMessage support enabled.");
        }
        catch (ClassNotFoundException ignored) {
            messenger = new LegacyMessenger();
        }
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        if (pluginManager.getPlugin("PlaceholderAPI") != null && pluginManager.isPluginEnabled("PlaceholderAPI")) {
            Parsers.register(new PlaceholderAPIParser(), 90);
            Bukkit.getLogger().info("[ChatColorHandler] Found plugin \"PlaceholderAPI\". PlaceholderAPI support enabled.");
        }
    }
}

