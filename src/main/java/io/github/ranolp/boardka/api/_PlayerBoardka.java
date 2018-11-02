package io.github.ranolp.boardka.api;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

final class _PlayerBoardka extends Boardka {
    /*********************************************************
     *                                                       *
     *                      ! WARNING !                      *
     *     Following methods, fields are used internally     *
     *             You MUST NOT use the methods.             *
     *                                                       *
     *********************************************************/

    Player _target;
    private Scoreboard handle;
    private boolean disposed = false;
    private _PlayerSidebar sidebar;

    _PlayerBoardka(Player target) {
        this._target = target;
        handle = Bukkit.getScoreboardManager().getNewScoreboard();
        this._target.setScoreboard(handle);
    }

    Scoreboard _handle() {
        _checkDisposed();
        return handle;
    }

    @Override
    public Sidebar sidebar() {
        if (sidebar == null) {
            sidebar = new _PlayerSidebar(this);
        }
        return sidebar;
    }

    @Override
    public boolean isDisposed() {
        return disposed;
    }

    @Override
    public void dispose(boolean safe) {
        if (disposed) {
            if (safe) {
                return;
            } else {
                throw new IllegalStateException("Already disposed");
            }
        }
        Boardka.USING.remove(_target.getUniqueId(), this);
        Boardka.AVAILABLE.add(this);
        // is it ok?
        if (_target.getScoreboard() == handle) {
            _target.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        sidebar._dispose();
        disposed = true;
    }

    private void _checkDisposed() {
        if (disposed) {
            throw new IllegalStateException("Disposed");
        }
    }

    @Override
    public String toString() {
        return "Boardka(player=" + _target.getName() + ", disposed=" + disposed + ")";
    }
}
