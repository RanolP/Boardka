package io.github.ranolp.boardka.api;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Team;

import java.util.Stack;

public class Sidebar {
    private Boardka parent;
    private boolean show;
    private Objective handle;

    Sidebar(Boardka parent) {
        this.parent = parent;
        this.handle = parent._handle().registerNewObjective("__sidebar", "dummy", "");
    }


    public boolean isShowing() {
        return this.show;
    }

    public Sidebar title(String title) {
        handle.setDisplayName(title);
        return this;
    }

    public Sidebar apply(SidebarLines lines) {
        this._lines = lines;
        return this;
    }


    public void update() {
        BoardkaManager.render(parent._target);
    }

    public void show() {
        if (this.show) {
            return;
        }
        this.show = true;
        handle.setDisplaySlot(DisplaySlot.SIDEBAR);
        update();
    }

    public void hide() {
        if (!this.show) {
            return;
        }
        this.show = false;
        handle.setDisplaySlot(null);
    }

    /*********************************************************
     *                                                       *
     *                      ! WARNING !                      *
     *     Following methods, fields are used internally     *
     *             You MUST NOT use the methods.             *
     *                                                       *
     *********************************************************/

    SidebarLines _lines;
    Stack<Score> _scores = new Stack<>();

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

    private static String makeColorId(int id) {
        StringBuilder builder = new StringBuilder();
        for (String s : String.format("%03x", id).split("")) {
            builder.append(ChatColor.COLOR_CHAR).append(s);
        }
        return builder.toString();
    }

    void _dispose() {
        hide();
        _lines = null;
        _requestSize(0);
    }
}
