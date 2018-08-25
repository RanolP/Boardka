package io.github.ranolp.boardka.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class Boardka {
    private static final Queue<Boardka> AVAILABLE = new LinkedList<>();
    private static final Map<UUID, Boardka> USING = new HashMap<>();

    private Scoreboard handle;
    private boolean disposed = false;
    private Sidebar sidebar;

    private Boardka(Player target) {
        this._target = target;
        handle = Bukkit.getScoreboardManager().getNewScoreboard();
        this._target.setScoreboard(handle);
    }

    Scoreboard _handle() {
        _checkDisposed();
        return handle;
    }

    public Sidebar sidebar() {
        if (sidebar == null) {
            sidebar = new Sidebar(this);
        }
        return sidebar;
    }

    public boolean isDisposed() {
        return disposed;
    }

    public void dispose(boolean safe) {
        if (disposed) {
            if (safe) {
                return;
            } else {
                throw new IllegalStateException("Already disposed");
            }
        }
        USING.remove(_target.getUniqueId(), this);
        AVAILABLE.add(this);
        // is it ok?
        if (_target.getScoreboard() == handle) {
            _target.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        sidebar._dispose();
        disposed = true;
    }

    public static Boardka getScoreboard(PlayerEvent e) {
        return getScoreboard(e.getPlayer());
    }

    public static Boardka getScoreboard(Player player) {
        Objects.requireNonNull(player, "player");
        if (USING.containsKey(player.getUniqueId())) {
            return USING.get(player.getUniqueId());
        } else if (!AVAILABLE.isEmpty()) {
            Boardka scoreboard = AVAILABLE.poll();
            scoreboard._updateTarget(player);
            USING.put(player.getUniqueId(), scoreboard);
            return scoreboard;
        } else {
            Boardka scoreboard = new Boardka(player);
            USING.put(player.getUniqueId(), scoreboard);
            return scoreboard;
        }
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

    /*********************************************************
     *                                                       *
     *                      ! WARNING !                      *
     *     Following methods, fields are used internally     *
     *             You MUST NOT use the methods.             *
     *                                                       *
     *********************************************************/

    Player _target;

    private void _updateTarget(Player target) {
        this._target = target;
    }

    private void _checkDisposed() {
        if (disposed) {
            throw new IllegalStateException("Disposed");
        }
    }
}
