package me.quexer.api.quexerapi.api;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class TestCallback implements Consumer<Player> {

    private Player player;

    public TestCallback(Player player) {
        this.player = player;
        accept(player);
    }

    @Override
    public void accept(Player player) {

    }


}
