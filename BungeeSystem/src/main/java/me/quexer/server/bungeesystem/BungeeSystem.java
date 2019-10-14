package me.quexer.server.bungeesystem;

import com.google.gson.Gson;
import me.quexer.server.bungeeserverperms.ServerPerms;
import me.quexer.server.bungeeserverperms.database.MongoManager;
import me.quexer.server.bungeeserverperms.manager.GroupManager;
import me.quexer.server.bungeesystem.listeners.PlayerChatListener;
import me.quexer.server.bungeesystem.listeners.PlayerConnectListener;
import me.quexer.server.bungeesystem.listeners.ProxyMessageListener;
import me.quexer.server.bungeesystem.manager.*;
import net.md_5.bungee.api.plugin.Plugin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class BungeeSystem extends Plugin {

    private MongoManager mongoManager;
    private static BungeeSystem instance;
    private String prefix;
    private String banPrefix;
    private Gson gson;
    private ExecutorService executor;

    private BackendManager backendManager;
    private BackendPlayerManager backendPlayerManager;
    private BanManager banManager;
    private MuteManager muteManager;
    private GroupManager groupManager;
    private ProxyMessageManager proxyMessageManager;
    private ReportManager reportManager;

    @Override
    public void onEnable() {
        instance = this;
        mongoManager = new MongoManager("localhost", 27017, "ServerNET");
        prefix = "§6§lProxy §8➜ ";
        banPrefix = "§4§lBanSystem §8➜ ";
        gson = new Gson();
        executor = Executors.newCachedThreadPool();

        backendManager = new BackendManager(this);
        backendPlayerManager = new BackendPlayerManager(this);
        banManager = new BanManager(this);
        muteManager = new MuteManager(this);
        groupManager = ServerPerms.getInstance().getGroupManager();
        proxyMessageManager = new ProxyMessageManager(this);
        reportManager = new ReportManager(this);

        getProxy().getPluginManager().registerListener(this, new PlayerConnectListener(this));
        getProxy().getPluginManager().registerListener(this, new PlayerChatListener(this));
        getProxy().getPluginManager().registerListener(this, new ProxyMessageListener(this));
    }

    @Override
    public void onDisable() {
        backendManager.getBackendPlayerCache().forEach((s, backendPlayer) -> {
            backendManager.savePlayer(backendPlayer);
        });
    }

    public String getDate(long millis) {
        if(millis == -1) {
            return "§4§lPERMANENT";
        } else {
            SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            Date now = new Date();
            now.setTime(millis);
            return sdfDate.format(now);
        }
    }

    public GroupManager getGroupManager() {
        return groupManager;
    }

    public MongoManager getMongoManager() {
        return mongoManager;
    }

    public static BungeeSystem getInstance() {
        return instance;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getBanPrefix() {
        return banPrefix;
    }

    public Gson getGson() {
        return gson;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public BackendManager getBackendManager() {
        return backendManager;
    }

    public BackendPlayerManager getBackendPlayerManager() {
        return backendPlayerManager;
    }

    public BanManager getBanManager() {
        return banManager;
    }

    public MuteManager getMuteManager() {
        return muteManager;
    }

    public ProxyMessageManager getProxyMessageManager() {
        return proxyMessageManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }
}
