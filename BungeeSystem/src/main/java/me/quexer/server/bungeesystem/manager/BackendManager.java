package me.quexer.server.bungeesystem.manager;

import me.quexer.server.bungeesystem.BungeeSystem;
import me.quexer.server.bungeesystem.models.BackendPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class BackendManager {

    private BungeeSystem plugin;

    private HashMap<String, BackendPlayer> backendPlayerCache;

    public BackendManager(BungeeSystem plugin) {
        this.plugin = plugin;

        backendPlayerCache = new HashMap<>();
    }




    public BackendPlayer getPlayer(String uuid) {
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


    public BackendPlayer savePlayer(BackendPlayer backendPlayer) {
        CompletableFuture<BackendPlayer> completableFuture = new CompletableFuture<>();
        completableFuture.complete(plugin.getBackendPlayerManager().saveToDB(backendPlayer));
        backendPlayerCache.put(backendPlayer.getUuid().toLowerCase(), backendPlayer);
        try {
            return completableFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public HashMap<String, BackendPlayer> getBackendPlayerCache() {
        return backendPlayerCache;
    }
}
