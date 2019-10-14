package me.quexer.server.bungeeserverperms;


import com.google.gson.Gson;
import de.dytanic.cloudnet.api.CloudAPI;
import de.dytanic.cloudnet.bridge.event.proxied.ProxiedSubChannelMessageEvent;
import de.dytanic.cloudnet.lib.utility.document.Document;
import me.quexer.server.bungeeserverperms.commands.GroupCMD;
import me.quexer.server.bungeeserverperms.database.MongoManager;
import me.quexer.server.bungeeserverperms.listeners.PlayerConnectListener;
import me.quexer.server.bungeeserverperms.manager.GroupManager;
import me.quexer.server.bungeeserverperms.manager.GroupPlayerManager;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.ExecutorService;

public final class ServerPerms extends Plugin implements Listener {


    private MongoManager mongoManager;
    private static ServerPerms instance;
    private Gson gson;
    private ExecutorService executor;
    private String prefix;
    private String error;
    private String noPerms;
    private GroupManager groupManager;
    private GroupPlayerManager groupPlayerManager;

    @Override
    public void onEnable() {

        instance = this;
        mongoManager = new MongoManager("localhost", 27017, "ServerNET");
        prefix = "§6§lPerms §8➜ ";
        error = "§4§lError §8➜ ";
        noPerms = "§4§lPerms §8➜ §7Dazu hast du keine Rechte§8!";
        gson = new Gson();
        groupManager = new GroupManager(this);
        groupPlayerManager = new GroupPlayerManager(this);
        System.out.println("ENABELING");
        getProxy().getPluginManager().registerCommand(this, new GroupCMD(this));
        getProxy().getPluginManager().registerListener(this, new PlayerConnectListener(this));
        getProxy().getPluginManager().registerListener(this, this);

    }

   /* @EventHandler
    public void onIncomeMessage(ProxiedSubChannelMessageEvent event) {
        System.out.println(event.getChannel()+";"+event.getMessage()+";"+event.getDocument().getString("uuid"));
        if(event.getChannel().equalsIgnoreCase("group") && event.getMessage().equalsIgnoreCase("getPlayer")) {
            CloudAPI.getInstance().sendCustomSubServerMessage("group", "getPlayer", event.getDocument().append("group", "ADMIN"));
            System.out.println("SENT");
        }
    }
    */

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public static ServerPerms getInstance() {
        return instance;
    }

    public Gson getGson() {
        return gson;
    }

    public ExecutorService getExecutor() {
        return executor;
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

    public GroupPlayerManager getGroupPlayerManager() {
        return groupPlayerManager;
    }


}
