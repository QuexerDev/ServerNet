
package me.quexer.server.serverapi;

import me.quexer.api.quexerapi.misc.uuid.UUIDFetcher;
import me.quexer.server.serverapi.callbacks.CheckPlayerCallback;
import me.quexer.server.serverapi.commands.*;
import me.quexer.server.serverapi.listeners.ServerListeners;
import me.quexer.server.serverapi.manager.InventoryManager;
import me.quexer.server.serverapi.listeners.PlayerListeners;
import me.quexer.server.serverapi.utils.TablistManager;
import me.quexer.server.serverplugin.ServerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class ServerAPI extends ServerPlugin {

    private TablistManager tablistManager;
    private PlayerListeners playerListeners;
    private InventoryManager inventoryManager;

    @Override
    public void init() {
        tablistManager = new TablistManager(this);
        playerListeners = new PlayerListeners(this);
        inventoryManager = new InventoryManager(this);
        new ServerListeners(this);



        initCommands();

    }

    private void initCommands() {
        new CoinsCMD(this);
        new GamemodeCMD(this);
        new HelpCMD(this);
        new TeleportCMD(this);
        new BanCMD(this);
        new CheckCMD(this);
        new UnBanCMD(this);
    }

    @Override
    public void disable() {

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

    @Override
    public String getPrefix() {
        return "§6§lServer §8➜ ";
    }

    public TablistManager getTablistManager() {
        return tablistManager;
    }

    public PlayerListeners getPlayerListeners() {
        return playerListeners;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }
}
