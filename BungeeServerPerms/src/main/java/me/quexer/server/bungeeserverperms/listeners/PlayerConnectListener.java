package me.quexer.server.bungeeserverperms.listeners;

import me.quexer.server.bungeeserverperms.ServerPerms;
import me.quexer.server.bungeeserverperms.models.GroupPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PlayerConnectListener implements Listener {

    private ServerPerms plugin;

    public PlayerConnectListener(ServerPerms plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onLogin(LoginEvent event) {
        GroupPlayer groupPlayer = plugin.getGroupManager().getPlayer(event.getConnection().getUniqueId().toString());

        if(!(System.currentTimeMillis() < groupPlayer.getExpires() || groupPlayer.getExpires() == -1)) {
            plugin.getGroupManager().setGroup(groupPlayer.getUuid(), plugin.getGroupManager().getLowestGroup(), -1);
        }

    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        GroupPlayer groupPlayer = plugin.getGroupManager().getPlayer(event.getPlayer().getUniqueId().toString());

        plugin.getGroupManager().savePlayer(groupPlayer);
        System.out.println("Stored in Database");
        plugin.getGroupManager().getGroupPlayerCache().remove(event.getPlayer().getUniqueId().toString());

    }

}
