package io.github.ranolp.boardka.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

import java.util.*;

public abstract class Boardka {
    static final Queue<_PlayerBoardka> AVAILABLE = new LinkedList<>();
    static final Map<UUID, _PlayerBoardka> USING = new HashMap<>();

    Boardka() {
    }

    public static Boardka of(PlayerEvent e) {
        return of(e.getPlayer());
    }

    public static Boardka of(Player player) {
        Objects.requireNonNull(player, "player");
        if (USING.containsKey(player.getUniqueId())) {
            return USING.get(player.getUniqueId());
        } else if (!AVAILABLE.isEmpty()) {
            _PlayerBoardka scoreboard = AVAILABLE.poll();
            scoreboard._target = player;
            USING.put(player.getUniqueId(), scoreboard);
            return scoreboard;
        } else {
            _PlayerBoardka scoreboard = new _PlayerBoardka(player);
            USING.put(player.getUniqueId(), scoreboard);
            return scoreboard;
        }
    }

    public static Boardka of(EntityEvent event) {
        return of(event.getEntity());
    }

    public static Boardka of(Entity entity) {
        return entity instanceof Player ? of((Player) entity) : _EmptyBoardka.SingletonHolder.INSTANCE;
    }

    public static void dispose(PlayerEvent e) {
        dispose(e.getPlayer());
    }

    public static void dispose(PlayerEvent e, boolean safe) {
        dispose(e.getPlayer(), safe);
    }

    public static void dispose(Player player) {
        dispose(player, true);
    }

    public static void dispose(Player player, boolean safe) {
        if (USING.containsKey(player.getUniqueId())) {
            USING.get(player.getUniqueId()).dispose(safe);
        }
    }

    public abstract Sidebar sidebar();

    public abstract boolean isDisposed();

    public abstract void dispose(boolean safe);
}
