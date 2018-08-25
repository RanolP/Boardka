package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public final class ConstantLineRenderer extends ByPlayerLineRenderer {
    private final String text;

    public ConstantLineRenderer(@Nullable String text) {
        this.text = Objects.toString(text, "");
    }

    @Nonnull
    @Override
    public String compute(Player player) {
        return text;
    }
}
