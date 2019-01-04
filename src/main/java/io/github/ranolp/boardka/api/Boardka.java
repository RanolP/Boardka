package io.github.ranolp.boardka.api;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Provides scoreboard assigned to player, and easy way to use the scoreboard.
 */
public abstract class Boardka {
    static final Queue<_PlayerBoardka> AVAILABLE = new LinkedList<>();
    static final Map<UUID, _PlayerBoardka> USING = new HashMap<>();

    Boardka() {
    }

    /**
     * Get Boardka from PlayerEvent.
     *
     * @param event
     *         a event to get a player
     * @return Boardka which is assigned to the player
     */
    @Nonnull
    public static Boardka of(@Nonnull PlayerEvent event) {
        return of(event.getPlayer());
    }

    /**
     * Get Boardka from Player.
     *
     * @param player
     *         a player
     * @return Boardka which is assigned to the player
     */
    @Nonnull
    public static Boardka of(@Nullable Player player) {
        if (player == null) {
            return _EmptyBoardka.SingletonHolder.INSTANCE;
        }
        if (USING.containsKey(player.getUniqueId())) {
            return USING.get(player.getUniqueId());
        } else if (!AVAILABLE.isEmpty()) {
            _PlayerBoardka scoreboard = AVAILABLE.poll();
            scoreboard._reuse();
            scoreboard._target = player;
            player.setScoreboard(scoreboard._handle());
            USING.put(player.getUniqueId(), scoreboard);
            return scoreboard;
        } else {
            _PlayerBoardka scoreboard = new _PlayerBoardka(player);
            USING.put(player.getUniqueId(), scoreboard);
            return scoreboard;
        }
    }

    /**
     * Get Boardka from EntityEvent. If the entity isn't player, returns empty Boardka.
     *
     * @param event
     *         a event to get a player
     * @return if the entity is player, return Boardka which is assigned to the player, or else return empty Boardka
     */
    @Nonnull
    public static Boardka of(@Nonnull EntityEvent event) {
        return of(event.getEntity());
    }

    /**
     * Get Boardka from Entity. If the entity isn't player, returns empty Boardka.
     *
     * @param entity
     *         a entity that may be a player
     * @return if the entity is player, return Boardka which is assigned to the player, or else return empty Boardka
     */
    @Nonnull
    public static Boardka of(@Nullable Entity entity) {
        return entity instanceof Player ? of((Player) entity) : _EmptyBoardka.SingletonHolder.INSTANCE;
    }


    /**
     * Dispose Boardka from PlayerEvent.
     *
     * @param event
     *         a event
     */
    public static void dispose(@Nonnull PlayerEvent event) {
        dispose(event.getPlayer());
    }

    /**
     * Dispose Boardka from PlayerEvent.
     *
     * @param event
     *         a event
     * @param safe
     *         is disposing safety
     */
    public static void dispose(@Nonnull PlayerEvent event, boolean safe) {
        dispose(event.getPlayer(), safe);
    }

    /**
     * Dispose Boardka from Player.
     *
     * @param player
     *         a player
     */
    public static void dispose(@Nullable Player player) {
        if (player != null) {
            dispose(player, true);
        }
    }

    /**
     * Dispose Boardka from Player.
     *
     * @param player
     *         a player
     * @param safe
     *         is disposing safety
     */
    public static void dispose(@Nullable Player player, boolean safe) {
        if (player != null && USING.containsKey(player.getUniqueId())) {
            USING.get(player.getUniqueId()).dispose(safe);
        }
    }

    /**
     * Get sidebar manager object.
     *
     * @return the sidebar object
     */
    @Nonnull
    public abstract Sidebar sidebar();

    /**
     * Check is boardka disposed.
     *
     * @return is disposed
     */
    public abstract boolean isDisposed();

    /**
     * Disposes Boardka object.
     *
     * @param safe
     *         is disposing safety
     */
    public abstract void dispose(boolean safe);
}
