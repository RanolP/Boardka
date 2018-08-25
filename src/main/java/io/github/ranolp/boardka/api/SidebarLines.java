package io.github.ranolp.boardka.api;

import io.github.ranolp.boardka.api.sidebar.LineRenderer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.function.Supplier;

public class SidebarLines {
    private final List<LineRenderer> lines = new ArrayList<>();

    private SidebarLines() {
    }

    public SidebarLines append(LineRenderer line) {
        lines.add(line);
        return this;
    }

    public SidebarLines appendEmptyLine() {
        return appendConstant("");
    }

    public SidebarLines appendConstant(Object... texts) {
        StringBuilder builder = new StringBuilder();
        for (Object text : texts) {
            builder.append(Objects.toString(text));
        }
        return appendConstant(builder.toString());
    }

    public SidebarLines appendConstant(String... texts) {
        return appendConstant(String.join("", texts));
    }

    public SidebarLines appendConstant(String text) {
        return append(LineRenderer.constant().text(text).build());
    }

    public SidebarLines appendGlobal(Supplier<String> computer) {
        return append(LineRenderer.global().computeBy(computer).build());
    }

    public static SidebarLines newInstance() {
        return new SidebarLines();
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
            for (Map.Entry<Player, String> entry : renderer.compute(targets).entrySet()) {
                texts.get(entry.getKey()).add(entry.getValue());
            }
        }

        for (Map.Entry<Player, List<String>> entry : texts.entrySet()) {
            Player player = entry.getKey();
            List<String> value = entry.getValue();
            Boardka scoreboard = Boardka.of(player);
            Sidebar sidebar = scoreboard.sidebar();
            sidebar._requestSize(value.size());
            Stack<Score> scores = sidebar._scores;
            for (int i = 0; i < scores.size(); i++) {
                Score score = scores.get(i);
                String line = value.get(i);
                if (line.length() > 28) {
                    Bukkit.getLogger().severe(String.format(
                            "Can't render scoreboard sidebar for player '%s' on line %d. data is %s",
                            player.getName(),
                            i,
                            line
                    ));
                    line = "CAN'T RENDER THIS";
                }
                Team team = scoreboard._handle().getTeam(score.getEntry());
                String current;
                if (team.getSuffix().isEmpty()) {
                    current = team.getPrefix();
                } else {
                    current = team.getPrefix() + team.getSuffix().substring(2);
                }
                if (line.equals(current)) {
                    continue;
                }
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
}
