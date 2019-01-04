package io.github.ranolp.boardka.api;

import org.bukkit.plugin.Plugin;

/**
 * Scoreboard sidebar manager class.
 */
public abstract class Sidebar {
    Sidebar() {
    }

    /**
     * Check sidebar is showing.
     *
     * @return is showing
     */
    public abstract boolean isShowing();

    /**
     * Set the title of sidebar. title must be shorter than 128 characters(in 1.13, other versions are not tested).
     *
     * @param title
     *         the title to set
     * @return self
     */
    public abstract Sidebar title(String title);

    /**
     * Set the sidebar lines.
     *
     * @param lines
     *         the lines to set
     * @return self
     */
    public abstract Sidebar apply(SidebarLines lines);

    /**
     * Request sidebar updating.
     */
    public abstract void update();

    /**
     * Request sidebar updating later.
     *
     * @param plugin
     *         the requester
     * @param delay
     *         delay
     */
    public abstract void updateLater(Plugin plugin, long delay);

    /**
     * Request sidebar updating later.
     *
     * @param plugin
     *         the requester
     */
    public abstract void updateLater(Plugin plugin);

    /**
     * Request sidebar updating later.
     *
     * @param delay
     *         delay
     */
    public abstract void updateLater(long delay);

    /**
     * Request sidebar updating later(delays 1 tick).
     */
    public abstract void updateLater();

    public abstract void show();

    public abstract void hide();
}
