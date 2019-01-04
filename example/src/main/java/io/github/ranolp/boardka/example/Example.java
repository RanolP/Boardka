package io.github.ranolp.boardka.example;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Example extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SidebarUpdateListener(), this);
    }
}
