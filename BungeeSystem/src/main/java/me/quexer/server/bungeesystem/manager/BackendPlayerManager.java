package me.quexer.server.bungeesystem.manager;

import com.mongodb.client.model.Filters;
import me.quexer.server.bungeesystem.BungeeSystem;
import me.quexer.server.bungeesystem.models.BackendPlayer;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public class BackendPlayerManager {

    private BungeeSystem plugin;

    public BackendPlayerManager(BungeeSystem plugin) {
        this.plugin = plugin;
    }

    public void getFromDB(UUID uuid, Consumer<BackendPlayer> consumer) {
        plugin.getMongoManager().getCollection("bungeebackend").find(Filters.eq("uuid", uuid.toString())).first((document, throwable) -> {
            if (document == null) {

                BackendPlayer backendPlayer = new BackendPlayer(uuid.toString());

                consumer.accept(backendPlayer);

                document = plugin.getGson().fromJson(plugin.getGson().toJson(backendPlayer), Document.class);
                    plugin.getBackendManager().getBackendPlayerCache().put(uuid.toString(), backendPlayer);
                plugin.getMongoManager().getCollection("bungeebackend").insertOne(document, (aVoid, throwable1) -> {

                });

                return;
            } else {
                BackendPlayer user = plugin.getGson().fromJson(document.toJson(), BackendPlayer.class);
                consumer.accept(user);
                    plugin.getBackendManager().getBackendPlayerCache().put(uuid.toString(), user);
                return;
            }
        });
    }






    public BackendPlayer saveToDB(BackendPlayer backendPlayer) {
        Document document = plugin.getGson().fromJson(plugin.getGson().toJson(backendPlayer), Document.class);
        plugin.getMongoManager().getCollection("bungeebackend")
                .replaceOne(Filters.eq("uuid", backendPlayer.getUuid()), document, (result, t) -> {
                });
        return backendPlayer;
    }

}


