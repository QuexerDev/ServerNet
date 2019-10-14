package me.quexer.server.bungeesystem.manager;

import me.quexer.server.bungeesystem.BungeeSystem;
import me.quexer.server.bungeesystem.misc.AsyncTask;
import me.quexer.server.bungeesystem.misc.uuid.UUIDFetcher;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ProxyMessageManager {

    private BungeeSystem plugin;

    public ProxyMessageManager(BungeeSystem plugin) {
        this.plugin = plugin;
    }

    public void ban(String uuid, String target, String reason) {

        new AsyncTask(() -> {

            switch (reason) {
                case "Hacking":
                    plugin.getBanManager().ban(target, uuid, "Hacking", 24 * 14, 3);
                    break;

                case "Trolling":
                    plugin.getBanManager().ban(target, uuid, "Trolling", 24 * 3, 2);
                    break;
                case "Teaming":
                    plugin.getBanManager().ban(target, uuid, "Teaming", 24 * 1, 1);
                    break;

                case "Bugusing":
                    plugin.getBanManager().ban(target, uuid, "Bugusing", 24 * 3, 2);
                    break;

                case "Skin":
                    plugin.getBanManager().ban(target, uuid, "Skin", 2, 1);
                    break;

                case "Name":
                    plugin.getBanManager().ban(target, uuid, "Name", 2, 1);
                    break;

                case "Reportausnutzung":
                    plugin.getBanManager().ban(target, uuid, "Reportausnutzung", 24 * 2, 1);
                    break;

                case "Banumgehung":
                    plugin.getBanManager().ban(target, uuid, "Banumgehung", -1, 10);
                    break;

                case "Hausverbot":
                    plugin.getBanManager().ban(target, uuid, "Hausverbot", -1, 10);
                    break;


                case "Beleidigung":
                    plugin.getMuteManager().mute(target, uuid, "Beleidigung", 24 * 3, 2);
                    break;

                case "Rassismus":
                    plugin.getMuteManager().mute(target, uuid, "Rassismus", 24 * 7, 3);
                    break;

                case "Werbung":
                    plugin.getMuteManager().mute(target, uuid, "Werbung", 24 * 1, 1);
                    break;

                case "Spam":
                    plugin.getMuteManager().mute(target, uuid, "Spam", 12, 1);
                    break;

                case "Provokation":
                    plugin.getMuteManager().mute(target, uuid, "Provokation", 12, 2);
                    break;

                case "Schweigepflicht":
                    plugin.getMuteManager().mute(target, uuid, "Schweigepflicht", -1, 10);
                    break;

            }
        });
    }


}
