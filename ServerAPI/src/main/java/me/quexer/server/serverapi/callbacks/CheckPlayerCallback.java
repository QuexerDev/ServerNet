package me.quexer.server.serverapi.callbacks;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitSubChannelMessageEvent;
import de.dytanic.cloudnet.lib.utility.document.Document;
import me.quexer.api.quexerapi.event.EventManager;
import me.quexer.server.serverapi.ServerAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class CheckPlayerCallback implements Consumer<Player> {

    private ServerAPI plugin;

    private String targetUUID;
    private String senderUUID;
    private Player sender;

    public CheckPlayerCallback(ServerAPI plugin, String targetUUID, String senderUUID, Player sender) {
        this.plugin = plugin;
        this.targetUUID = targetUUID;
        this.senderUUID = senderUUID;
        this.sender = sender;
    }

    @Override
    public void accept(Player player) {

        CloudAPI.getInstance().sendCustomSubProxyMessage("ban", "check", new Document("uuid", senderUUID).append("target", targetUUID), "Bungee-1");
        player.sendTitle("§eEinen Moment...", "§7Die Daten werden geladen§8!");
    }




}
