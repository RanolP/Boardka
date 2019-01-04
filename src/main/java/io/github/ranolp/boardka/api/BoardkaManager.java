package io.github.ranolp.boardka.api;

import io.github.ranolp.boardka.BoardkaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * A little manager for Boardka.
 */
public final class BoardkaManager {
    private BoardkaManager() {
        throw new UnsupportedOperationException("You cannot instantiate BoardkaManager");
    }

    /**
     * Request scoreboard rendering.
     *
     * @param player
     *         the target player
     */
    public static void render(@Nonnull Player player) {
        renderSidebar(player);
    }

    /**
     * Request sidebar rendering.
     *
     * @param player
     *         the target player
     */
    public static void renderSidebar(@Nonnull Player player) {
        _PlayerSidebar sidebar = (_PlayerSidebar) Boardka.of(player).sidebar();
        if (sidebar._lines == null) {
            return;
        }
        sidebar._lines._render(Collections.singleton(player));
    }

    /**
     * Request scoreboard rendering.
     *
     * @param players
     *         the target players
     */
    public static void render(Player... players) {
        renderSidebar(players);
    }

    /**
     * Request sidebar rendering.
     *
     * @param players
     *         the target players
     */
    public static void renderSidebar(Collection<? extends Player> players) {
        Map<SidebarLines, List<Player>> map = new HashMap<>();
        for (Player player : players) {
            SidebarLines sidebarLines = ((_PlayerSidebar) Boardka.of(player).sidebar())._lines;
            if (sidebarLines == null) {
                continue;
            }
            if (!map.containsKey(sidebarLines)) {
                map.put(sidebarLines, new ArrayList<>());
            }
            map.get(sidebarLines).add(player);
        }
        for (Map.Entry<SidebarLines, List<Player>> entry : map.entrySet()) {
            entry.getKey()._render(new HashSet<>(entry.getValue()));
        }
    }

    /**
     * Request sidebar rendering
     *
     * @param players
     *         the target players
     */
    public static void renderSidebar(Player... players) {
        renderSidebar(Arrays.asList(players));
    }

    /**
     * Request scoreboard rendering.
     *
     * @param players
     *         the target players
     */
    public static void render(Collection<? extends Player> players) {
        renderSidebar(players);
    }

    /**
     * Request scoreboard rendering for all players.
     */
    public static void renderAll() {
        renderAll();
    }

    /**
     * Request scoreboard rendering for all players.
     */
    public static void renderAllLater() {
        renderAllLater(BoardkaPlugin.getInstance());
    }

    /**
     * Request scoreboard rendering for all players.
     */
    public static void renderAllLater(Plugin requester) {
        new BukkitRunnable() {
            @Override
            public void run() {
                renderAllSidebars();
            }
        }.runTaskLater(requester, 0);
    }

    /**
     * Request sidebar rendering for all players.
     */
    public static void renderAllSidebarsLater() {
        renderAllSidebarsLater(BoardkaPlugin.getInstance());
    }

    /**
     * Request sidebar rendering for all players.
     */
    public static void renderAllSidebarsLater(Plugin requester) {
        new BukkitRunnable() {
            @Override
            public void run() {
                renderAllSidebars();
            }
        }.runTaskLater(requester, 0);
    }

    /**
     * Request sidebar rendering for all players.
     */
    public static void renderAllSidebars() {
        renderSidebar(Bukkit.getOnlinePlayers());
    }
}
