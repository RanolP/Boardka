package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SidebarLineBuilder {
    public abstract LineRenderer build();

    public static class Constant extends SidebarLineBuilder {
        private String text;

        public Constant text(String text) {
            this.text = text;
            return this;
        }

        @Override
        public LineRenderer build() {
            return new ConstantLineRenderer(text);
        }
    }

    public static class Global extends SidebarLineBuilder {
        private Supplier<String> computer;

        public Global text(String text) {
            this.computer = () -> text;
            return this;
        }

        public Global computeBy(Supplier<String> computer) {
            this.computer = computer;
            return this;
        }

        @Override
        public LineRenderer build() {
            return new GlobalLineRenderer$Builded(computer);
        }

        private static final class GlobalLineRenderer$Builded extends GlobalLineRenderer {
            private final Supplier<String> computer;

            GlobalLineRenderer$Builded(Supplier<String> computer) {
                this.computer = computer;
            }

            @Nonnull
            @Override
            public String compute() {
                return computer.get();
            }
        }
    }

    public static class ByPlayer extends SidebarLineBuilder {
        private Function<Player, String> computer;

        public ByPlayer computeBy(Function<Player, String> computer) {
            this.computer = computer;
            return this;
        }

        @Override
        public LineRenderer build() {
            if (computer == null) {
                throw new IllegalStateException("Computer not set");
            }
            return new ByPlayer$Built(computer);
        }

        private static final class ByPlayer$Built extends ByPlayerLineRenderer {
            private final Function<Player, String> computer;

            ByPlayer$Built(Function<Player, String> computer) {
                this.computer = computer;
            }

            @Nonnull
            public String compute(@Nonnull Player player) {
                return computer.apply(player);
            }
        }
    }

    public static class PlayerlessGrouped<T> extends SidebarLineBuilder {
        private Function<Player, T> grouper;
        private Function<T, String> computer;

        public PlayerlessGrouped<T> groupBy(Function<Player, T> grouper) {
            this.grouper = grouper;
            return this;
        }

        public PlayerlessGrouped<T> computeBy(Function<T, String> computer) {
            this.computer = computer;
            return this;
        }

        @Override
        public LineRenderer build() {
            if (grouper == null) {
                throw new IllegalStateException("Grouper not set");
            }
            if (computer == null) {
                throw new IllegalStateException("Computer not set");
            }
            return new PlayerlessGroupedLineRenderer$Built<>(grouper, computer);
        }

        private static final class PlayerlessGroupedLineRenderer$Built<T> extends PlayerlessGroupedLineRenderer<T> {
            private final Function<Player, T> grouper;
            private final Function<T, String> computer;

            PlayerlessGroupedLineRenderer$Built(Function<Player, T> grouper, Function<T, String> computer) {
                this.grouper = grouper;
                this.computer = computer;
            }

            @Nonnull
            public T group(@Nonnull Player player) {
                return grouper.apply(player);
            }

            @Nonnull
            public String compute(@Nonnull T object) {
                return computer.apply(object);
            }
        }
    }

    public static class Grouped<T> extends SidebarLineBuilder {
        private Function<Player, T> grouper;
        private BiFunction<T, Set<Player>, Map<Player, String>> computer;

        public PlayerlessGrouped<T> playerless() {
            return new PlayerlessGrouped<T>().groupBy(grouper);
        }

        public Grouped<T> groupBy(Function<Player, T> grouper) {
            this.grouper = grouper;
            return this;
        }

        public Grouped<T> computeBy(BiFunction<T, Set<Player>, Map<Player, String>> computer) {
            this.computer = computer;
            return this;
        }

        @Override
        public LineRenderer build() {
            if (grouper == null) {
                throw new IllegalStateException("Grouper not set");
            }
            if (computer == null) {
                throw new IllegalStateException("Computer not set");
            }
            return new GroupedLineRenderer$Built<>(grouper, computer);
        }

        private static final class GroupedLineRenderer$Built<T> extends GroupedLineRenderer<T> {
            private final Function<Player, T> grouper;
            private final BiFunction<T, Set<Player>, Map<Player, String>> computer;

            GroupedLineRenderer$Built(Function<Player, T> grouper,
                    BiFunction<T, Set<Player>, Map<Player, String>> computer) {
                this.grouper = grouper;
                this.computer = computer;
            }

            @Nonnull
            public T group(@Nonnull Player player) {
                return grouper.apply(player);
            }

            @Nonnull
            public Map<Player, String> compute(@Nonnull T object, @Nonnull Set<Player> player) {
                return computer.apply(object, player);
            }
        }
    }
}
