package me.quexer.server.serverapi.commands;

import me.quexer.api.quexerapi.misc.uuid.UUIDFetcher;
import me.quexer.server.serverapi.ServerAPI;
import me.quexer.server.spigotserverperms.models.Group;
import org.bukkit.entity.Player;

public class BanCMD {

    private ServerAPI plugin;

    public BanCMD(ServerAPI plugin) {
        this.plugin = plugin;

        plugin.onCommand("ban", (sender, command, s, args) -> {
            Player player = (Player) sender;
            Group group = plugin.getGroupManager().getPlayer(player.getUniqueId().toString()).getGroup();
            if(group.hasPermission(9)) {
                if(args.length == 1) {
                    plugin.getInventoryManager().banInv(args[0], player);
                } else {
                    player.sendMessage(plugin.getError()+"§7Benutze§8: §c/ban <Spieler>");
                }
            }
            return true;
        });

    }

}
