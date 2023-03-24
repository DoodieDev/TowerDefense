package doodieman.towerdefense.game.interactive;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.utils.ScoreboardUtil;
import doodieman.towerdefense.utils.StringUtil;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;

public class GameScoreboard {

    final Game game;
    final Player player;
    ScoreboardUtil scoreboard;

    BukkitTask updater;

    public GameScoreboard(Game game) {
        this.game = game;
        this.player = game.getPlayer().getPlayer();
    }

    public void createScoreboard() {
        this.scoreboard = new ScoreboardUtil("§6§nSol");
        this.scoreboard.send(this.player);

        this.startUpdater();
    }

    public void updateScoreboard() {

        this.scoreboard.add("§1", 9);
        this.scoreboard.add("§aRunde: "+game.getCurrentRound()+"/"+game.getDifficulty().getRounds(), 8);
        this.scoreboard.add("§eMap: "+game.getMap().getMapName(), 7);
        this.scoreboard.add("§2", 6);
        this.scoreboard.add("§6Guld: "+StringUtil.formatNum(game.getGold()), 5);
        this.scoreboard.add("§7Liv: "+ StringUtil.formatNum(game.getHealth())+" §c❤", 4);
        this.scoreboard.add("§3", 3);

        this.scoreboard.update();
    }

    public void deleteScoreboard() {
        updater.cancel();
        this.scoreboard.getScoreboard().getObjective(DisplaySlot.SIDEBAR).unregister();
    }

    public void startUpdater() {
        this.updater = new BukkitRunnable() {
            @Override
            public void run() {
                updateScoreboard();
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0L, 1L);
    }

}
