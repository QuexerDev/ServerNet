package me.quexer.server.serverapi.commands;

import me.quexer.server.serverapi.ServerAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

public class CoinsCMD  {

    private ServerAPI plugin;


    public CoinsCMD(ServerAPI plugin) {
        this.plugin = plugin;

        plugin.onCommand("coins", (sender, command, s, args) -> {
            sender.sendMessage("");
            sender.sendMessage(String.format(plugin.getPrefix() + "§7Du hast §e" + new DecimalFormat("###,###,###,###,###,###").format(plugin.getBackendManager().getPlayer(((Player) sender).getUniqueId().toString()).getData().getCoins()) + " §7Coin(s)§8."));
            sender.sendMessage(String.format(plugin.getPrefix() + "§7Du hast §e" + new DecimalFormat("###,###,###,###,###,###").format(plugin.getBackendManager().getPlayer(((Player) sender).getUniqueId().toString()).getData().getKeys()) + " §7Key(s)§8."));
            sender.sendMessage(String.format(plugin.getPrefix() + "§7Du hast §e" + new DecimalFormat("###,###,###,###,###,###").format(plugin.getBackendManager().getPlayer(((Player) sender).getUniqueId().toString()).getData().getCpr()) + " §7Coin(s) pro Runde (CPR)§8."));
            sender.sendMessage("");
            return true;
        });

    }


}
