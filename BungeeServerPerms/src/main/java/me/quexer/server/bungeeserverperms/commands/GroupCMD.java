package me.quexer.server.bungeeserverperms.commands;


import me.quexer.server.bungeeserverperms.ServerPerms;
import me.quexer.server.bungeeserverperms.models.Group;
import me.quexer.server.bungeeserverperms.models.GroupPlayer;
import me.quexer.server.bungeeserverperms.uuid.UUIDFetcher;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;
import java.util.stream.Collectors;

public class GroupCMD extends Command {

    private ServerPerms plugin;

    public GroupCMD(ServerPerms plugin) {
        super("group");
        this.plugin = plugin;

        //group set Quexer Admin -1
        //group get Quexer
        //group set Quexer Admin 3

    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        boolean isPlayer = (sender instanceof ProxiedPlayer);
        boolean allow = false;
        if (isPlayer && plugin.getGroupManager().getPlayer(((ProxiedPlayer) sender).getUniqueId().toString()).getGroup().getLevelID() >= 12) {
            allow = true;
        } else if (!isPlayer && sender.hasPermission("admin")) {
            allow = true;
        } else {
            allow = false;
        }
        if (allow) {
            switch (args.length) {
                case 1: {
                    if (args[0].equalsIgnoreCase("list")) {
                        sender.sendMessage("§8§m-------------------------");
                        sender.sendMessage("§7Spielergruppen:");
                        plugin.getGroupManager().getGroups().forEach(backendGroup -> {
                            sender.sendMessage("§7- " + backendGroup.getFullName() + " §8/ §e" + backendGroup.getLevelID());
                        });
                        sender.sendMessage("§8§m-------------------------");
                    } else
                        help(sender);

                    break;
                }
                case 2: {
                    if (args[0].equalsIgnoreCase("info")) {
                        UUIDFetcher.getUUID(args[1], uuid -> {
                            if (uuid != null) {
                                GroupPlayer groupPlayer = plugin.getGroupManager().getPlayer(UUID.fromString(uuid.toString()).toString());
                                sender.sendMessage(plugin.getPrefix() + "§7Der Spieler " + groupPlayer.getGroup().getColor() + args[1] + " §7hat die Gruppe§8: §e" + groupPlayer.getGroup().getFullName());
                                sender.sendMessage(plugin.getPrefix() + "§7Er hat die Gruppe noch bis zum§8: §e" + plugin.getGroupManager().getDate(groupPlayer.getExpires()));
                            } else {
                                sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler existiert nicht!");
                            }
                        });
                    } else
                        help(sender);
                    break;
                }
                case 4: {

                    try {

                        if (args[0].equalsIgnoreCase("set")) {
                            if (Integer.valueOf(args[2]) <= plugin.getGroupManager().getGroups().size()) {
                                Group group = plugin.getGroupManager().getGroups().stream().filter(backendGroup -> backendGroup.getLevelID() == Integer.valueOf(args[2])).collect(Collectors.toList()).get(0);
                                UUIDFetcher.getUUID(args[1], uuid -> {
                                    if (uuid != null) {
                                        GroupPlayer groupPlayer = plugin.getGroupManager().getPlayer(UUID.fromString(uuid.toString()).toString());
                                        plugin.getGroupManager().setGroup(UUID.fromString(uuid.toString()).toString(), group, Long.valueOf(args[3]));
                                        sender.sendMessage(plugin.getPrefix() + "§7Der Spieler " + group.getColor() + args[1] + " §7hat die Gruppe§8: " + group.getFullName() + " §7erhalten§8. §7Bis zum§8: §e" + plugin.getGroupManager().getDate(groupPlayer.getExpires() ));

                                    } else {
                                        sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler existiert nicht!");
                                    }
                                });
                            } else
                                sender.sendMessage(plugin.getPrefix() + "§cDiese Gruppe existiert nicht §8[§e/group list§8]");
                        } else
                            help(sender);
                        break;
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(plugin.getError() + "§7Achte auf dein Eingabeformat§8!");
                    }
                }
                default:
                    help(sender);
                    break;
            }
        } else {
            GroupPlayer player = plugin.getGroupManager().getPlayer(plugin.getProxy().getPlayer(sender.getName()).getUniqueId().toString());
            sender.sendMessage(plugin.getPrefix() + "§7Du hast deine Gruppe noch bis zum§8: §e" + plugin.getGroupManager().getDate(player.getExpires()));
        }
    }

    private void help(CommandSender sender) {
        sender.sendMessage(plugin.getPrefix() + "§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        sender.sendMessage(plugin.getPrefix() + "§e/group info [Name]");
        sender.sendMessage(plugin.getPrefix() + "§e/group set [Name] [GroupID] [days]");
        sender.sendMessage(plugin.getPrefix() + "§e/group list");
        sender.sendMessage(plugin.getPrefix() + "§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
    }
}
