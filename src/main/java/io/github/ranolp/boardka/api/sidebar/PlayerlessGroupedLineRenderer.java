package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class PlayerlessGroupedLineRenderer<T> extends GroupedLineRenderer<T> {
    @Nonnull
    public abstract String compute(@Nonnull T object);

    @Nonnull
    @Override
    protected Map<Player, String> compute(@Nonnull T object, @Nonnull Set<Player> player) {
        String data = compute(object);
        Map<Player, String> map = new HashMap<>();
        for (Player it : player) {
            map.put(it, data);
        }
        return map;
    }
}
