package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class GroupedLineRenderer<T> extends LineRenderer {
    @Nonnull
    public abstract T group(@Nonnull Player player);

    @Nonnull
    protected abstract Map<Player, String> render(@Nonnull T object, @Nonnull Set<Player> player);

    @Nonnull
    @Override
    public final Map<Player, String> render(@Nonnull Set<Player> players) {
        Map<T, List<Player>> map = new HashMap<>();
        for (Player player : players) {
            map.computeIfAbsent(group(player), k -> new ArrayList<>()).add(player);
        }

        Map<Player, String> result = new HashMap<>();
        for (Map.Entry<T, List<Player>> groupEntry : map.entrySet()) {
            Map<Player, String> computed = render(groupEntry.getKey(), new HashSet<>(groupEntry.getValue()));
            for (Map.Entry<Player, String> lineEntry : computed.entrySet()) {
                result.put(lineEntry.getKey(), lineEntry.getValue());
            }
        }

        return result;
    }
}
