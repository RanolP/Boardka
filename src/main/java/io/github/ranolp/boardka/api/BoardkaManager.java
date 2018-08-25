package io.github.ranolp.boardka.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class BoardkaManager {
    private BoardkaManager() {
        throw new UnsupportedOperationException("You cannot instantiate BoardkaManager");
    }

    public static void render(Player player) {
        Sidebar sidebar = Boardka.getScoreboard(player).sidebar();
        if (sidebar._lines == null) {
            return;
        }
        sidebar._lines._render(Collections.singleton(player));
    }

    public static void render(Player... players) {
        render(Arrays.asList(players));
    }

    public static void render(Collection<? extends Player> players) {
        Map<SidebarLines, List<Player>> map = new HashMap<>();
        for (Player it : players) {
            map.computeIfAbsent(Boardka.getScoreboard(it).sidebar()._lines, k -> new ArrayList<>()).add(it);
        }
        for (Map.Entry<SidebarLines, List<Player>> entry : map.entrySet()) {
            SidebarLines key = entry.getKey();
            List<Player> value = entry.getValue();
            key._render(new HashSet<>(value));
        }
    }

    public static void renderAll() {
        render(Bukkit.getOnlinePlayers());
    }
}
