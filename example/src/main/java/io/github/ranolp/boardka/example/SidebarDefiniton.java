package io.github.ranolp.boardka.example;

import io.github.ranolp.boardka.api.SidebarLines;
import io.github.ranolp.boardka.api.sidebar.ByPlayerLineRenderer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

final class SidebarDefiniton {
    static final SidebarLines LINES = SidebarLines.newInstance()
            // Constant. It'll never change.
            .appendConstant(ChatColor.AQUA, ChatColor.BOLD, "Players")
            // Global. It updates on render request.
            .appendGlobal(() -> ChatColor.GRAY.toString() + Bukkit.getOnlinePlayers().size())
            // equals to appendConstant("")
            .appendEmptyLine()
            .appendConstant(ChatColor.AQUA, ChatColor.BOLD, "Health")
            // Custom line renderer
            .append(new HealthLineRenderer())
            .appendEmptyLine()
            // by-player line renderer
            .appendByPlayerLine(player -> ChatColor.AQUA + "Level " + ChatColor.RESET + player.getLevel());

    // Custom renderer implementation
    private static final class HealthLineRenderer extends ByPlayerLineRenderer {
        @Nonnull
        @Override
        public String compute(@Nonnull Player player) {
            switch (player.getGameMode()) {
                case CREATIVE:
                    return "Creative";
                case SPECTATOR:
                    return "Spectator";
                default:
                    return Math.round(player.getHealth()) + " / " + Math.round(
                            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            }
        }
    }
}
