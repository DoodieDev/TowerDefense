package doodieman.towerdefense.sumo;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.sumo.objects.SumoLocation;
import doodieman.towerdefense.sumo.objects.SumoPlayer;
import doodieman.towerdefense.sumo.objects.SumoState;
import doodieman.towerdefense.utils.WorldGuardUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
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

    @Getter
    private final List<String> sumoMessages = Arrays.asList(
        "§8{loser} §7blev destrueret af §e{winner}§7!",
        "§8{loser} §7blev sendt til månen af §e{winner}§7!",
        "§8{loser} §7er officielt dårligere end §e{winner}§7!",
        "§8{loser}§7, hvordan føles det at tabe til §e{winner}§7?",
        "§8{loser} §7<-- noob §7 §7 §7gamer --> §e{winner}§7!",
        "§8{loser} §7glemte at klikke på musen, derfor vandt §e{winner}§7!",
        "§8{loser} §7blev sendt i Netto af §e{winner}§7!",
        "§8{loser} §7blev forvandlet til en fugl af §e{winner}§7!"
    );

    public SumoHandler() {
        this.listener = new SumoListener(this);
        this.util = new SumoUtil(this);

        WorldGuardUtil.playersWithinRegion(Bukkit.getOnlinePlayers(), Bukkit.getWorld("world"), "sumo")
            .forEach(player -> {
                player.teleport(SumoLocation.LOSERLOBBY.getLocation());
            });

        this.sumoBenchIDS = Arrays.asList("8", "9", "10");
        this.startTimer();
    }

    private void startTimer() {
        new BukkitRunnable() {
            int ingameTime = 0;
            @Override
            public void run() {

                if (getState() == SumoState.IDLE) {
                    if (waitingPlayers.size() < 2) return;

                    ingameTime = 0;

                    List<Player> randomPlayers = util.getRandomPlayers();
                    util.startGame(randomPlayers);
                }

                else if (getState() == SumoState.FIGHTING) {
                    if (ingameTime >= 60) {
                        util.stopGame(ingamePlayers.get(0).getPlayer());
                        return;
                    }

                    ingameTime++;
                }

            }
        }.runTaskTimer(TowerDefense.getInstance(),0L,20L);
    }

}
