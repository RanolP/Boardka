package io.github.ranolp.boardka;

import org.bukkit.plugin.java.JavaPlugin;

public final class BoardkaPlugin extends JavaPlugin {
    private static BoardkaPlugin instance;

    public static BoardkaPlugin getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Boardka not initialized");
        }
        return instance;
    }

    @Override
    public void onLoad() {
        BoardkaPlugin.instance = this;
    }
}
