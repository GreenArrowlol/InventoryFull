package me.dave.inventoryfull;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import me.dave.inventoryfull.command.MainCmd;
import me.dave.inventoryfull.config.ConfigManager;
import me.dave.inventoryfull.libraries.chatcolor.ChatColorHandler;
import space.arim.morepaperlib.MorePaperLib;
import me.dave.inventoryfull.utils.Updater;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public final class InventoryFull
extends JavaPlugin {
    private static MorePaperLib morePaperLib;
    private static Updater updater;
    private static InventoryFull plugin;
    private static ConfigManager configManager;
    private static NamespacedKey alertsKey;

    public void onEnable() {
        plugin = this;
        alertsKey = new NamespacedKey(this, "alerts");
        morePaperLib = new MorePaperLib(this);
        updater = new Updater(this, "inventory-full", "inventoryfull update");
        configManager = new ConfigManager();
        configManager.reloadConfig();
        MainCmd mainCmd = new MainCmd();
        this.getCommand("inventoryfull").setExecutor(mainCmd);
        this.getCommand("inventoryfull").setTabCompleter(mainCmd);
        morePaperLib.scheduling().asyncScheduler().runAtFixedRate(this::heartBeat, Duration.of(1000L, ChronoUnit.MILLIS), Duration.of((long)configManager.getPeriod() * 50L, ChronoUnit.MILLIS));
    }

    public void onDisable() {
        if (morePaperLib != null) {
            morePaperLib.scheduling().cancelGlobalTasks();
            morePaperLib = null;
        }
        if (updater != null) {
            updater.shutdown();
            updater = null;
        }
        configManager = null;
    }

    // timer is async so the inv check + messages run on the players own scheduler
    private void heartBeat() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            morePaperLib.scheduling().entitySpecificScheduler(player).run(() -> {
                if (!player.isOnline() || !hasAlertsEnabled(player)) {
                    return;
                }
                if (player.getInventory().firstEmpty() == -1) {
                    this.sendMessage(player);
                }
            }, null);
        }
    }

    private void sendMessage(Player player) {
        switch (configManager.getMessageType()) {
            case MESSAGE: {
                ChatColorHandler.sendMessage(player, configManager.getMessage("inventory-full"));
                break;
            }
            case ACTION:
            case ACTION_BAR: {
                ChatColorHandler.sendActionBarMessage(player, configManager.getMessage("inventory-full"));
                break;
            }
            case TITLE: {
                String[] titleRaw = configManager.getMessage("inventory-full").split("\\|");
                String title = titleRaw[0];
                String subtitle = titleRaw.length == 2 ? titleRaw[1] : "";
                player.sendTitle(ChatColorHandler.translateAlternateColorCodes(title), ChatColorHandler.translateAlternateColorCodes(subtitle), 10, 70, 20);
            }
        }
    }

    public static boolean hasAlertsEnabled(Player player) {
        Byte value = player.getPersistentDataContainer().get(alertsKey, PersistentDataType.BYTE);
        return value == null || value != 0;
    }

    /**
     * @return the new state (true = alerts on)
     */
    public static boolean toggleAlerts(Player player) {
        boolean enabled = !hasAlertsEnabled(player);
        PersistentDataContainer container = player.getPersistentDataContainer();
        if (enabled) {
            container.remove(alertsKey);
        } else {
            container.set(alertsKey, PersistentDataType.BYTE, (byte)0);
        }
        return enabled;
    }

    public static MorePaperLib getMorePaperLib() {
        return morePaperLib;
    }

    public static Updater getUpdater() {
        return updater;
    }

    public static InventoryFull getInstance() {
        return plugin;
    }

    public static ConfigManager getConfigManager() {
        return configManager;
    }
}
