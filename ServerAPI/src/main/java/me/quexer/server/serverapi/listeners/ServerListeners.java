package me.quexer.server.serverapi.listeners;

import de.dytanic.cloudnet.bridge.event.bukkit.BukkitSubChannelMessageEvent;
import me.quexer.api.quexerapi.event.EventManager;
import me.quexer.server.serverapi.ServerAPI;
import org.bukkit.Bukkit;

import java.util.UUID;

public class ServerListeners {

    private ServerAPI plugin;

    public ServerListeners(ServerAPI plugin) {
        this.plugin = plugin;
        initListeners();
    }

    private void initListeners() {
        plugin.getQuexerAPI().getEventManager().registerEvent(BukkitSubChannelMessageEvent.class, (EventManager.EventListener<BukkitSubChannelMessageEvent>) event -> {
            if (event.getChannel().equalsIgnoreCase("ban")) {
                if (event.getMessage().equalsIgnoreCase("check")) {
                    plugin.getInventoryManager().checkInv(Bukkit.getPlayer(UUID.fromString(event.getDocument().getString("uuid"))), event.getDocument().getString("target"), event.getDocument());
                }
            }
        });
    }
}
