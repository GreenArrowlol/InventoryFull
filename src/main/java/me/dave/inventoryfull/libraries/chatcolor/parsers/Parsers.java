/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 */
package me.dave.inventoryfull.libraries.chatcolor.parsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import me.dave.inventoryfull.libraries.chatcolor.parsers.custom.Parser;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class Parsers {
    private static Map<Parser, Integer> parsers = new HashMap<Parser, Integer>();

    public static void register(Parser parser, int priority) {
        parsers.put(parser, priority);
        parsers = Parsers.sortByValue(parsers);
    }

    public static String parseString(String string, @Nullable Player player, @Nullable List<Class<? extends Parser>> ignoredParsers) {
        for (Parser parser : parsers.keySet()) {
            if (ignoredParsers != null && ignoredParsers.contains(parser.getClass())) continue;
            string = parser.parseString(string, player);
        }
        return string;
    }

    private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        ArrayList<Map.Entry<K, V>> list = new ArrayList<Map.Entry<K, V>>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        Collections.reverse(list);
        LinkedHashMap result = new LinkedHashMap();
        for (Map.Entry entry : list) {
            result.put(entry.getKey(), (Comparable)entry.getValue());
        }
        return result;
    }
}

