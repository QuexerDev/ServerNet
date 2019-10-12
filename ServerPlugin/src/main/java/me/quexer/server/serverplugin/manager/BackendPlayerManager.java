package me.quexer.server.serverplugin.manager;

import com.mongodb.client.model.Filters;
import me.quexer.server.serverplugin.ServerPlugin;
import me.quexer.server.serverplugin.models.BackendPlayer;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.UUID;
import java.util.function.Consumer;

public class BackendPlayerManager implements Listener {

    private ServerPlugin plugin;

    public BackendPlayerManager(ServerPlugin plugin) {
        this.plugin = plugin;
    }

    public void getFromDB(UUID uuid, Consumer<BackendPlayer> consumer) {
        plugin.getMongoManager().getCollection("spigotbackend").find(Filters.eq("uuid", uuid.toString())).first((document, throwable) -> {
            if (document == null) {


                BackendPlayer backendPlayer = new BackendPlayer(uuid.toString());


                consumer.accept(backendPlayer);

                document = plugin.getQuexerAPI().getGson().fromJson(plugin.getQuexerAPI().getGson().toJson(backendPlayer), Document.class);
                plugin.getBackendManager().getBackendPlayerCache().put(uuid.toString(), backendPlayer);
                plugin.getMongoManager().getCollection("spigotbackend").insertOne(document, (aVoid, throwable1) -> {

                });
                return;
            } else {
                BackendPlayer backendPlayer = plugin.getQuexerAPI().getGson().fromJson(document.toJson(), BackendPlayer.class);
                consumer.accept(backendPlayer);
                plugin.getBackendManager().getBackendPlayerCache().put(uuid.toString(), backendPlayer);
                return;
            }
        });
    }

    public void saveToDB(BackendPlayer backendPlayer) {
        Document document = plugin.getQuexerAPI().getGson().fromJson(plugin.getQuexerAPI().getGson().toJson(backendPlayer), Document.class);
        plugin.getMongoManager().getCollection("spigotbackend")
                .replaceOne(Filters.eq("uuid", backendPlayer.getUuid()), document, (result, t) -> {
                });
    }

}
