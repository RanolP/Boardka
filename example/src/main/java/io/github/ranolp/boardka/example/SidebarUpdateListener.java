package io.github.ranolp.boardka.example;

import io.github.ranolp.boardka.api.Boardka;
import io.github.ranolp.boardka.api.BoardkaManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SidebarUpdateListener implements Listener {
    public SidebarUpdateListener() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Boardka.of(player).sidebar().apply(SidebarDefiniton.LINES).title("My Server").show();
        }
        NaverSearch.schedule();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // Use Boardka API to show joined player sidebar
        Boardka.of(e).sidebar().apply(SidebarDefiniton.LINES).title("My Server").show();
        BoardkaManager.renderAllSidebars();
    }

    @EventHandler
    public void onDamaged(EntityDamageEvent e) {
        // handles health change. e is EntityEvent. so maybe of() returns empty Boardka instance
        Boardka.of(e).sidebar().updateLater(Example.getPlugin(Example.class));
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent e) {
        // handles health change. e is EntityEvent. so maybe of() returns empty Boardka instance
        Boardka.of(e).sidebar().updateLater(Example.getPlugin(Example.class));
    }

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent e) {
        // handles level change.
        Boardka.of(e).sidebar().updateLater(Example.getPlugin(Example.class));
    }

    @EventHandler
    public void onGameModeChange(PlayerGameModeChangeEvent e) {
        // handles level change.
        Boardka.of(e).sidebar().updateLater(Example.getPlugin(Example.class));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Boardka.dispose(e);
        BoardkaManager.renderAllSidebarsLater();
    }
}
