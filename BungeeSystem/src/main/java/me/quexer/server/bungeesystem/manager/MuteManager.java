package me.quexer.server.bungeesystem.manager;


import me.quexer.server.bungeesystem.BungeeSystem;
import me.quexer.server.bungeesystem.misc.AsyncTask;
import me.quexer.server.bungeesystem.misc.uuid.UUIDFetcher;
import me.quexer.server.bungeesystem.models.BackendPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MuteManager {

    private BungeeSystem plugin;

    public MuteManager(BungeeSystem plugin) {
        this.plugin = plugin;
    }

    public boolean isMuted(String uuid) {
        return plugin.getBackendManager().getPlayer(uuid).getMutePlayer().isPunished();
    }

    public void mute(String targetUUID, String senderUUID, String reason, long hours, int strenght) {
        if (isMuted(targetUUID)) {
            plugin.getProxy().getPlayer(UUID.fromString(senderUUID)).sendMessage(plugin.getBanPrefix() + "§cDieser Spieler ist bereits gemutet!");
            return;
        }

        new AsyncTask(() -> {

            String senderName = UUIDFetcher.getName(UUID.fromString(senderUUID));
            String targetName = UUIDFetcher.getName(UUID.fromString(targetUUID));

            BackendPlayer sender = plugin.getBackendManager().getPlayer(senderUUID);
            BackendPlayer target = plugin.getBackendManager().getPlayer(targetUUID);
            BackendPlayer.BanPlayer mute = target.getMutePlayer();

            long now = System.currentTimeMillis();
            long end;
            if (mute.getBanPoints() >= 6 || hours == -1) {
                end = -1;
            } else {
                end = now + (hours != -1 ? (hours + (mute.getBanPoints() * (hours * (20 / 100)))) * 1000 * 60 * 60 : -1);
            }

            mute.setBanPoints(mute.getBanPoints() + strenght);
            mute.setPunished_at(System.currentTimeMillis());
            mute.setEnd(end);
            mute.setPunished_from(senderUUID);
            mute.setPunished(true);
            mute.setReason(reason);
            mute.getHistory().add(reason);

            /*if(plugin.getReportManager().isReportet(targetUUID)) {
                Report report = plugin.getBackendManager().getReportPlayerCache().get(targetUUID);
                if(plugin.getProxy().getPlayer(UUID.fromString(report.getFrom())) != null) {
                    plugin.getProxy().getPlayer(UUID.fromString(report.getFrom())).sendMessage(plugin.getReportPrefix()+"§7Ein Spieler, den du reportet hast, wurde gemutet!");
                    plugin.getProxy().getPlayer(UUID.fromString(report.getFrom())).sendMessage(plugin.getReportPrefix()+"§7Vielen Dank für deinen Einsatz! §e:)");
                    plugin.getBackendManager().getReportPlayerCache().remove(targetUUID);
                }
            }
            */

            if (plugin.getProxy().getPlayer(UUID.fromString(targetUUID)) != null) {
                ProxiedPlayer proxiedPlayer = plugin.getProxy().getPlayer(UUID.fromString(targetUUID));
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Du wurdest von "+plugin.getGroupManager().getPlayer(senderUUID).getGroup().getPrefix()+senderName+" §7gemutet§8!");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Gemutet bis§8: §e"+plugin.getDate(end));
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Grund§8: §e"+reason);
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Mute-Points§8: §e"+mute.getBanPoints());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
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
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+plugin.getGroupManager().getPlayer(targetUUID).getGroup().getPrefix()+targetName+" §7wurde von "+plugin.getGroupManager().getPlayer(senderUUID).getGroup().getPrefix()+senderName+" §7gemutet§8!");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Gemutet bis§8: §e"+plugin.getDate(end));
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Grund§8: §e"+reason);
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Mute-Points§8: §e"+mute.getBanPoints());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
            });


        });


    }

    public void unMute(String targetUUID, String senderUUID) {
        if (!isMuted(targetUUID)) {
            plugin.getProxy().getPlayer(UUID.fromString(senderUUID)).sendMessage(plugin.getBanPrefix() + "§cDieser Spieler ist nicht gemutet!");
            return;
        }

        new AsyncTask(() -> {

            String senderName = UUIDFetcher.getName(UUID.fromString(senderUUID));
            String targetName = UUIDFetcher.getName(UUID.fromString(targetUUID));

            BackendPlayer sender = plugin.getBackendManager().getPlayer(senderUUID);
            BackendPlayer target = plugin.getBackendManager().getPlayer(targetUUID);
            BackendPlayer.BanPlayer mute = target.getMutePlayer();

            mute.setEnd(-1);
            mute.setPunished(false);
            mute.setReason(null);
            mute.setPunished_at(-1);
            mute.setPunished_from("NONE");

            List<ProxiedPlayer> team = new ArrayList<>();
            plugin.getProxy().getPlayers().forEach(proxiedPlayer -> {
                if(plugin.getGroupManager().getPlayer(proxiedPlayer.getUniqueId().toString()).getGroup().getLevelID() > 5) {
                    team.add(proxiedPlayer);
                }
            });

            team.forEach(proxiedPlayer -> {
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+plugin.getGroupManager().getPlayer(targetUUID).getGroup().getPrefix()+targetName+" §7wurde von "+plugin.getGroupManager().getPlayer(senderUUID).getGroup().getPrefix()+senderName+" §7entmutet§8!");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
            });

            plugin.getBackendManager().savePlayer(target);

        });

    }

    public void unMuteConsole(String targetUUID) {
        if (!isMuted(targetUUID)) {
            return;
        }

        new AsyncTask(() -> {

            String targetName = UUIDFetcher.getName(UUID.fromString(targetUUID));

            BackendPlayer target = plugin.getBackendManager().getPlayer(targetUUID);
            BackendPlayer.BanPlayer mute = target.getMutePlayer();

            mute.setEnd(-1);
            mute.setPunished(false);
            mute.setReason(null);
            mute.setPunished_at(-1);
            mute.setPunished_from("NONE");

            List<ProxiedPlayer> team = new ArrayList<>();
            plugin.getProxy().getPlayers().forEach(proxiedPlayer -> {
                if(plugin.getGroupManager().getPlayer(proxiedPlayer.getUniqueId().toString()).getGroup().getLevelID() > 5) {
                    team.add(proxiedPlayer);
                }
            });

            team.forEach(proxiedPlayer -> {
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+plugin.getGroupManager().getPlayer(targetUUID).getGroup().getPrefix()+targetName+" §7wurde von der §eCONSOLE §7entmutet§8!");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§7Grund§8: §eAutomatischer Entmute");
                proxiedPlayer.sendMessage(plugin.getBanPrefix()+"§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");
                proxiedPlayer.sendMessage(plugin.getBanPrefix());
            });
            

            plugin.getBackendManager().savePlayer(target);

        });

    }
}
