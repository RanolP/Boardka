package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class ByPlayerLineRenderer extends LineRenderer {
    @Nonnull
    public abstract String render(@Nonnull Player player);

    protected void startRender() {
    }

    protected void endRender() {
    }

    @Nonnull
    @Override
    public final Map<Player, String> render(@Nonnull Set<Player> players) {
        startRender();
        Map<Player, String> result = new HashMap<>();
        for (Player it : players) {
            result.put(it, render(it));
        }
        endRender();
        return result;
    }
}
