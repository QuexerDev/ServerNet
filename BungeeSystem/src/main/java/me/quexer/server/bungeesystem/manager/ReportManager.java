package me.quexer.server.bungeesystem.manager;

import me.quexer.server.bungeesystem.BungeeSystem;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class ReportManager {

    private BungeeSystem plugin;

    public ReportManager(BungeeSystem plugin) {
        this.plugin = plugin;
    }

    public void report(String targetUUID, String senderUUID, String reason) {
        ProxiedPlayer senderPlayer = plugin.getProxy().getPlayer(UUID.fromString(senderUUID));

    }

}
