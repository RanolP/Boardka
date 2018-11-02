package io.github.ranolp.boardka.example;

import io.github.ranolp.boardka.api.Boardka;
import io.github.ranolp.boardka.api.BoardkaManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

public class SidebarUpdateListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // Use Boardka API to show joined player sidebar
        Boardka.of(e).sidebar().apply(SidebarDefiniton.LINES).title("My Server").show();
        // Call method to update second line(current online players) to all players
        BoardkaManager.renderAll();
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
    public void onRegainHealth(PlayerLevelChangeEvent e) {
        // handles level change.
        Boardka.of(e).sidebar().updateLater(Example.getPlugin(Example.class));
    }
}
