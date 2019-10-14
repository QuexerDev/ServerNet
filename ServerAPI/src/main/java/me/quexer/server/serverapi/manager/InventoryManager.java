package me.quexer.server.serverapi.manager;

import com.google.gson.JsonArray;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitSubChannelMessageEvent;
import me.quexer.api.quexerapi.builder.ItemBuilder;
import me.quexer.api.quexerapi.builder.inventory.GuiBuilder;
import me.quexer.api.quexerapi.builder.inventory.GuiItem;
import me.quexer.api.quexerapi.builder.inventory.InventoryGui;
import me.quexer.api.quexerapi.event.EventManager;
import me.quexer.api.quexerapi.misc.AsyncTask;
import me.quexer.api.quexerapi.misc.uuid.UUIDFetcher;
import me.quexer.server.serverapi.ServerAPI;
import me.quexer.server.serverapi.callbacks.BanGuiCallback;
import me.quexer.server.serverapi.callbacks.CheckPlayerCallback;
import me.quexer.server.serverapi.callbacks.UnBanCallback;
import me.quexer.server.spigotserverperms.models.Group;
import me.quexer.server.spigotserverperms.models.GroupPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import de.dytanic.cloudnet.lib.utility.document.Document;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InventoryManager {

    private ServerAPI plugin;

    public InventoryManager(ServerAPI plugin) {
        this.plugin = plugin;
    }

    public void banInv(String targetName, Player sender) {
        UUIDFetcher.getUUID(targetName, targetUUID -> {
            if (targetUUID == null) {
                sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler existiert nicht!");
                return;
            }
        });
        targetName = Bukkit.getOfflinePlayer(targetName).getName();
        String senderUUID = sender.getUniqueId().toString();
        String targetUUID = UUIDFetcher.getUUID(targetName).toString();
        GuiBuilder builder = getDefaultInv("§c§lBanSystem");
        builder.addItem(4, new GuiItem(new ItemBuilder(Material.SIGN).setName("§7Bestrafe§8: §e" + targetName).toItemStack(), player -> player.playSound(player.getLocation(), Sound.CREEPER_HISS, 1, 1)));

        builder.addItem(11, new GuiItem(new ItemBuilder(Material.IRON_SWORD).setName("§eTeaming §8(§c§lBAN§8)").toItemStack(), new BanGuiCallback(plugin, "Teaming", senderUUID, targetUUID)));
        builder.addItem(12, new GuiItem(new ItemBuilder(Material.IRON_PICKAXE).setName("§eTrolling §8(§c§lBAN§8)").toItemStack(), new BanGuiCallback(plugin, "Trolling", senderUUID, targetUUID)));
        builder.addItem(13, new GuiItem(new ItemBuilder(Material.DIAMOND_SWORD).setName("§eHacking §8(§c§lBAN§8)").toItemStack(), new BanGuiCallback(plugin, "Hacking", senderUUID, targetUUID)));
        builder.addItem(14, new GuiItem(new ItemBuilder(Material.SKULL_ITEM, 1, (byte) 3).setName("§eSkin §8(§c§lBAN§8)").toItemStack(), new BanGuiCallback(plugin, "Skin", senderUUID, targetUUID)));
        builder.addItem(15, new GuiItem(new ItemBuilder(Material.NAME_TAG).setName("§eName §8(§c§lBAN§8)").toItemStack(), new BanGuiCallback(plugin, "Name", senderUUID, targetUUID)));
        builder.addItem(19, new GuiItem(new ItemBuilder(Material.REDSTONE).setName("§eReportausnutzung §8(§c§lBAN§8)").toItemStack(), new BanGuiCallback(plugin, "Reportausnutzung", senderUUID, targetUUID)));
        builder.addItem(20, new GuiItem(new ItemBuilder(Material.BEACON).setName("§eBanumgehung §8(§c§lBAN§8)").toItemStack(), new BanGuiCallback(plugin, "Banumgehung", senderUUID, targetUUID)));
        builder.addItem(21, new GuiItem(new ItemBuilder(Material.BOOKSHELF).setName("§eBugusing §8(§c§lBAN§8)").toItemStack(), new BanGuiCallback(plugin, "Bugusing", senderUUID, targetUUID)));
        builder.addItem(22, new GuiItem(new ItemBuilder(Material.BARRIER).setName("§eHausverbot §8(§c§lBAN§8)").toItemStack(), new BanGuiCallback(plugin, "Hausverbot", senderUUID, targetUUID)));
        builder.addItem(23, new GuiItem(new ItemBuilder(Material.BOOK_AND_QUILL).setName("§eBeleidigung §8(§c§lMUTE§8)").toItemStack(), new BanGuiCallback(plugin, "Beleidigung", senderUUID, targetUUID)));
        builder.addItem(24, new GuiItem(new ItemBuilder(Material.BOOK_AND_QUILL).setName("§eRassismus §8(§c§lMUTE§8)").toItemStack(), new BanGuiCallback(plugin, "Rassismus", senderUUID, targetUUID)));
        builder.addItem(25, new GuiItem(new ItemBuilder(Material.BOOK_AND_QUILL).setName("§eWerbung §8(§c§lMUTE§8)").toItemStack(), new BanGuiCallback(plugin, "Werbung", senderUUID, targetUUID)));
        builder.addItem(30, new GuiItem(new ItemBuilder(Material.BOOK_AND_QUILL).setName("§eSpam §8(§c§lMUTE§8)").toItemStack(), new BanGuiCallback(plugin, "Spam", senderUUID, targetUUID)));
        builder.addItem(31, new GuiItem(new ItemBuilder(Material.BOOK_AND_QUILL).setName("§eProvokation §8(§c§lMUTE§8)").toItemStack(), new BanGuiCallback(plugin, "Provokation", senderUUID, targetUUID)));
        builder.addItem(32, new GuiItem(new ItemBuilder(Material.BOOK_AND_QUILL).setName("§eSchweigepflicht §8(§c§lMUTE§8)").toItemStack(), new BanGuiCallback(plugin, "Schweigepflicht", senderUUID, targetUUID)));

        builder.onClose(player -> {
            sender.playSound(sender.getLocation(), Sound.CHEST_CLOSE, 0.3F, 0.3F);
        });
        sender.playSound(sender.getLocation(), Sound.CHEST_OPEN, 0.3F, 0.3F);
        builder.build().open(sender);

    }

    public void unBanInv(String targetName, Player sender) {
        UUIDFetcher.getUUID(targetName, targetUUID -> {
            if (targetUUID == null) {
                sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler existiert nicht!");
                return;
            }
        });
        targetName = Bukkit.getOfflinePlayer(targetName).getName();
        String senderUUID = sender.getUniqueId().toString();
        String targetUUID = UUIDFetcher.getUUID(targetName).toString();
        GuiBuilder builder = getDefaultInv("§c§lUnBan/UnMute");
        builder.addItem(4, new GuiItem(new ItemBuilder(Material.SIGN).setName("§7Entstrafe§8: §e" + targetName).toItemStack(), player -> player.playSound(player.getLocation(), Sound.CREEPER_HISS, 1, 1)));

        builder.addItem(20, new GuiItem(new ItemBuilder(Material.BARRIER).setName("§8> §cEntbannen").setLore("§8§m---------------", "§7Klicke um §e" + targetName + " §7zu entbannen.").toItemStack(), player -> {

            GuiBuilder acceptGui = getDefaultInv("§a§lBestätigen");
            acceptGui.addItem(20, new GuiItem(new ItemBuilder(Material.EMERALD_BLOCK).setName("§aJa, Spieler entbannen").toItemStack(), new UnBanCallback(plugin, targetUUID, senderUUID, "BAN")));
            acceptGui.addItem(24, new GuiItem(new ItemBuilder(Material.REDSTONE_BLOCK).setName("§cAbbrechen").toItemStack(), player1 -> {
                player1.playSound(player1.getLocation(), Sound.NOTE_BASS, 0.3F, 0.3F);
                player1.closeInventory();
            }));
            acceptGui.onClose(player1 -> {
                player1.playSound(player1.getLocation(), Sound.CHEST_CLOSE, 0.3F, 0.3F);
            });
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.3F, 0.3F);
            acceptGui.build().open(player);

        }));
        builder.addItem(24, new GuiItem(new ItemBuilder(Material.BOOK_AND_QUILL).setName("§8> §cEntmuten").setLore("§8§m---------------", "§7Klicke um §e" + targetName + " §7zu entmuten.").toItemStack(), player -> {

            GuiBuilder acceptGui = getDefaultInv("§a§lBestätigen");
            acceptGui.addItem(20, new GuiItem(new ItemBuilder(Material.EMERALD_BLOCK).setName("§aJa, Spieler entmuten").toItemStack(), new UnBanCallback(plugin, targetUUID, senderUUID, "MUTE")));
            acceptGui.addItem(24, new GuiItem(new ItemBuilder(Material.REDSTONE_BLOCK).setName("§cAbbrechen").toItemStack(), player1 -> {
                player1.playSound(player1.getLocation(), Sound.NOTE_BASS, 0.3F, 0.3F);
                player1.closeInventory();
            }));
            acceptGui.onClose(player1 -> {
                player1.playSound(player1.getLocation(), Sound.CHEST_CLOSE, 0.3F, 0.3F);
            });
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.3F, 0.3F);
            acceptGui.build().open(player);

        }));


        sender.playSound(sender.getLocation(), Sound.CHEST_OPEN, 0.3F, 0.3F);
        builder.build().open(sender);


    }

    public void checkInv(Player sender, String targetUUID, Document document) {


        new AsyncTask(() -> {
            String targetName = UUIDFetcher.getName(UUID.fromString(targetUUID));
            if (targetName == null) {
                sender.sendMessage(plugin.getPrefix() + "§cDieser Spieler existiert nicht!");
                return;
            }
            GuiBuilder builder = getDefaultInv("§c§lCheck");

            Document banPlayer = document.getDocument("banplayer");
            Document mutePlayer = document.getDocument("muteplayer");

            GroupPlayer groupPlayer = plugin.getGroupManager().getPlayer(targetUUID);
            Group group = groupPlayer.getGroup();
            System.out.println(banPlayer);
            System.out.println(mutePlayer);
            builder.addItem(22, new GuiItem(new ItemBuilder(Material.SIGN).setName("§e" + targetName + " §8> §eAllgemein").
                    setLore("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀",
                            "§7BanPoints§8: §e" + (mutePlayer.getInt("banPoints") + banPlayer.getInt("banPoints")),
                            "§7Rang§8: §e" + group.getFullName(),
                            "§7Expires§8: §e" + plugin.getDate(groupPlayer.getExpires()),
                            "§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄")
                    .toItemStack(), player -> player.playSound(player.getLocation(), Sound.CREEPER_HISS, 1, 1)));

            builder.addItem(20, getBanPlayerItem(banPlayer, targetName));
            builder.addItem(24, getMutePlayerItem(mutePlayer, targetName));



            sender.playSound(sender.getLocation(), Sound.CHEST_OPEN, 0.3F, 0.3F);
            builder.build().open(sender);
        });

    }

    public GuiBuilder getDefaultInv(String name) {
        GuiBuilder builder = plugin.getQuexerAPI().gui(45).setName(name);
        for (int i = 0; i < builder.getSize(); i++) {
            builder.addItem(i, new GuiItem(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 15).setName("§a").toItemStack(), null));
        }
        return builder;
    }

    private GuiItem getBanPlayerItem(Document banPlayer, String targetName) {

        List<String> lore = new ArrayList<>();
        lore.add("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        lore.add("§7Gebannt§8: " + (banPlayer.getBoolean("isPunished") ? "§aJa" : "§cNein"));
        if (banPlayer.getBoolean("isPunished")) {
            lore.add("§7Grund§8: §e" + banPlayer.getString("reason"));
            lore.add("§7Gebannt von§8: §e" + plugin.getGroupManager().getPlayer(banPlayer.getString("punished_from")).getGroup().getPrefix() + UUIDFetcher.getName(UUID.fromString(banPlayer.getString("punished_from"))));
            lore.add("§7Gebannt bis§8: §e" + plugin.getDate(banPlayer.getLong("end")));
        }
        JsonArray history = banPlayer.getArray("history");
        if (history.size() == 0)
            lore.add("§cDieser Spieler wurde noch nie gebannt!");
        else {
            history.forEach(histore -> {
                lore.add("§8- §e" + histore.getAsString());
            });
        }
        lore.add("§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");

        return new GuiItem(new ItemBuilder(Material.BARRIER).setName("§e" + targetName + " §8> §eBan").setLore(lore).toItemStack(), player -> unBanInv(targetName, player));
    }

    private GuiItem getMutePlayerItem(Document mutePlayer, String targetName) {

        List<String> lore = new ArrayList<>();
        lore.add("§8▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀▀");
        lore.add("§7Gemutet§8: " + (mutePlayer.getBoolean("isPunished") ? "§aJa" : "§cNein"));
        if (mutePlayer.getBoolean("isPunished")) {
            lore.add("§7Grund§8: §e" + mutePlayer.getString("reason"));
            lore.add("§7Gemutet von§8: §e" + plugin.getGroupManager().getPlayer(mutePlayer.getString("punished_from")).getGroup().getPrefix() + UUIDFetcher.getName(UUID.fromString(mutePlayer.getString("punished_from"))));
            lore.add("§7Gemutet bis§8: §e" + plugin.getDate(mutePlayer.getLong("end")));
        }
        JsonArray history = mutePlayer.getArray("history");
        if (history.size() == 0)
            lore.add("§cDieser Spieler wurde noch nie gemutet!");
        else {
            history.forEach(histore -> {
                lore.add("§8- §e" + histore.getAsString());
            });
        }
        lore.add("§8▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄▄");

        return new GuiItem(new ItemBuilder(Material.BOOK_AND_QUILL).setName("§e" + targetName + " §8> §eMute").setLore(lore).toItemStack(), player -> unBanInv(targetName, player));
    }


}
