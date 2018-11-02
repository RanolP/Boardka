package io.github.ranolp.boardka.api;

import org.bukkit.plugin.Plugin;

public abstract class Sidebar {
    Sidebar() {
    }

    public abstract boolean isShowing();

    public abstract Sidebar title(String title);

    public abstract Sidebar apply(SidebarLines lines);

    public abstract void update();

    public abstract void updateLater(Plugin plugin, long delay);

    public abstract void updateLater(Plugin plugin);

    public abstract void updateLater(long delay);

    public abstract void updateLater();

    public abstract void show();

    public abstract void hide();
}
