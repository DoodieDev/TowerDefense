package doodieman.towerdefense.sumo;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.sumo.objects.SumoPlayer;
import doodieman.towerdefense.sumo.objects.SumoState;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SumoHandler {

    final SumoListener listener;
    final SumoUtil util;

    @Getter
    private final List<Player> waitingPlayers = new ArrayList<>();
    @Getter
    private final List<String> sumoBenchIDS;
    @Getter
    private final List<SumoPlayer> ingamePlayers = new ArrayList<>();
    @Getter @Setter
    private SumoState state = SumoState.IDLE;

    public SumoHandler() {
        this.listener = new SumoListener(this);
        this.util = new SumoUtil(this);

        this.sumoBenchIDS = Arrays.asList("8", "9", "10");

        this.startTimer();
    }

    private void startTimer() {
        new BukkitRunnable() {
            @Override
            public void run() {

                if (getState() != SumoState.IDLE) return;
                if (waitingPlayers.size() < 2) return;

                List<Player> randomPlayers = util.getRandomPlayers();
                util.startGame(randomPlayers);

            }
        }.runTaskTimer(TowerDefense.getInstance(),0L,20L);
    }

}
