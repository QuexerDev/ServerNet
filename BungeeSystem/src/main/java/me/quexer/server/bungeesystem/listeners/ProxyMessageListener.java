package me.quexer.server.bungeesystem.listeners;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.proxied.ProxiedSubChannelMessageEvent;
import de.dytanic.cloudnet.lib.utility.document.Document;
import me.quexer.server.bungeeserverperms.models.GroupPlayer;
import me.quexer.server.bungeesystem.BungeeSystem;
import me.quexer.server.bungeesystem.models.BackendPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.UUID;

public class ProxyMessageListener implements Listener {

    private BungeeSystem plugin;

    public ProxyMessageListener(BungeeSystem plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessage(ProxiedSubChannelMessageEvent event) {
        System.out.println(event.getDocument());
        String uuid = event.getDocument().getString("uuid");
        String server = CloudAPI.getInstance().getOnlinePlayer(UUID.fromString(uuid)).getServer();
        if(event.getChannel().equalsIgnoreCase("ban")) {
            switch (event.getMessage().toLowerCase()) {
                case "check": {
                    BackendPlayer player = plugin.getBackendManager().getPlayer(event.getDocument().getString("target"));
                    GroupPlayer groupPlayer = plugin.getGroupManager().getPlayer(event.getDocument().getString("target"));
                    CloudAPI.getInstance().sendCustomSubServerMessage("ban", "check", new Document("uuid", uuid).append("target", player.getUuid()).append("muteplayer", plugin.getGson().fromJson(plugin.getGson().toJson(player.getMutePlayer()), org.bson.Document.class)).append("banplayer", plugin.getGson().fromJson(plugin.getGson().toJson(player.getBanPlayer()), org.bson.Document.class)), server);
                    break;
                }
                case "ban": {
                    String reason = event.getDocument().getString("reason");
                    String target = event.getDocument().getString("target");
                    plugin.getProxyMessageManager().ban(uuid, target, reason);
                    break;
                }
                case "mute": {
                    String reason = event.getDocument().getString("reason");
                    String target = event.getDocument().getString("target");
                    plugin.getProxyMessageManager().ban(uuid, target, reason);
                    break;
                }
                case "unban": {
                    String reason = event.getDocument().getString("reason");
                    String target = event.getDocument().getString("target");
                    plugin.getBanManager().unBan(target, uuid);
                    break;
                }
                case "unmute": {
                    String reason = event.getDocument().getString("reason");
                    String target = event.getDocument().getString("target");
                    plugin.getMuteManager().unMute(target, uuid);
                    break;
                }
            }
        }
    }
}
