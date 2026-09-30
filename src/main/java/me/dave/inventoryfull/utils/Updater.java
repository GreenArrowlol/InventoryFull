/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  org.bukkit.Bukkit
 *  org.bukkit.plugin.Plugin
 */
package me.dave.inventoryfull.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import me.dave.inventoryfull.InventoryFull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class Updater {
    private final ScheduledExecutorService updateExecutor = Executors.newScheduledThreadPool(1);
    private final Logger logger;
    private final String modrinthProjectSlug;
    private final String currentVersion;
    private final String jarName;
    private final String downloadCommand;
    private boolean enabled;
    private String latestVersion;
    private String downloadUrl;
    private boolean updateAvailable = false;
    private boolean ready = false;
    private boolean alreadyDownloaded = false;

    public Updater(Plugin plugin, String modrinthProjectSlug, String downloadCommand) {
        this.logger = plugin.getLogger();
        this.modrinthProjectSlug = modrinthProjectSlug;
        String currentVersion = plugin.getDescription().getVersion();
        this.currentVersion = currentVersion.contains("-") ? currentVersion.split("-")[0] : currentVersion;
        this.jarName = plugin.getDescription().getName();
        this.downloadCommand = downloadCommand;
        this.updateExecutor.scheduleAtFixedRate(() -> {
            try {
                this.check();
            }
            catch (Exception e) {
                this.logger.info("Unable to check for update: " + e.getMessage());
            }
        }, 2L, 600L, TimeUnit.SECONDS);
    }

    public void check() throws IOException {
        if (!this.enabled) {
            return;
        }
        URL url = new URL("https://api.modrinth.com/v2/project/" + this.modrinthProjectSlug + "/version?featured=true");
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.addRequestProperty("User-Agent", this.jarName + "/" + this.currentVersion);
        if (connection.getResponseCode() != 200) {
            throw new IllegalStateException("Response code was " + connection.getResponseCode());
        }
        InputStream inputStream = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(inputStream);
        JsonArray versionsJson = JsonParser.parseReader((Reader)reader).getAsJsonArray();
        JsonObject currVersionJson = versionsJson.get(0).getAsJsonObject();
        this.latestVersion = currVersionJson.get("version_number").getAsString();
        this.downloadUrl = currVersionJson.get("files").getAsJsonArray().get(0).getAsJsonObject().get("url").getAsString();
        if (this.latestVersion.contains("-")) {
            this.latestVersion = this.latestVersion.split("-")[0];
        }
        if (this.latestVersion.isEmpty()) {
            throw new IllegalStateException("Latest version is empty!");
        }
        String[] parts = this.latestVersion.split("\\.");
        String[] currParts = this.currentVersion.split("\\.");
        int i = 0;
        for (String part : parts) {
            int currVersion;
            if (i >= currParts.length) break;
            int newVersion = Integer.parseInt(part);
            if (newVersion > (currVersion = Integer.parseInt(currParts[i]))) {
                if (i != 0) {
                    int currVersionLast;
                    int newVersionLast = Integer.parseInt(parts[i - 1]);
                    if (newVersionLast >= (currVersionLast = Integer.parseInt(currParts[i - 1]))) {
                        this.updateAvailable = true;
                        break;
                    }
                } else {
                    this.updateAvailable = true;
                    break;
                }
            }
            ++i;
        }
        if (this.updateAvailable && !this.ready) {
            this.logger.info("An update is available! (" + this.latestVersion + ") Do /" + this.downloadCommand + " to download it!");
        } else if (!this.ready) {
            this.logger.info("You are up to date! (" + this.latestVersion + ")");
        }
        this.ready = true;
    }

    public boolean isUpdateAvailable() {
        return this.enabled && this.updateAvailable;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAlreadyDownloaded() {
        return this.alreadyDownloaded;
    }

    public CompletableFuture<Boolean> downloadUpdate() {
        if (!this.isEnabled()) {
            this.logger.warning("Updater is disabled");
            return CompletableFuture.completedFuture(false);
        }
        if (!this.isUpdateAvailable()) {
            this.logger.warning("No update is available!");
            return CompletableFuture.completedFuture(false);
        }
        if (this.isAlreadyDownloaded()) {
            this.logger.warning("The update has already been downloaded!");
            return CompletableFuture.completedFuture(false);
        }
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<Boolean>();
        InventoryFull.getMorePaperLib().scheduling().asyncScheduler().run(() -> {
            try {
                URL url = new URL(this.downloadUrl);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.addRequestProperty("User-Agent", this.jarName + "/" + this.currentVersion);
                connection.setInstanceFollowRedirects(true);
                HttpURLConnection.setFollowRedirects(true);
                if (connection.getResponseCode() != 200) {
                    throw new IllegalStateException("Response code was " + connection.getResponseCode());
                }
                ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
                File out = new File(this.getUpdateFolder(), this.jarName + "-" + this.latestVersion + ".jar");
                this.logger.info(out.getAbsolutePath());
                FileOutputStream fos = new FileOutputStream(out);
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
                fos.close();
                this.updateAvailable = false;
                this.alreadyDownloaded = true;
                completableFuture.complete(true);
            }
            catch (Exception e) {
                e.printStackTrace();
                completableFuture.complete(false);
            }
        });
        return completableFuture;
    }

    public void shutdown() {
        this.updateExecutor.shutdown();
    }

    private File getUpdateFolder() {
        File updateDir = new File(InventoryFull.getInstance().getDataFolder().getParentFile(), Bukkit.getUpdateFolder());
        if (!updateDir.exists()) {
            updateDir.mkdir();
        }
        return updateDir;
    }
}

