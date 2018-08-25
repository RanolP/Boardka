package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GlobalLineRenderer extends LineRenderer {
    @Nonnull
    public abstract String compute();

    @Nonnull
    @Override
    public final Map<Player, String> compute(@Nonnull Set<Player> players) {
        String data = compute();

        Map<Player, String> map = new HashMap<>();
        for (Player it : players) {
            map.put(it, data);
        }
        return map;
    }
}
