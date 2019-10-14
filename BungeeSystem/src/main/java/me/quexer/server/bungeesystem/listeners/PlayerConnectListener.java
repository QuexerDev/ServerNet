package me.quexer.server.bungeesystem.listeners;

import me.quexer.server.bungeesystem.BungeeSystem;
import me.quexer.server.bungeesystem.misc.uuid.UUIDFetcher;
import me.quexer.server.bungeesystem.models.BackendPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class PlayerConnectListener implements Listener {

    private BungeeSystem plugin;

    public PlayerConnectListener(BungeeSystem plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onLogin(LoginEvent event) {
        BackendPlayer backendPlayer = plugin.getBackendManager().getPlayer(event.getConnection().getUniqueId().toString());
        if(backendPlayer.getBanPlayer().isPunished() == false) {
            backendPlayer.getDate().setLastLogin(System.currentTimeMillis());

        } else {
            if (System.currentTimeMillis() < backendPlayer.getBanPlayer().getEnd() || backendPlayer.getBanPlayer().getEnd() == -1) {
                event.setCancelled(true);
                event.setCancelReason("§e§lServer§7§l.§e§lnet\n\n" +
                        "§cDu wurdest vom §eNetzwerk §cgebannt§8!\n" +
                        "\n" +
                        "§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\n" +
                        "§7Grund§8: §e" + backendPlayer.getBanPlayer().getReason() + "\n" +
                        "§7Gebannt von§8: §e" + plugin.getGroupManager().getPlayer(backendPlayer.getBanPlayer().getPunished_from()).getGroup().getPrefix()+ UUIDFetcher.getName(UUID.fromString(backendPlayer.getBanPlayer().getPunished_from())) + "\n" +
                        "§7Gebannt bis§8: §e" + plugin.getDate(backendPlayer.getBanPlayer().getEnd()) + "\n" +
                        "§7BanPoints§8: §e" + backendPlayer.getBanPlayer().getBanPoints() + "\n" +
                        "§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\n" +
                        "\n" +
                        "§7Du kannst einen §eEntbannungsantrag §7im Forum stellen!");
            } else
                plugin.getBanManager().unBanConsole(event.getConnection().getUniqueId().toString());
        }
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        BackendPlayer backendPlayer = plugin.getBackendManager().getPlayer(event.getPlayer().getUniqueId().toString());
        backendPlayer.getDate().setLastOffline(System.currentTimeMillis());
        plugin.getBackendManager().savePlayer(backendPlayer);
        System.out.println("Stored in Database");
        plugin.getBackendManager().getBackendPlayerCache().remove(event.getPlayer().getUniqueId().toString());

    }

}
