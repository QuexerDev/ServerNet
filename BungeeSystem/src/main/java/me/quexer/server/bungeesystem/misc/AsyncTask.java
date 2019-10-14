package me.quexer.server.bungeesystem.misc;


import me.quexer.server.bungeesystem.BungeeSystem;

public class AsyncTask {


    public AsyncTask(Runnable run) {
        BungeeSystem.getInstance().getExecutor().execute(run);
    }
}
