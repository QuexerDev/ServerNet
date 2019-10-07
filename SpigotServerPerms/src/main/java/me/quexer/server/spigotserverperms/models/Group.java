package me.quexer.server.spigotserverperms.models;

import java.util.stream.Collectors;

public class Group {

    private String full;
    private String prefix;
    private String color;
    private int levelID;
    private int tabID;

    public Group(String full, String prefix, String color, int levelID, int tabID) {
        this.full = full;
        this.prefix = prefix;
        this.color = color;
        this.levelID = levelID;
        this.tabID = tabID;
    }



    public String getPrefix() {
        return prefix;
    }

    public String getColor() {
        return color;
    }

    public int getLevelID() {
        return levelID;
    }

    public int tabID() {
        return tabID;
    }

    public boolean hasPermission(int level) {
        return (levelID >= level);
    }

    public String getFullName() {
        return full;
    }

    public String getTabID() {
        return "00"+levelID;
    }

}
