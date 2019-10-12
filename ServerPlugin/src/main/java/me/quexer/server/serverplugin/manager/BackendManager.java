package me.quexer.server.serverplugin.manager;

import me.quexer.api.quexerapi.event.EventManager;
import me.quexer.server.serverplugin.ServerPlugin;
import me.quexer.server.serverplugin.models.BackendPlayer;
import me.quexer.server.spigotserverperms.models.GroupPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class BackendManager {

    private ServerPlugin plugin;

    private HashMap<String, BackendPlayer> backendPlayerCache;

    public BackendManager(ServerPlugin plugin) {
        this.plugin = plugin;

        backendPlayerCache = new HashMap<>();

    }



    public void savePlayer(BackendPlayer backendPlayer) {
        plugin.getBackendPlayerManager().saveToDB(backendPlayer);
    }

    public BackendPlayer getPlayer(String uuid) {

        //TODO: Beim Join den Spieler ausm Cache removen!!!!!

        if (backendPlayerCache.containsKey(uuid)) {
            System.out.println("Get From META");
            return backendPlayerCache.get(uuid);
        } else {
            try {
                CompletableFuture<BackendPlayer> completableFuture = new CompletableFuture<>();
                plugin.getBackendPlayerManager().getFromDB(UUID.fromString(uuid), backendPlayer -> {
                    completableFuture.complete(backendPlayer);
                    System.out.println("Get From Database");
                });
                return completableFuture.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public HashMap<String, BackendPlayer> getBackendPlayerCache() {
        return backendPlayerCache;
    }
}
