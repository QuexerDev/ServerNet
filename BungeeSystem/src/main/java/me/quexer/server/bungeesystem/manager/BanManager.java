package me.quexer.server.bungeesystem.manager;

import me.quexer.server.bungeeserverperms.uuid.UUIDFetcher;
import me.quexer.server.bungeesystem.BungeeSystem;
import me.quexer.server.bungeesystem.misc.AsyncTask;
import me.quexer.server.bungeesystem.models.BackendPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanManager {

    private BungeeSystem plugin;

    public BanManager(BungeeSystem plugin) {
        this.plugin = plugin;
    }

    public boolean isBanned(String uuid) {
        return plugin.getBackendManager().getPlayer(uuid).getBanPlayer().isPunished();
    }

    public void ban(String targetUUID, String senderUUID, String reason, long hours, int strenght) {
        if (isBanned(targetUUID)) {
            plugin.getProxy().getPlayer(UUID.fromString(senderUUID)).sendMessage(plugin.getBanPrefix() + "§cDieser Spieler ist bereits gebannt!");
            return;
        }

        new AsyncTask(() -> {

            String senderName = UUIDFetcher.getName(UUID.fromString(senderUUID));
            String targetName = UUIDFetcher.getName(UUID.fromString(targetUUID));

            BackendPlayer sender = plugin.getBackendManager().getPlayer(senderUUID);
            BackendPlayer target = plugin.getBackendManager().getPlayer(targetUUID);
            BackendPlayer.BanPlayer ban = target.getBanPlayer();

            long now = System.currentTimeMillis();
            long end;
            if (ban.getBanPoints() >= 6 || hours == -1) {
                end = -1;
            } else {
                end = now + (hours != -1 ? (hours + (hours * (ban.getBanPoints() * (20 / 100)))) * 1000 * 60 * 60 : -1);
            }

            ban.setBanPoints(ban.getBanPoints() + strenght);
            ban.setPunished_at(System.currentTimeMillis());
            ban.setEnd(end);
            ban.setPunished_from(senderUUID);
            ban.setPunished(true);
            ban.setReason(reason);
            ban.getHistory().add(reason);

           /* if(plugin.getReportManager().isReportet(targetUUID)) {
                Report report = plugin.getBackendManager().getReportPlayerCache().get(targetUUID);
                if(plugin.getProxy().getPlayer(UUID.fromString(report.getFrom())) != null) {
                    plugin.getProxy().getPlayer(UUID.fromString(report.getFrom())).sendMessage(plugin.getReportPrefix()+"§7Ein Spieler, den du reportet hast, wurde gebannt!");
                    plugin.getProxy().getPlayer(UUID.fromString(report.getFrom())).sendMessage(plugin.getReportPrefix()+"§7Vielen Dank für deinen Einsatz! §e:)");
                    plugin.getBackendManager().getReportPlayerCache().remove(targetUUID);
                }
            }
            */

            if (plugin.getProxy().getPlayer(UUID.fromString(targetUUID)) != null) {
                plugin.getProxy().getPlayer(UUID.fromString(targetUUID)).disconnect(
                        "§e§lServer§7§l.§e§lnet\n\n" +
                                "§cDu wurdest vom §eNetzwerk §cgebannt§8!\n" +
                                "\n" +
                                "§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀\n" +
                                "§7Grund§8: §e" + reason + "\n" +
                                "§7Gebannt von§8: §e" + plugin.getGroupManager().getPlayer(senderUUID).getGroup().getPrefix()+senderName + "\n" +
                                "§7Gebannt bis§8: §e" + plugin.getDate(end) + "\n" +
                                "§7BanPoints§8: §e" + ban.getBanPoints() + "\n" +
                                "§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄\n" +
                                "\n" +
                                "§7Du kannst einen §eEntbannungsantrag §7im Forum stellen!"
                );
            }

            plugin.getBackendManager().savePlayer(target);

            List<ProxiedPlayer> team = new ArrayList<>();
            plugin.getProxy().getPlayers().forEach(proxiedPlayer -> {
                if(plugin.getGroupManager().getPlayer(proxiedPlayer.getUniqueId().toString()).getGroup().getLevelID() > 5) {
                    team.add(proxiedPlayer);
                }
            });

            team.forEach(proxiedPlayer -> {
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+plugin.getGroupManager().getPlayer(targetUUID).getGroup().getPrefix()+targetName+" §7wurde von "+plugin.getGroupManager().getPlayer(senderUUID).getGroup().getPrefix()+senderName+" §7gebannt§8!");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Gebannt bis§8: §e"+plugin.getDate(end));
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Grund§8: §e"+reason);
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Ban-Points§8: §e"+ban.getBanPoints());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
            });


        });


    }

    public void unBan(String targetUUID, String senderUUID) {
        if (!isBanned(targetUUID)) {
            plugin.getProxy().getPlayer(UUID.fromString(senderUUID)).sendMessage(plugin.getBanPrefix() + "§cDieser Spieler ist nicht gebannt!");
            return;
        }

        new AsyncTask(() -> {

            String senderName = UUIDFetcher.getName(UUID.fromString(senderUUID));
            String targetName = UUIDFetcher.getName(UUID.fromString(targetUUID));

            BackendPlayer sender = plugin.getBackendManager().getPlayer(senderUUID);
            BackendPlayer target = plugin.getBackendManager().getPlayer(targetUUID);
            BackendPlayer.BanPlayer ban = target.getBanPlayer();

            ban.setEnd(-1);
            ban.setPunished(false);
            ban.setReason(null);
            ban.setPunished_at(-1);
            ban.setPunished_from("NONE");

            List<ProxiedPlayer> team = new ArrayList<>();
            plugin.getProxy().getPlayers().forEach(proxiedPlayer -> {
                if(plugin.getGroupManager().getPlayer(proxiedPlayer.getUniqueId().toString()).getGroup().getLevelID() > 5) {
                    team.add(proxiedPlayer);
                }
            });

            team.forEach(proxiedPlayer -> {
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+plugin.getGroupManager().getPlayer(targetUUID).getGroup().getPrefix()+targetName+" §7wurde von "+plugin.getGroupManager().getPlayer(senderUUID).getGroup().getPrefix()+senderName+" §7entbannt§8!");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
            });

            plugin.getBackendManager().savePlayer(target);

        });

    }

    public void unBanConsole(String targetUUID) {
        if (!isBanned(targetUUID)) {
            return;
        }

        new AsyncTask(() -> {

            String targetName = UUIDFetcher.getName(UUID.fromString(targetUUID));

            BackendPlayer target = plugin.getBackendManager().getPlayer(targetUUID);
            BackendPlayer.BanPlayer ban = target.getBanPlayer();

            ban.setEnd(-1);
            ban.setPunished(false);
            ban.setReason(null);
            ban.setPunished_at(-1);
            ban.setPunished_from("NONE");

            List<ProxiedPlayer> team = new ArrayList<>();
            plugin.getProxy().getPlayers().forEach(proxiedPlayer -> {
                if(plugin.getGroupManager().getPlayer(proxiedPlayer.getUniqueId().toString()).getGroup().getLevelID() > 5) {
                    team.add(proxiedPlayer);
                }
            });

            team.forEach(proxiedPlayer -> {
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+plugin.getGroupManager().getPlayer(targetUUID).getGroup().getPrefix()+targetName+" §7wurde von der §eCONSOLE §7entbannt§8!");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Grund§8: §eAutomatischer Entbann");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
            });

            plugin.getBackendManager().savePlayer(target);

        });

    }

}
