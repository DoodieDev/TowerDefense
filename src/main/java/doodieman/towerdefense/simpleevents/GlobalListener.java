package doodieman.towerdefense.simpleevents;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.lobby.spawn.SpawnUtil;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.staff.StaffHandler;
import doodieman.towerdefense.utils.LuckPermsUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class GlobalListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Bukkit.getOnlinePlayers().size() > 20)
            event.setJoinMessage("");
        else
            event.setJoinMessage("§8[§a+§8] "+LuckPermsUtil.getRankColor(player)+player.getName());

        if (player.hasPlayedBefore()) {
            player.teleport(SpawnUtil.getSpawn());
            player.setGameMode(GameMode.ADVENTURE);

        } else {
            Difficulty difficulty = Difficulty.EASY;
            Map map = MapUtil.getInstance().getMap("eventyr");
            Game game = GameUtil.getInstance().createGame(player, map, difficulty);
            GameUtil.getInstance().prepareGame(game, null);
        }


    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        if (Bukkit.getOnlinePlayers().size() > 20 || StaffHandler.getInstance().isInStaffMode(player))
            event.setQuitMessage("");
        else
            event.setQuitMessage("§8[§c-§8] "+LuckPermsUtil.getRankColor(player)+player.getName());
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onThunderChange(ThunderChangeEvent event) {
        if (event.toThunderState())
            event.setCancelled(true);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        event.setRespawnLocation(SpawnUtil.getSpawn());
        player.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        event.setDamage(0);
    }

    @EventHandler
    public void onBlockUpdate(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        event.setCancelled(true);
    }



}
