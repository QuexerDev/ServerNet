package me.quexer.server.spigotserverperms.manager;

import com.mongodb.client.model.Filters;
import me.quexer.server.spigotserverperms.ServerPerms;
import me.quexer.server.spigotserverperms.models.GroupPlayer;
import org.bson.Document;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;

public class GroupPlayerManager {

    private ServerPerms plugin;

    public GroupPlayerManager(ServerPerms plugin) {
        this.plugin = plugin;
    }


    public void getFromDB(UUID uuid, Consumer<GroupPlayer> consumer) {
        plugin.getMongoManager().getCollection("serverperms").find(Filters.eq("uuid", uuid.toString())).first((document, throwable) -> {
            if (document == null) {

               if(Bukkit.getPlayer(uuid) != null) {
                   Bukkit.getPlayer(uuid).kickPlayer(plugin.getError()+"§7Du musst erneut joinen, da die Datenbank nicht richtig syncronisiert ist§8!");
               }
                return;
            } else {
                GroupPlayer groupPlayer = plugin.getQuexerAPI().getGson().fromJson(document.toJson(), GroupPlayer.class);
                consumer.accept(groupPlayer);
                plugin.getGroupManager().getGroupPlayerCache().put(uuid.toString(), groupPlayer);
                return;
            }
        });
    }

}
