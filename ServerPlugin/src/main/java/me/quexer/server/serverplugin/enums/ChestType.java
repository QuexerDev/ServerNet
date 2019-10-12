package me.quexer.server.serverplugin.enums;

public enum ChestType {

    COMMON("§7§lCommon"),
    RARE("§b§lRare"),
    EPIC("§5§lEpic"),
    ULTIMATE("§6§lULTIMATE");

    private String name;

    ChestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
