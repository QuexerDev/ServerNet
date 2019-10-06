package me.quexer.server.serverplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class ServerPlugin extends JavaPlugin {

    private static ServerPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static ServerPlugin getInstance() {
        return instance;
    }
}
