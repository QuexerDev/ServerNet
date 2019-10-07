package me.quexer.server.spigotserverperms.listeners;

import me.quexer.api.quexerapi.event.EventManager;
import me.quexer.server.spigotserverperms.ServerPerms;
import me.quexer.server.spigotserverperms.models.GroupPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MainListeners {

    private ServerPerms plugin;

    public MainListeners(ServerPerms plugin) {
        this.plugin = plugin;

        initJoinListener();
        initQuitListener();
    }

    private void initQuitListener() {
        plugin.getQuexerAPI().getEventManager().registerEvent(PlayerQuitEvent.class, (EventManager.EventListener<PlayerQuitEvent>) event -> {
            if (plugin.getGroupManager().getGroupPlayerCache().containsKey(event.getPlayer().getUniqueId().toString())) {
                plugin.getGroupManager().getGroupPlayerCache().remove(event.getPlayer().getUniqueId().toString());
                System.out.println("Delete Group from cache!");
            }
        });
    }

    private void initJoinListener() {
        plugin.getQuexerAPI().getEventManager().registerEvent(PlayerJoinEvent.class, (EventManager.EventListener<PlayerJoinEvent>) event -> {
            GroupPlayer groupPlayer = plugin.getGroupManager().getPlayer(event.getPlayer().getUniqueId().toString());
            event.getPlayer().sendMessage(plugin.getPrefix()+"§7Deine Gruppe§8: "+groupPlayer.getGroup().getFullName());
        });


    }




}
