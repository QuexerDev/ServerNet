package me.quexer.server.spigotserverperms;

import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.bukkit.BukkitSubChannelMessageEvent;
import de.dytanic.cloudnet.lib.utility.document.Document;
import me.quexer.api.quexerapi.QuexerAPI;
import me.quexer.api.quexerapi.database.MongoManager;
import me.quexer.server.spigotserverperms.listeners.MainListeners;
import me.quexer.server.spigotserverperms.manager.GroupManager;
import me.quexer.server.spigotserverperms.manager.GroupPlayerManager;
import me.quexer.server.spigotserverperms.models.GroupPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServerPerms extends JavaPlugin implements Listener {

    private static ServerPerms instance;

    private MongoManager mongoManager;
    private QuexerAPI quexerAPI;
    private String prefix;
    private String error;
    private String noPerms;
    private GroupManager groupManager;
    private GroupPlayerManager groupPlayerManager;


    @Override
    public void onEnable() {
        instance = this;
        quexerAPI = new QuexerAPI(this);
        mongoManager = new MongoManager("localhost", 27017, "ServerNET");
        prefix = "§6§lPerms §8➜ ";
        error = "§4§lError §8➜ ";
        noPerms = "§4§lPerms §8➜ §7Dazu hast du keine Rechte§8!";
        groupManager = new GroupManager(this);
        groupPlayerManager = new GroupPlayerManager(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        new MainListeners(this);
        //getServer().getMessenger().registerOutgoingPluginChannel(this, "cloudnet:main");
        //CloudAPI.getInstance().sendCustomSubProxyMessage("group", "getPlayer", new Document("uuid", Bukkit.getOfflinePlayer("Quexer").getUniqueId().toString()));
    }

    /*@EventHandler
    public void onIncomeMessage(BukkitSubChannelMessageEvent event) {
        System.out.println(event.getChannel()+";"+event.getMessage()+";"+event.getDocument().getString("uuid"));

    }
    */


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ServerPerms getInstance() {
        return instance;
    }

    public GroupPlayerManager getGroupPlayerManager() {
        return groupPlayerManager;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public QuexerAPI getQuexerAPI() {
        return quexerAPI;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getError() {
        return error;
    }

    public String getNoPerms() {
        return noPerms;
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }
}
