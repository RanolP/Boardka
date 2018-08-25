package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public abstract class LineRenderer {
    @Nonnull
    public abstract Map<Player, String> compute(@Nonnull Set<Player> players);

    @Nonnull
    public static SidebarLineBuilder.Constant constant() {
        return new SidebarLineBuilder.Constant();
    }

    @Nonnull
    public static SidebarLineBuilder.Global global() {
        return new SidebarLineBuilder.Global();
    }

    @Nonnull
    public static <T> SidebarLineBuilder.Grouped<T> grouped() {
        return new SidebarLineBuilder.Grouped<>();
    }
}
