package io.github.ranolp.boardka.api.sidebar;

import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class SidebarLineBuilder {
    /**
     * Builds line renderFunction
     *
     * @return built line renderFunction
     */
    @Nonnull
    public abstract LineRenderer build();

    /**
     * Constant sidebar line builder
     */
    public static class Constant extends SidebarLineBuilder {
        private String text;

        /**
         * Set text.
         *
         * @param text
         *         text
         * @return self
         */
        @Nonnull
        public Constant text(@Nullable String text) {
            this.text = text;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Nonnull
        public LineRenderer build() {
            return new ConstantLineRenderer(text);
        }
    }

    public static class Global extends SidebarLineBuilder {
        private Supplier<String> renedrFunction;

        /**
         * Set text.
         *
         * @param text
         *         text
         * @return self
         */
        @Nonnull
        public Global text(String text) {
            this.renedrFunction = () -> text;
            return this;
        }

        /**
         * Set render function.
         *
         * @param renderFunction
         *         render function
         * @return self
         */
        @Nonnull
        public Global renderBy(Supplier<String> renderFunction) {
            this.renedrFunction = renderFunction;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Nonnull
        public LineRenderer build() {
            return new GlobalLineRenderer$Builded(renedrFunction);
        }

        private static final class GlobalLineRenderer$Builded extends GlobalLineRenderer {
            private final Supplier<String> renderFunction;

            GlobalLineRenderer$Builded(Supplier<String> renderFunction) {
                this.renderFunction = renderFunction;
            }

            @Nonnull
            @Override
            public String render() {
                return renderFunction.get();
            }
        }
    }

    public static class ByPlayer extends SidebarLineBuilder {
        private Function<Player, String> renderer;

        /**
         * Set render function.
         *
         * @param renderFunction
         *         render function
         * @return self
         */
        @Nonnull
        public ByPlayer renderBy(Function<Player, String> renderFunction) {
            this.renderer = renderFunction;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Nonnull
        public LineRenderer build() {
            if (renderer == null) {
                throw new IllegalStateException("Computer not set");
            }
            return new ByPlayer$Built(renderer);
        }

        private static final class ByPlayer$Built extends ByPlayerLineRenderer {
            private final Function<Player, String> renderer;

            ByPlayer$Built(Function<Player, String> renderer) {
                this.renderer = renderer;
            }

            @Nonnull
            public String render(@Nonnull Player player) {
                return renderer.apply(player);
            }
        }
    }

    public static class GroupedValueBased<T> extends SidebarLineBuilder {
        private Function<Player, T> groupFunction;
        private Function<T, String> renderFunction;

        /**
         * Set group function.
         *
         * @param groupFunction
         *         group function
         * @return self
         */
        @Nonnull
        public GroupedValueBased<T> groupBy(Function<Player, T> groupFunction) {
            this.groupFunction = groupFunction;
            return this;
        }

        /**
         * Set render function.
         *
         * @param renderFunction
         *         render function
         * @return self
         */
        @Nonnull
        public GroupedValueBased<T> renderBy(Function<T, String> renderFunction) {
            this.renderFunction = renderFunction;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Nonnull
        public LineRenderer build() {
            if (groupFunction == null) {
                throw new IllegalStateException("Grouper not set");
            }
            if (renderFunction == null) {
                throw new IllegalStateException("Computer not set");
            }
            return new GroupedValueBasedLineRenderer$Built<>(groupFunction, renderFunction);
        }

        private static final class GroupedValueBasedLineRenderer$Built<T> extends GroupedValueBasedLineRenderer<T> {
            private final Function<Player, T> groupFunction;
            private final Function<T, String> renderFunction;

            GroupedValueBasedLineRenderer$Built(Function<Player, T> groupFunction, Function<T, String> renderFunction) {
                this.groupFunction = groupFunction;
                this.renderFunction = renderFunction;
            }

            @Nonnull
            public T group(@Nonnull Player player) {
                return groupFunction.apply(player);
            }

            @Nonnull
            public String compute(@Nonnull T object) {
                return renderFunction.apply(object);
            }
        }
    }

    public static class Grouped<T> extends SidebarLineBuilder {
        private Function<Player, T> groupFunction;
        private BiFunction<T, Set<Player>, Map<Player, String>> renderFunction;

        /**
         * Create grouped value based builder instance.
         *
         * @return new instance
         */
        @Nonnull
        public GroupedValueBased<T> groupedValueBased() {
            return new GroupedValueBased<T>().groupBy(groupFunction);
        }

        /**
         * Set group function.
         *
         * @param groupFunction
         *         group function
         * @return self
         */
        @Nonnull
        public Grouped<T> groupBy(Function<Player, T> groupFunction) {
            this.groupFunction = groupFunction;
            return this;
        }

        /**
         * Set render function.
         *
         * @param renderFunction
         *         render function
         * @return self
         */
        @Nonnull
        public Grouped<T> renderBy(BiFunction<T, Set<Player>, Map<Player, String>> renderFunction) {
            this.renderFunction = renderFunction;
            return this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        @Nonnull
        public LineRenderer build() {
            if (groupFunction == null) {
                throw new IllegalStateException("Grouper not set");
            }
            if (renderFunction == null) {
                throw new IllegalStateException("Computer not set");
            }
            return new GroupedLineRenderer$Built<>(groupFunction, renderFunction);
        }

        private static final class GroupedLineRenderer$Built<T> extends GroupedLineRenderer<T> {
            private final Function<Player, T> groupFunction;
            private final BiFunction<T, Set<Player>, Map<Player, String>> renderFunction;

            GroupedLineRenderer$Built(Function<Player, T> groupFunction,
                    BiFunction<T, Set<Player>, Map<Player, String>> renderFunction) {
                this.groupFunction = groupFunction;
                this.renderFunction = renderFunction;
            }

            @Nonnull
            public T group(@Nonnull Player player) {
                return groupFunction.apply(player);
            }

            @Nonnull
            public Map<Player, String> render(@Nonnull T object, @Nonnull Set<Player> player) {
                return renderFunction.apply(object, player);
            }
        }
    }
}
