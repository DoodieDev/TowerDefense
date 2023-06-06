package doodieman.towerdefense.game;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameTurret;
import doodieman.towerdefense.game.utils.TurretUtil;
import doodieman.towerdefense.game.values.TurretType;
import doodieman.towerdefense.utils.LocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GameListener implements Listener {

    final GameHandler handler;
    final GameUtil util;

    public GameListener(GameHandler handler) {
        this.handler = handler;
        this.util = handler.getGameUtil();
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    //Place turret OR cancel the event
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getItemInHand();
        if (!util.isInGame(player)) return;

        event.setCancelled(true);

        //Is not turret item
        if (!handler.getTurretUtil().isTurretItem(tool))
            return;

        Location blockLocation = event.getBlockPlaced().getLocation();
        Game game = util.getActiveGame(player);
        TurretType turretType = handler.getTurretUtil().getTurretFromItem(tool);

        //Check if it can be placed
        if (!handler.getTurretUtil().canTurretBePlaced(game,blockLocation)) {
            player.sendMessage("§cDu kan ikke placere et tårn her!");
            return;
        }

        //Create the turret
        TurretUtil turretUtil = handler.getTurretUtil();
        turretUtil.removeTurretItems(player, turretType, 1);
        GameTurret turret = turretUtil.createTurret(game, turretType, blockLocation);

        //Add correct rotation
        //My brain is melting after making this shitty mess. Future me please recode
        double yaw = player.getLocation().getYaw() + 90;
        double normalizedYaw = yaw < 0 ? yaw + 360d : yaw;
        double rotationRounded = Math.round(normalizedYaw / 90) * 90;
        turret.setRotation(rotationRounded);

        //Render the turret
        turret.render();

        player.playSound(blockLocation, Sound.ZOMBIE_WOOD,0.8f,0.4f);
        player.spigot().playEffect(blockLocation.add(0.5,0,0.5), Effect.TILE_BREAK,turretType.getItem().getTypeId(),0,1,2,1,0.1f,150,20);
    }

    //Leave the game
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!util.isInGame(player)) return;
        GameUtil.getInstance().exitGame(player, true);
    }


    /*
        CANCEL EVENTS
    */

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (!util.isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if (!util.isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (!util.isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        if (!util.isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (!util.isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        Player player = (Player) event.getEntity();
        if (!util.isInGame(player)) return;
        event.setFoodLevel(20);
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!util.isInGame(player)) return;

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK)
            return;
        if (event.getAction() == Action.LEFT_CLICK_BLOCK)
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        if (!util.isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        if (!util.isInGame(player)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPaintingBreak(HangingBreakByEntityEvent event) {
        if (!(event.getRemover() instanceof Player)) return;
        if (!(event.getEntity() instanceof Painting)) return;
        Player player = (Player) event.getRemover();
        if (!util.isInGame(player)) return;
        event.setCancelled(true);
    }


}
