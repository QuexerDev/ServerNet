package me.quexer.server.bungeesystem.models;

import me.quexer.server.bungeesystem.enums.FriendOption;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackendPlayer {

    private String uuid;
    private BanPlayer banPlayer;
    private BanPlayer mutePlayer;
    private FriendPlayer friendPlayer;
    private Date date;

    public BackendPlayer(String uuid) {
        this.uuid = uuid;
        banPlayer = new BanPlayer();
        mutePlayer = new BanPlayer();
        friendPlayer = new FriendPlayer();
        date = new Date();
    }

    public static class FriendPlayer {

        private List<String> requests;
        private List<String> friends;
        private List<String> blocked;
        private HashMap<FriendOption, Boolean> settings;

        public FriendPlayer() {
            requests = new ArrayList<>();
            friends = new ArrayList<>();
            blocked = new ArrayList<>();
            settings = new HashMap<>();
        }

        public List<String> getRequests() {
            return requests;
        }

        public void setRequests(List<String> requests) {
            this.requests = requests;
        }

        public List<String> getFriends() {
            return friends;
        }

        public void setFriends(List<String> friends) {
            this.friends = friends;
        }

        public List<String> getBlocked() {
            return blocked;
        }

        public void setBlocked(List<String> blocked) {
            this.blocked = blocked;
        }

        public HashMap<FriendOption, Boolean> getSettings() {
            return settings;
        }

        public void setSettings(HashMap<FriendOption, Boolean> settings) {
            this.settings = settings;
        }
    }

    public static class Date {
        private long created_at;
        private long lastLogin;
        private long lastOffline;

        public Date() {
            created_at = System.currentTimeMillis();
            lastLogin = System.currentTimeMillis();
            lastOffline = System.currentTimeMillis();
        }

        public long getCreated_at() {
            return created_at;
        }

        public long getLastLogin() {
            return lastLogin;
        }

        public long getLastOffline() {
            return lastOffline;
        }

        public void setCreated_at(long created_at) {
            this.created_at = created_at;
        }

        public void setLastLogin(long lastLogin) {
            this.lastLogin = lastLogin;
        }

        public void setLastOffline(long lastOffline) {
            this.lastOffline = lastOffline;
        }
    }

    public static class BanPlayer {

        private boolean isPunished;
        private String reason;
        private long end;
        private long punished_at;
        private int banPoints;
        private List<String> history;
        private String punished_from;

        public BanPlayer() {
            isPunished = false;
            reason = "NONE";
            end = 0;
            punished_at = 0;
            banPoints = 1;
            history = new ArrayList<>();
            punished_from = "NONE";
        }

        public boolean isPunished() {
            return isPunished;
        }

        public String getReason() {
            return reason;
        }

        public long getEnd() {
            return end;
        }

        public long getPunished_at() {
            return punished_at;
        }

        public List<String> getHistory() {
            return history;
        }

        public String getPunished_from() {
            return punished_from;
        }

        public void setPunished(boolean punished) {
            isPunished = punished;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public void setEnd(long end) {
            this.end = end;
        }

        public void setPunished_at(long punished_at) {
            this.punished_at = punished_at;
        }

        public void setHistory(List<String> history) {
            this.history = history;
        }

        public void setPunished_from(String punished_from) {
            this.punished_from = punished_from;
        }

        public int getBanPoints() {
            return banPoints;
        }

        public void setBanPoints(int banPoints) {
            this.banPoints = banPoints;
        }
    }

    public String getUuid() {
        return uuid;
    }

    public BanPlayer getBanPlayer() {
        return banPlayer;
    }

    public BanPlayer getMutePlayer() {
        return mutePlayer;
    }

    public FriendPlayer getFriendPlayer() {
        return friendPlayer;
    }

    public Date getDate() {
        return date;
    }
}
