package doodieman.towerdefense.sumo;

import com.boydti.fawe.bukkit.util.BukkitReflectionUtils;
import com.boydti.fawe.wrappers.PlayerWrapper;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.lobby.water.WaterHandler;
import doodieman.towerdefense.sumo.objects.SumoLocation;
import doodieman.towerdefense.sumo.objects.SumoPlayer;
import doodieman.towerdefense.sumo.objects.SumoState;
import doodieman.towerdefense.utils.PacketUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SumoUtil {

    final SumoHandler handler;

    @Getter
    private static SumoUtil instance;

    public SumoUtil(SumoHandler handler) {
        this.handler = handler;
        instance = this;
    }

    public void startGame(List<Player> players) {
        if (players.size() != 2) return;

        handler.getWaitingPlayers().removeAll(players);
        handler.getIngamePlayers().add(new SumoPlayer(players.get(0), SumoLocation.PLAYER1.getLocation()));
        handler.getIngamePlayers().add(new SumoPlayer(players.get(1), SumoLocation.PLAYER2.getLocation()));

        handler.setState(SumoState.COUNTDOWN);

        for (SumoPlayer sumoPlayer : handler.getIngamePlayers()) {
            sumoPlayer.getPlayer().teleport(sumoPlayer.getSpawnLocation());
            sumoPlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
        }

        new BukkitRunnable() {

            int timeLeft = 3;

            @Override
            public void run() {

                if (timeLeft <= 0) {

                    for (SumoPlayer sumoPlayer : handler.getIngamePlayers()) {
                        PacketUtil.sendTitle(sumoPlayer.getPlayer(),"§aKÆMP","",0,20,5);
                        sumoPlayer.getPlayer().playSound(sumoPlayer.getSpawnLocation(), Sound.ORB_PICKUP,0.5f,0.8f);
                    }


                    handler.setState(SumoState.FIGHTING);

                    this.cancel();
                    return;
                }

                for (SumoPlayer sumoPlayer : handler.getIngamePlayers()) {
                    PacketUtil.sendTitle(sumoPlayer.getPlayer(),"§a§l"+timeLeft,"",0,25,0);
                    sumoPlayer.getPlayer().playSound(sumoPlayer.getSpawnLocation(), Sound.NOTE_BASS,0.5f,0.8f + ((float)timeLeft / 6));
                }

                timeLeft--;
            }
        }.runTaskTimer(TowerDefense.getInstance(),20L, 20L);
    }

    public void stopGame(Player losingPlayer) {
        if (handler.getState() == SumoState.IDLE) return;

        Player winningPlayer = this.getOpponent(losingPlayer);

        Location winLocation = SumoLocation.WINLOBBY.getLocation();
        Location loserLocation = SumoLocation.LOSERLOBBY.getLocation();

        PacketUtil.sendTitle(winningPlayer,"§aDU VANDT!","§aDu er virkelig dygtig",0,40,0);
        PacketUtil.sendTitle(losingPlayer,"§cDU TABTE!","§cDu er virkelig dårlig",0,40,0);

        handler.setState(SumoState.WINEFFECT);

        //Launch the losing player
        Bukkit.getWorld("world").strikeLightningEffect(losingPlayer.getLocation());
        Location center = SumoLocation.CENTER.getLocation();

        //Catch the warning "Excessive velocity set detected: tried to set velocity of entity" that spams console.
        try {
            losingPlayer.setVelocity(new Vector((losingPlayer.getLocation().getX() - center.getX()) * 5, 2, (losingPlayer.getLocation().getZ() - center.getZ()) * 5));
        } catch (Exception ignored) {}

        //Reset after 2 seconds
        new BukkitRunnable() {
            @Override
            public void run() {

                handler.getIngamePlayers().clear();

                WaterHandler.getInstance().getLastLocations().put(losingPlayer, loserLocation);
                WaterHandler.getInstance().getLastLocations().put(winningPlayer, winLocation);
                losingPlayer.teleport(SumoLocation.LOSERLOBBY.getLocation());
                winningPlayer.teleport(SumoLocation.WINLOBBY.getLocation());

                handler.setState(SumoState.IDLE);

                //Launch 1 to 10 Firework
                for (int i = 0; i <= new Random().nextInt(10); i++)
                    spawnFirework(winLocation);

            }
        }.runTaskLater(TowerDefense.getInstance(),40L);
    }

    public List<Player> getRandomPlayers() {
        if (handler.getWaitingPlayers().size() < 2) return new ArrayList<>();

        List<Player> randomPlayers = new ArrayList<>();
        while (randomPlayers.size() < 2) {

            int randomIndex = ThreadLocalRandom.current().nextInt(handler.getWaitingPlayers().size());
            Player randomPlayer = handler.getWaitingPlayers().get(randomIndex);

            if (randomPlayers.contains(randomPlayer)) continue;

            randomPlayers.add(randomPlayer);
        }

        return randomPlayers;
    }

    public boolean isInGame(Player player) {
        return handler.getIngamePlayers()
            .stream()
            .anyMatch(ingamePlayer -> ingamePlayer.getPlayer().equals(player));
    }

    public Player getOpponent(Player player) {
        for (SumoPlayer p : handler.getIngamePlayers()) {
            if (!p.getPlayer().equals(player))
                return p.getPlayer();
        }
        return null;
    }

    public SumoPlayer getSumoPlayer(Player player) {
        return handler.getIngamePlayers()
            .stream()
            .filter(ingamePlayer -> ingamePlayer.getPlayer().equals(player))
            .findFirst().get();
    }

    public void spawnFirework(Location location){
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        Random r = new Random();

        int rt = r.nextInt(3);
        Type type = Type.BALL;
        if (rt == 0) type = Type.BALL;
        if (rt == 1) type = Type.BALL_LARGE;
        if (rt == 2) type = Type.BURST;
        if (rt == 3) type = Type.STAR;

        Color c1 = Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256));
        Color c2 = Color.fromRGB(r.nextInt(256), r.nextInt(256), r.nextInt(256));

        FireworkEffect effect = FireworkEffect.builder().flicker(r.nextBoolean()).withColor(c1).withFade(c2).with(type).trail(r.nextBoolean()).build();
        fwm.addEffect(effect);

        fwm.setPower(r.nextInt(3));
        fw.setFireworkMeta(fwm);
    }

}
