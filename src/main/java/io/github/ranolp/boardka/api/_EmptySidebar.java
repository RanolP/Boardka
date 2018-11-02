package io.github.ranolp.boardka.api;

import org.bukkit.plugin.Plugin;

final class _EmptySidebar extends Sidebar {
    private _EmptySidebar() {
    }

    @Override
    public boolean isShowing() {
        return false;
    }

    @Override
    public Sidebar title(String title) {
        return this;
    }

    @Override
    public Sidebar apply(SidebarLines lines) {
        return this;
    }

    @Override
    public void update() {
        // do nothing
    }

    @Override
    public void updateLater(Plugin plugin, long delay) {
        // do nothing
    }

    @Override
    public void updateLater(Plugin plugin) {
        // do nothing
    }

    @Override
    public void updateLater(long delay) {
        // do nothing
    }

    @Override
    public void updateLater() {
        // do nothing
    }

    @Override
    public void show() {
        // do nothing
    }

    @Override
    public void hide() {
        // do nothing
    }

    @Override
    public String toString() {
        return "EmptyBoardka.EmptySidebar";
    }

    static final class SingletonHolder {
        static final _EmptySidebar INSTANCE = new _EmptySidebar();
    }
}
