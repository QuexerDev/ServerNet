package me.quexer.server.serverapi.commands;

import me.quexer.api.quexerapi.misc.uuid.UUIDFetcher;
import me.quexer.server.serverapi.ServerAPI;
import me.quexer.server.serverapi.callbacks.CheckPlayerCallback;
import me.quexer.server.spigotserverperms.models.Group;
import org.bukkit.entity.Player;

public class CheckCMD {

    private ServerAPI plugin;

    public CheckCMD(ServerAPI plugin) {
        this.plugin = plugin;

        plugin.onCommand("check", (sender, command, s, args) -> {

            Player player = (Player) sender;
            Group group = plugin.getGroupManager().getPlayer(player.getUniqueId().toString()).getGroup();
            if(group.hasPermission(9)) {
                if (args.length == 1) {
                    UUIDFetcher.getUUID(args[0], targetUUID -> {
                        if (targetUUID == null) {
                            sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler existiert nicht!");
                            return;
                        }

                        new CheckPlayerCallback(plugin, targetUUID.toString(), ((Player) sender).getUniqueId().toString(), (Player) sender).accept((Player) sender);
                    });
                } else {
                    player.sendMessage(plugin.getError()+"§7Benutze§8: §c/check <Spieler>");
                }
            }
            return true;
        });

    }

}
