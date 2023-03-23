package doodieman.towerdefense.game.interactive;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.utils.PacketUtil;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameAnimations {

    final Game game;

    public GameAnimations(Game game) {
        this.game = game;
    }

    //Animation when a new round starts
    public void newRoundStarted() {
        int roundNumber = game.getCurrentRound();

        new BukkitRunnable() {

            int spaces = 15;
            final int maxSpaces = spaces;

            @Override
            public void run() {
                if (spaces <= 0) {
                    PacketUtil.sendTitle(getPlayer(), "§eRunde", "§e§l"+roundNumber, 0, 20, 0);
                    getPlayer().playSound(getPlayer().getLocation(), Sound.NOTE_BASS, 0.3f, 0.8f);
                    this.cancel();
                    return;
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < spaces; i++)
                    stringBuilder.append(" ");
                String spacesString = stringBuilder.toString();
                PacketUtil.sendTitle(getPlayer(), "§aRunde", "§a§l"+roundNumber+spacesString+roundNumber+spacesString+roundNumber+spacesString+roundNumber, 0, 20, 0);
                float pitch = (((float) spaces / maxSpaces) * 1f)+1.3f;
                getPlayer().playSound(getPlayer().getLocation(), Sound.NOTE_BASS, 0.1f, pitch);

                spaces--;
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0L, 1L);
    }

    //Particles when a mob spawns
    public void mobSpawned(Location location) {
        getPlayer().spigot().playEffect(location, Effect.FIREWORKS_SPARK,0,0,0.1f,0.1f,0.1f,0.2f,10,40);
    }

    //Particles when a mob gets to the goal
    public void mobFinished(Location location) {
        getPlayer().spigot().playEffect(location, Effect.LAVA_POP,0,0,0.1f,0.1f,0.1f,0.2f,10,40);
    }

    public Player getPlayer() {
        return game.getPlayer().getPlayer();
    }
}
