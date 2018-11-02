package io.github.ranolp.boardka.api;

import io.github.ranolp.boardka.BoardkaPlugin;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.Stack;

final class _PlayerSidebar extends Sidebar {
    /*********************************************************
     *                                                       *
     *                      ! WARNING !                      *
     *     Following methods, fields are used internally     *
     *             You MUST NOT use the methods.             *
     *                                                       *
     *********************************************************/

    SidebarLines _lines;
    Stack<Score> _scores = new Stack<>();
    private _PlayerBoardka parent;
    private boolean show;
    private Objective handle;

    _PlayerSidebar(_PlayerBoardka parent) {
        this.parent = parent;

        //noinspection deprecation; for compatibility.
        this.handle = parent._handle().registerNewObjective("__sidebar", "dummy");
        handle.setDisplayName("");
    }

    private static String makeColorId(int id) {
        StringBuilder builder = new StringBuilder();
        for (String s : String.format("%03x", id).split("")) {
            builder.append(ChatColor.COLOR_CHAR).append(s);
        }
        return builder.toString();
    }

    @Override
    public boolean isShowing() {
        return this.show;
    }

    @Override
    public Sidebar title(String title) {
        handle.setDisplayName(title);
        return this;
    }

    @Override
    public Sidebar apply(SidebarLines lines) {
        this._lines = lines;
        return this;
    }

    @Override
    public void update() {
        BoardkaManager.render(parent._target);
    }

    @Override
    public void updateLater(Plugin plugin, long delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskLater(plugin, delay);
    }

    @Override
    public void updateLater(Plugin plugin) {
        updateLater(plugin, 0);
    }

    @Override
    public void updateLater(long delay) {
        updateLater(BoardkaPlugin.getInstance(), delay);
    }

    @Override
    public void updateLater() {
        updateLater(BoardkaPlugin.getInstance(), 0);
    }

    @Override
    public void show() {
        if (this.show) {
            return;
        }
        this.show = true;
        handle.setDisplaySlot(DisplaySlot.SIDEBAR);
        update();
    }

    @Override
    public void hide() {
        if (!this.show) {
            return;
        }
        this.show = false;
        handle.setDisplaySlot(null);
    }

    void _requestSize(int size) {
        while (_scores.size() > size) {
            Score score = _scores.pop();
            parent._handle().resetScores(score.getEntry());
        }
        while (_scores.size() < size) {
            Score score = handle.getScore(makeColorId(0x5b0 + _scores.size()));
            score.setScore(16 - _scores.size());
            _scores.add(score);
            if (parent._handle().getTeams().stream().noneMatch(it -> it.getName().equals(score.getEntry()))) {
                Team team = parent._handle().registerNewTeam(score.getEntry());
                team.addEntry(score.getEntry());
            }
        }
    }

    void _dispose() {
        hide();
        _lines = null;
        _requestSize(0);
    }

    @Override
    public String toString() {
        return "Boardka.Sidebar(player=" + parent._target.getName() + ", show=" + show + ")";
    }
}
