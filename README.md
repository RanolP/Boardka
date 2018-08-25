# Boardka

Dirty scoreboard library

## Installing

### Gradle

Add following code to your `build.gradle` file.

```gradle
repositories {
  ...
  maven { url 'https://jitpack.io' }
}

dependencies {
  implementation 'com.github.RanolP:Boardka:-SNAPSHOT'
}
```

### Maven

Add following code to your `pom.xml` file.

```maven
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>

<dependency>
  <groupId>com.github.RanolP</groupId>
  <artifactId>Boardka</artifactId>
  <version>-SNAPSHOT</version>
</dependency>
```

## Examples

Simple global sidebar

```java
public final class TestListener implements Listener {
  private static final class HealthLineRenderer extends ByPlayerLineRenderer {
    @Override
    public String compute(Player player) {
      switch (player.getGameMode()) {
        case CREATIVE:
          return "Creative";
        case SPECTATOR:
          return "Spectator";
        default:
          return Math.round(player.getHealth()) + " / " + Math.round(
            player.getAttribute(
              Attribute.GENERIC_MAX_HEALTH
            ).getValue()
          );
      }
    }
  }

  private final SidebarLines lines;

  {
    lines = SidebarLines.newInstance()
      // Constant. It'll never change.
      .appendConstant(ChatColor.AQUA, ChatColor.BOLD, "Players")
      // Global. It updates on render request.
      .appendGlobal(
        () -> ChatColor.GRAY.toString()
                + Bukkit.getOnlinePlayers().size()
      )
      // equals to appendConstant("")
      .appendEmptyLine()
      .appendConstant(ChatColor.AQUA, ChatColor.BOLD, "Health")
      // Custom line renderer
      .append(new HealthLineRenderer());
  }

  @EventHandler
  public void onJoin(PlayerJoinEvent e) {
    // Use boardka api
    Boardka board = Boardka.getScoreboard(e);
    board.sidebar().apply(lines).title("My Server").show();
    BoardkaManager.renderAll();
  }

  @EventHandler
  public void onDamage(EntityDamageEvent e) {
    // handle health change
    if (!(e.getEntity() instanceof Player)) {
      return;
    }

    Player player = (Player) e.getEntity();

    new BukkitRunnable() {
      @Override
      public void run() {
        Boardka.getScoreboard(player).sidebar().update();
      }
    }.runTask(BoardkaPlugin.getPlugin(BoardkaPlugin.class));
  }

  @EventHandler
  public void onRegainHealth(EntityRegainHealthEvent e) {
    // handle health change
    if (!(e.getEntity() instanceof Player)) {
      return;
    }

    Player player = (Player) e.getEntity();

    new BukkitRunnable() {
      @Override
      public void run() {
        Boardka.getScoreboard(player).sidebar().update();
      }
    }.runTask(BoardkaPlugin.getPlugin(BoardkaPlugin.class));
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    // Dispose boardka instance.
    Boardka.dispose(e);
    // Update player list
    BoardkaManager.renderAll();
  }
}
```
