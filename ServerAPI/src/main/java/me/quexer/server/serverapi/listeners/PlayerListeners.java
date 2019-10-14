package me.quexer.server.serverapi.listeners;

import me.quexer.api.quexerapi.event.EventManager;
import me.quexer.server.serverapi.ServerAPI;
import me.quexer.server.serverplugin.models.BackendPlayer;
import me.quexer.server.spigotserverperms.models.GroupPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.help.HelpTopic;

public class PlayerListeners {

    private ServerAPI plugin;

    private EventManager.EventListener<PlayerJoinEvent> joinEvent;
    private EventManager.EventListener<PlayerQuitEvent> quitEvent;
    private EventManager.EventListener<AsyncPlayerChatEvent> chatEvent;

    public PlayerListeners(ServerAPI plugin) {
        this.plugin = plugin;

        joinEvent = event -> {
            if (plugin.getBackendManager().getBackendPlayerCache().containsKey(event.getPlayer().getUniqueId().toString())) {
                plugin.getBackendManager().getBackendPlayerCache().remove(event.getPlayer().getUniqueId().toString());
            }
            event.setJoinMessage(null);
            BackendPlayer backendPlayer = plugin.getBackendManager().getPlayer(event.getPlayer().getUniqueId().toString());
            GroupPlayer groupPlayer = plugin.getGroupManager().getPlayer(event.getPlayer().getUniqueId().toString());
                plugin.getTablistManager().setTablist(event.getPlayer(), groupPlayer);


        };

        quitEvent = event -> {
            event.setQuitMessage(null);
            BackendPlayer backendPlayer = plugin.getBackendManager().getPlayer(event.getPlayer().getUniqueId().toString());
            plugin.getBackendManager().savePlayer(backendPlayer);
            plugin.getBackendManager().getBackendPlayerCache().remove(event.getPlayer().getUniqueId().toString());

        };


        chatEvent = event -> {
            event.setFormat(event.getPlayer().getDisplayName() + " §8➜ §7" + event.getMessage());
            String msg = event.getMessage().replace("%", "%%");
            event.setMessage(msg);
            if (!event.isCancelled()) {
                if(msg.startsWith("/")) {
                    String cmd = event.getMessage().split(" ")[0];
                    HelpTopic topic = Bukkit.getServer().getHelpMap().getHelpTopic(cmd);
                    if (topic == null) {
                        event.getPlayer().sendMessage(plugin.getPrefix() + "§7Dieser Befehl existiert nicht, versuche §e/help §7für eine Übersicht");
                        event.setCancelled(true);
                    }
                }
            }

        };

        plugin.getQuexerAPI().getEventManager().registerEvent(PlayerJoinEvent.class, joinEvent);
        plugin.getQuexerAPI().getEventManager().registerEvent(PlayerQuitEvent.class, quitEvent);
        plugin.getQuexerAPI().getEventManager().registerEvent(AsyncPlayerChatEvent.class, chatEvent);

    }

}
