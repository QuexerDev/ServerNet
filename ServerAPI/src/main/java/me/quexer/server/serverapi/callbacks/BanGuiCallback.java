package me.quexer.server.serverapi.callbacks;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.lib.utility.document.Document;
import me.quexer.server.serverapi.ServerAPI;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class BanGuiCallback implements Consumer<Player> {

    private ServerAPI plugin;
    private String reason;
    private String senderUUID;
    private String targetUUID;

    public BanGuiCallback(ServerAPI plugin, String reason, String senderUUID, String targetUUID) {
        this.plugin = plugin;
        this.reason = reason;
        this.senderUUID = senderUUID;
        this.targetUUID = targetUUID;
    }


    @Override
    public void accept(Player player) {
        player.closeInventory();

        player.sendTitle("§eEinen Moment...", "§7Der Spieler wird §cbestraft§8!");

        switch (reason) {
            case "Teaming":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "ban", new Document("uuid", senderUUID).append("reason", "Teaming").append("target", targetUUID), "Bungee-1");
                break;
            case "Trolling":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "ban", new Document("uuid", senderUUID).append("reason", "Trolling").append("target", targetUUID), "Bungee-1");
                break;
            case "Hacking":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "ban", new Document("uuid", senderUUID).append("reason", "Hacking").append("target", targetUUID), "Bungee-1");
                break;
            case "Skin":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "ban", new Document("uuid", senderUUID).append("reason", "Skin").append("target", targetUUID), "Bungee-1");
                break;
            case "Name":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "ban", new Document("uuid", senderUUID).append("reason", "Name").append("target", targetUUID), "Bungee-1");
                break;
            case "Reportausnutzung":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "ban", new Document("uuid", senderUUID).append("reason", "Reportausnutzung").append("target", targetUUID), "Bungee-1");
                break;
            case "Banumgehung":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "ban", new Document("uuid", senderUUID).append("reason", "Banumgehung").append("target", targetUUID), "Bungee-1");
                break;
            case "Hausverbot":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "ban", new Document("uuid", senderUUID).append("reason", "Hausverbot").append("target", targetUUID), "Bungee-1");
                break;
            case "Bugusing":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "ban", new Document("uuid", senderUUID).append("reason", "Bugusing").append("target", targetUUID), "Bungee-1");
                break;
            case "Beleidigung":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "mute", new Document("uuid", senderUUID).append("reason", "Beleidigung").append("target", targetUUID), "Bungee-1");
                break;
            case "Rassismus":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "mute", new Document("uuid", senderUUID).append("reason", "Rassismus").append("target", targetUUID), "Bungee-1");
                break;
            case "Werbung":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "mute", new Document("uuid", senderUUID).append("reason", "Werbung").append("target", targetUUID), "Bungee-1");
                break;
            case "Spam":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "mute", new Document("uuid", senderUUID).append("reason", "Spam").append("target", targetUUID), "Bungee-1");
                break;
            case "Provokation":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "mute", new Document("uuid", senderUUID).append("reason", "Provokation").append("target", targetUUID), "Bungee-1");
                break;
            case "Schweigepflicht":
                CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "mute", new Document("uuid", senderUUID).append("reason", "Schweigepflicht").append("target", targetUUID), "Bungee-1");
                break;


        }
    }
}
