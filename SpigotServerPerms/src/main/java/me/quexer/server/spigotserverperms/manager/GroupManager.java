package me.quexer.server.spigotserverperms.manager;

import me.quexer.server.spigotserverperms.ServerPerms;
import me.quexer.server.spigotserverperms.models.Group;
import me.quexer.server.spigotserverperms.models.GroupPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class GroupManager {

    private ServerPerms plugin;
    private List<Group> groups;

    private HashMap<String, GroupPlayer> groupPlayerCache;

    public GroupManager(ServerPerms plugin) {
        this.plugin = plugin;

        groupPlayerCache = new HashMap<>();

        initGroups();
    }

    private void initGroups() {
        groups = new ArrayList<>();
        groups.add(new Group("§4Administrator", "§4Admin §8| §7", "§4", 13, 1));
        groups.add(new Group("§bSrDeveloper", "§bSrDev §8| §7", "§b", 12, 2));
        groups.add(new Group("§cSrModerator", "§cSrMod §8| §7", "§c", 11, 3));
        groups.add(new Group("§bDeveloper", "§bDev §8| §7", "§b", 10, 4));
        groups.add(new Group("§cModerator", "§cMod §8| §7", "§c", 9, 5));
        groups.add(new Group("§2Builder", "§2Builder §8| §7", "§2", 8, 6));
        groups.add(new Group("§bContent", "§bContent §8| §7", "§b", 7, 7));
        groups.add(new Group("§9Supporter", "§9Sup §8| §7", "§9", 6, 8));
        groups.add(new Group("§5YouTuber", "§5YouTube §8| §7", "§5", 5, 9));
        groups.add(new Group("§3Master", "§3Master §8| §7", "§3", 4, 10));
        groups.add(new Group("§eVIP", "§eVIP §7| §e", "§7", 3, 11));
        groups.add(new Group("§6Premium", "§ePremium §7| §7", "§6", 2, 12));
        groups.add(new Group("§aSpieler", "§aSpieler §8| §7", "§a", 1, 13));

    }

    public Group getLowestGroup() {
        return groups.stream().filter(backendGroup -> backendGroup.getLevelID() == 1).collect(Collectors.toList()).get(0);
    }

    public GroupPlayer getPlayer(String uuid) {
        if (groupPlayerCache.containsKey(uuid)) {
            System.out.println("Get Group From META");
            return groupPlayerCache.get(uuid);
        } else {
            try {
                CompletableFuture<GroupPlayer> completableFuture = new CompletableFuture<>();
                plugin.getGroupPlayerManager().getFromDB(UUID.fromString(uuid), groupPlayer -> {
                    completableFuture.complete(groupPlayer);
                    System.out.println("Get Group From Database");
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

    public List<Group> getGroups() {
        return groups;
    }

    public HashMap<String, GroupPlayer> getGroupPlayerCache() {
        return groupPlayerCache;
    }
}
