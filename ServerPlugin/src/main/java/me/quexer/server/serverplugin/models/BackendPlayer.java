package me.quexer.server.serverplugin.models;

import com.sun.nio.zipfs.ZipFileSystem;
import me.quexer.server.serverplugin.enums.ChestType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class BackendPlayer {


    private String uuid;
    private Data data;
    private LobbyPlayer lobbyPlayer;

    public BackendPlayer(String uuid) {
        this.uuid = uuid;

        data = new Data();
        lobbyPlayer = new LobbyPlayer();

    }

    public static class Data {

        private long coins;
        private long keys;
        private long cpr;
        private long elo;
        private boolean nick;

        public Data() {
            coins = 10000;
            keys = 3;
            cpr = new Random().nextInt(200) + 300;
            elo = 1000;
            nick = false;
        }

        public long getCoins() {
            return coins;
        }

        public void setCoins(long coins) {
            this.coins = coins;
        }

        public long getKeys() {
            return keys;
        }

        public void setKeys(long keys) {
            this.keys = keys;
        }

        public long getCpr() {
            return cpr;
        }

        public void setCpr(long cpr) {
            this.cpr = cpr;
        }

        public long getElo() {
            return elo;
        }

        public void setElo(long elo) {
            this.elo = elo;
        }

        public boolean isNick() {
            return nick;
        }

        public void setNick(boolean nick) {
            this.nick = nick;
        }
    }

    public static class LobbyPlayer {

        private List<String> gadgets;
        private HashMap<ChestType, Integer> chests;

        public LobbyPlayer() {
            gadgets = new ArrayList<>();
            chests = new HashMap<>();
        }

        public List<String> getGadgets() {
            return gadgets;
        }

        public HashMap<ChestType, Integer> getChests() {
            return chests;
        }
    }

    public String getUuid() {
        return uuid;
    }

    public Data getData() {
        return data;
    }

    public LobbyPlayer getLobbyPlayer() {
        return lobbyPlayer;
    }
}
