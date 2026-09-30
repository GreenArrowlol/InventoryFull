/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.TabCompleter
 */
package me.dave.inventoryfull.command;

import java.util.ArrayList;
import java.util.List;
import me.dave.inventoryfull.InventoryFull;
import me.dave.inventoryfull.libraries.chatcolor.ChatColorHandler;
import me.dave.inventoryfull.utils.Updater;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MainCmd
implements CommandExecutor,
TabCompleter {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "toggle": {
                    if (!(sender instanceof Player)) {
                        ChatColorHandler.sendMessage(sender, "&#ff6969Only players can toggle inventory full alerts.");
                        return true;
                    }
                    if (!sender.hasPermission("inventoryfull.toggle")) {
                        ChatColorHandler.sendMessage(sender, InventoryFull.getConfigManager().getMessage("no-permissions"));
                        return true;
                    }
                    boolean enabled = InventoryFull.toggleAlerts((Player)sender);
                    ChatColorHandler.sendMessage(sender, InventoryFull.getConfigManager().getMessage(enabled ? "toggle-on" : "toggle-off"));
                    return true;
                }
                case "reload": {
                    if (!sender.hasPermission("inventoryfull.reload")) {
                        ChatColorHandler.sendMessage(sender, InventoryFull.getConfigManager().getMessage("no-permissions"));
                        return true;
                    }
                    InventoryFull.getConfigManager().reloadConfig();
                    ChatColorHandler.sendMessage(sender, InventoryFull.getConfigManager().getMessage("reload"));
                    return true;
                }
                case "update": {
                    if (!sender.hasPermission("inventoryfull.update")) {
                        ChatColorHandler.sendMessage(sender, InventoryFull.getConfigManager().getMessage("no-permissions"));
                        return true;
                    }
                    Updater updater = InventoryFull.getUpdater();
                    if (updater.isAlreadyDownloaded() || !updater.isUpdateAvailable()) {
                        ChatColorHandler.sendMessage(sender, "&#ff6969It looks like there is no new update available!");
                        return true;
                    }
                    updater.downloadUpdate().thenAccept(success -> {
                        if (success.booleanValue()) {
                            ChatColorHandler.sendMessage(sender, "&#b7faa2Successfully updated InventoryFull, restart the server to apply changes!");
                        } else {
                            ChatColorHandler.sendMessage(sender, "&#ff6969Failed to update InventoryFull!");
                        }
                    });
                    return true;
                }
            }
        }
        ChatColorHandler.sendMessage(sender, "&#a8e1ffYou are currently running InventoryFull version &#58b1e0" + InventoryFull.getInstance().getDescription().getVersion());
        return true;
    }

    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        ArrayList<String> tabComplete = new ArrayList<String>();
        ArrayList<String> wordCompletion = new ArrayList<String>();
        boolean wordCompletionSuccess = false;
        if (args.length == 1) {
            if (sender instanceof Player && sender.hasPermission("inventoryfull.toggle")) {
                tabComplete.add("toggle");
            }
            if (sender.hasPermission("inventoryfull.reload")) {
                tabComplete.add("reload");
            }
            if (sender.hasPermission("inventoryfull.update")) {
                tabComplete.add("update");
            }
        }
        for (String currTab : tabComplete) {
            int currArg;
            if (!currTab.startsWith(args[currArg = args.length - 1])) continue;
            wordCompletion.add(currTab);
            wordCompletionSuccess = true;
        }
        return wordCompletionSuccess ? wordCompletion : tabComplete;
    }
}

