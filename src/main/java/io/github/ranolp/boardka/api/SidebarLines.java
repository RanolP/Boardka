package io.github.ranolp.boardka.api;

import io.github.ranolp.boardka.api.sidebar.LineRenderer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Scoreboard sidebar lines data object.
 */
public final class SidebarLines {
    private final List<LineRenderer> lines = new ArrayList<>();

    private SidebarLines() {
    }

    /**
     * Create new instance.
     *
     * @return new instance
     */
    @Nonnull
    public static SidebarLines newInstance() {
        return new SidebarLines();
    }

    /**
     * Append custom line renderer.
     *
     * @param lineRenderer
     *         custom line renderer
     * @return self
     */
    @Nonnull
    public SidebarLines append(@Nonnull LineRenderer lineRenderer) {
        Objects.requireNonNull(lineRenderer, "lineRenderer");
        lines.add(lineRenderer);
        return this;
    }

    /**
     * Append empty line.
     *
     * @return self
     */
    @Nonnull
    public SidebarLines appendEmptyLine() {
        return appendConstant("");
    }

    /**
     * Append constant text.
     *
     * @param texts
     *         texts
     * @return self
     */
    @Nonnull
    public SidebarLines appendConstant(@Nonnull Object... texts) {
        Objects.requireNonNull(texts, "texts");
        StringBuilder builder = new StringBuilder();
        for (Object text : texts) {
            builder.append(text);
        }
        return appendConstant(builder.toString());
    }

    /**
     * Append constant text.
     *
     * @param texts
     *         texts
     * @return self
     */
    @Nonnull
    public SidebarLines appendConstant(@Nonnull String... texts) {
        return appendConstant(String.join("", Objects.requireNonNull(texts, "texts")));
    }

    /**
     * Append constant text.
     *
     * @param text
     *         text
     * @return self
     */
    @Nonnull
    public SidebarLines appendConstant(@Nullable String text) {
        return append(LineRenderer.constant().text(text).build());
    }

    /**
     * Append global line renderer
     *
     * @param renderer
     *         the renderer
     * @return self
     */
    @Nonnull
    public SidebarLines appendGlobal(@Nonnull Supplier<String> renderer) {
        return append(LineRenderer.global().renderBy(renderer).build());
    }

    /**
     * Append player specific line renderer
     *
     * @param renderer
     *         the renderer
     * @return self
     */
    @Nonnull
    public SidebarLines appendByPlayerLine(@Nonnull Function<Player, String> renderer) {
        return append(LineRenderer.byPlayer().renderBy(renderer).build());
    }

    /*********************************************************
     *                                                       *
     *                      ! WARNING !                      *
     *     Following methods, fields are used internally     *
     *             You MUST NOT use the methods.             *
     *                                                       *
     *********************************************************/

    void _render(Set<Player> targets) {
        Map<Player, List<String>> texts = new HashMap<>();

        for (Player target : targets) {
            texts.put(target, new ArrayList<>());
        }

        for (LineRenderer renderer : lines) {
            for (Map.Entry<Player, String> entry : renderer.render(targets).entrySet()) {
                texts.get(entry.getKey()).add(entry.getValue());
            }
        }

        for (Map.Entry<Player, List<String>> entry : texts.entrySet()) {
            Player player = entry.getKey();
            List<String> value = entry.getValue();
            _PlayerBoardka scoreboard = (_PlayerBoardka) Boardka.of(player);
            _PlayerSidebar sidebar = (_PlayerSidebar) scoreboard.sidebar();
            sidebar._requestSize(value.size());
            Stack<Score> scores = sidebar._scores;
            for (int i = 0; i < scores.size(); i++) {
                Score score = scores.get(i);
                String line = value.get(i);
                if (line.length() > 28) {
                    Bukkit.getLogger().severe(String.format(
                            "Can't renderSidebar scoreboard sidebar for player '%s' on line %d. data is %s",
                            player.getName(), i, line
                    ));
                    line = "CAN'T RENDER THIS";
                }
                Team team = scoreboard._handle().getTeam(score.getEntry());
                if (line.length() > 16) {
                    int target;
                    if (line.charAt(15) == ChatColor.COLOR_CHAR) {
                        target = 14;
                    } else if (line.charAt(16) == ChatColor.COLOR_CHAR) {
                        target = 15;
                    } else {
                        target = 16;
                    }
                    String left = line.substring(0, target);
                    team.setPrefix(left);
                    team.setSuffix(ChatColor.getLastColors(left) + line.substring(target));
                } else {
                    team.setPrefix(line);
                    team.setSuffix("");
                }
            }
        }
    }

    @Override
    public String toString() {
        return "SidebarLines(" + lines.size() + " element" + (lines.size() == 1 ? "" : "s") + ")";
    }
}
