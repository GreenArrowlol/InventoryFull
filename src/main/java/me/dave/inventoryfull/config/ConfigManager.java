/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.configuration.ConfigurationSection
 *  org.bukkit.configuration.file.FileConfiguration
 */
package me.dave.inventoryfull.config;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import me.dave.inventoryfull.InventoryFull;
import me.dave.inventoryfull.utils.MessageType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private MessageType messageType;
    private int period;
    private final ConcurrentHashMap<String, String> messages = new ConcurrentHashMap<String, String>();
    // defaults for the new keys so old configs still work
    private static final Map<String, String> DEFAULT_MESSAGES = Map.of(
            "toggle-on", "&#b7faa2Inventory full alerts &#66b04fenabled",
            "toggle-off", "&#ff6969Inventory full alerts disabled");

    public ConfigManager() {
        InventoryFull.getInstance().saveDefaultConfig();
    }

    public void reloadConfig() {
        InventoryFull.getInstance().reloadConfig();
        FileConfiguration config = InventoryFull.getInstance().getConfig();
        try {
            this.messageType = MessageType.valueOf(config.getString("message-type", "message").toUpperCase());
        }
        catch (IllegalArgumentException e) {
            InventoryFull.getInstance().getLogger().severe("'message-type' has to be one of the following: " + Arrays.toString((Object[])MessageType.values()));
            this.messageType = MessageType.MESSAGE;
        }
        this.period = config.getInt("period", 20);
        this.messages.clear();
        ConfigurationSection messagesSection = config.getConfigurationSection("messages");
        if (messagesSection != null) {
            messagesSection.getValues(false).forEach((key, value) -> this.messages.put((String)key, (String)value));
        }
    }

    public MessageType getMessageType() {
        return this.messageType;
    }

    public int getPeriod() {
        return this.period;
    }

    public String getMessage(String messageName) {
        String output = this.messages.getOrDefault(messageName, DEFAULT_MESSAGES.getOrDefault(messageName, ""));
        if (this.messages.containsKey("prefix")) {
            return output.replaceAll("%prefix%", this.messages.get("prefix"));
        }
        return output;
    }
}

