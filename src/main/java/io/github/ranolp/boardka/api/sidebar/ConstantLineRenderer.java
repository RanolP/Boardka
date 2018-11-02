package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class ConstantLineRenderer extends LineRenderer {
    private final String text;

    public ConstantLineRenderer(@Nullable String text) {
        this.text = Objects.toString(text, "");
    }

    @Nonnull
    @Override
    public Map<Player, String> compute(@Nonnull Set<Player> players) {
        Map<Player, String> result = new HashMap<>();
        for (Player it : players) {
            result.put(it, text);
        }
        return result;
    }
}
