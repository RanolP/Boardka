package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ByPlayerLineRenderer extends LineRenderer {
    @Nonnull
    public abstract String compute(@Nonnull Player player);

    protected void startCompute() {
    }

    protected void endCompute() {
    }

    @Nonnull
    @Override
    public final Map<Player, String> compute(@Nonnull Set<Player> players) {
        startCompute();
        Map<Player, String> result = new HashMap<>();
        for (Player it : players) {
            result.put(it, compute(it));
        }
        endCompute();
        return result;
    }
}
