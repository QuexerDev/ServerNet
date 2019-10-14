package me.quexer.server.serverapi.utils;

import me.quexer.api.quexerapi.api.NickAPI;
import me.quexer.server.serverapi.ServerAPI;
import me.quexer.server.spigotserverperms.models.Group;
import me.quexer.server.spigotserverperms.models.GroupPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class TablistManager {

    private ServerAPI plugin;

    private org.bukkit.scoreboard.Scoreboard board;
    private Objective obj;
    private net.minecraft.server.v1_8_R3.Scoreboard sb;
    private HashMap<Integer, Team> groupTeamHashMap = new HashMap<>();

    public TablistManager(ServerAPI plugin) {
        this.plugin = plugin;

        board = Bukkit.getScoreboardManager().getNewScoreboard();
        obj = board.registerNewObjective("aaaaa", "bbbbb");
        for (int i = 0; i < plugin.getGroupManager().getGroups().size(); i++) {
            Group group = plugin.getGroupManager().getGroups().get(i);
            Team team = board.registerNewTeam("" + group.getTabID() + ChatColor.stripColor(group.getPrefix().split(" ")[0]).toUpperCase().toString());
            System.out.println(team.getName());
            System.out.println("" + group.getTabID() + group.toString());
            team.setPrefix(group.getPrefix());
            groupTeamHashMap.put(group.getLevelID(), team);
        }
    }

    public void setTablist(Player player, GroupPlayer backendPlayer) {
        if(!NickAPI.hasNick(player)) {
            groupTeamHashMap.get(backendPlayer.getGroup().getLevelID()).addPlayer(player);

            player.setPlayerListName(backendPlayer.getGroup().getPrefix() + player.getName());
            player.setDisplayName(backendPlayer.getGroup().getPrefix() + player.getName());
        } else {
            groupTeamHashMap.get(plugin.getGroupManager().getLowestGroup().getLevelID()).addPlayer(player);

            player.setPlayerListName(plugin.getGroupManager().getLowestGroup().getPrefix() + player.getName());
            player.setDisplayName(plugin.getGroupManager().getLowestGroup().getPrefix() + player.getName());
        }

        Bukkit.getOnlinePlayers().forEach(o -> {
            o.setScoreboard(board);
        });
    }

}
