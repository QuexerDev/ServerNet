package me.quexer.server.bungeeserverperms.manager;

import com.mongodb.client.model.Filters;
import me.quexer.server.bungeeserverperms.ServerPerms;
import me.quexer.server.bungeeserverperms.models.Group;
import me.quexer.server.bungeeserverperms.models.GroupPlayer;
import org.bson.Document;

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


                GroupPlayer groupPlayer = new GroupPlayer();
                groupPlayer.setExpires(-1);
                groupPlayer.setGroup(plugin.getGroupManager().getLowestGroup());
                groupPlayer.setUuid(uuid.toString());

                consumer.accept(groupPlayer);

                document = plugin.getGson().fromJson(plugin.getGson().toJson(groupPlayer), Document.class);
                plugin.getGroupManager().getGroupPlayerCache().put(uuid.toString(), groupPlayer);
                plugin.getMongoManager().getCollection("serverperms").insertOne(document, (aVoid, throwable1) -> {

                });
                return;
            } else {
                GroupPlayer groupPlayer = plugin.getGson().fromJson(document.toJson(), GroupPlayer.class);
                consumer.accept(groupPlayer);
                plugin.getGroupManager().getGroupPlayerCache().put(uuid.toString(), groupPlayer);
                return;
            }
        });
    }

    public void saveToDB(GroupPlayer groupPlayer) {
        Document document = plugin.getGson().fromJson(plugin.getGson().toJson(groupPlayer), Document.class);
        plugin.getMongoManager().getCollection("serverperms")
                .replaceOne(Filters.eq("uuid", groupPlayer.getUuid()), document, (result, t) -> {
                });
    }

}
