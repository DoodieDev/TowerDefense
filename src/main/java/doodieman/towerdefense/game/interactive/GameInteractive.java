package doodieman.towerdefense.game.interactive;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.GameHandler;
import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.interactive.settings.SettingsMenu;
import doodieman.towerdefense.game.interactive.turretstore.TurretStoreMenu;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.utils.TurretUtil;
import doodieman.towerdefense.game.values.ControlBlock;
import doodieman.towerdefense.game.values.TurretType;
import doodieman.towerdefense.utils.ItemBuilder;
import doodieman.towerdefense.utils.LocationUtil;
import doodieman.towerdefense.utils.NumberUtil;
import doodieman.towerdefense.utils.PacketUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GameInteractive implements Listener {

    private final OfflinePlayer offlinePlayer;
    private final Player player;
    private final Game game;

    @Getter
    private final GameAnimations gameAnimations;
    @Getter
    private final GameScoreboard scoreboard;

    private double displayPathOffset;

    public GameInteractive(OfflinePlayer player, Game game) {
        this.offlinePlayer = player;
        this.player = offlinePlayer.getPlayer();
        this.game = game;

        this.displayPathOffset = 0;
        this.gameAnimations = new GameAnimations(game);
        this.scoreboard = new GameScoreboard(game);
    }

    //Setups the entire interactive side (Items in inventory, Scoreboard, etc)
    public void register() {
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());

        //Scoreboard
        this.scoreboard.createScoreboard();

        //Settings item
        ItemBuilder settings = new ItemBuilder(Material.NETHER_STAR);
        settings.name("§f§nIndstillinger§r §7(Højreklik)");
        player.getInventory().setItem(8, settings.build());

        //Buy turrets item
        ItemBuilder shop = new ItemBuilder(Material.GOLD_INGOT);
        shop.name("§e§nKøb tårne§r §7(Højreklik)");
        player.getInventory().setItem(6, shop.build());

        this.updateRoundItemSlot();
    }

    //Unregister the interactive side
    public void unregister() {
        HandlerList.unregisterAll(this);
        this.scoreboard.deleteScoreboard();
    }

    //Sets the 'Round' item, wether round is active or not.
    public void updateRoundItemSlot() {

        if (game.isRoundActive()) {
            ItemBuilder builder = new ItemBuilder(Material.BARRIER);
            builder.name("§c§oRunden er aktiv");
            player.getInventory().setItem(7, builder.build());

        } else if (!game.isAlive()) {
            ItemBuilder builder = new ItemBuilder(Material.BARRIER);
            builder.name("§c§oDu er død");
            player.getInventory().setItem(7, builder.build());

        } else if (game.hasWonGame()) {
            ItemBuilder builder = new ItemBuilder(Material.BARRIER);
            builder.name("§c§oDu har klaret alle runder");
            player.getInventory().setItem(7, builder.build());
        }

        else {
            ItemBuilder builder = new ItemBuilder(Material.EMERALD);
            builder.name("§a§nStart runde§r §7(Højreklik)");
            player.getInventory().setItem(7, builder.build());
        }

    }

    //Called every tick from the Game class
    public void doTick(int tick) {

        if (!game.isRoundActive()) {
            showPathParticles(displayPathOffset,30);
            displayPathOffset += 0.30;
        }

        //When player is holding a turret
        if (TurretUtil.getInstance().isTurretItem(player.getItemInHand())) {
            if (tick % 4 == 0)
                this.displayPlacementOptions();
            if (tick % 6 == 0)
                this.displayRange();
            if (tick % 10 == 0)
                PacketUtil.sendActionBar(player,"§5Tårnets Range §f §f §f✦ §a §a §aFri placering");
        }
    }

    //Display where the player can place turrets
    public void displayPlacementOptions() {
        int scanRange = 7;

        Block targetBlock = LocationUtil.getTargetBlock(player,50);

        //Corners to scan
        int xCorner1 = targetBlock.getLocation().getBlockX() - scanRange;
        int zCorner1 = targetBlock.getLocation().getBlockZ() - scanRange;
        int xCorner2 = targetBlock.getLocation().getBlockX() + scanRange;
        int zCorner2 = targetBlock.getLocation().getBlockZ() + scanRange;

        for (int x = xCorner1; x <= xCorner2; x++) {
            for (int z = zCorner1; z <= zCorner2; z++) {
                if (!ControlBlock.isPlaceable(x,z,game.getWorld())) continue;

                this.displayPlacement(x,z,4, Color.fromRGB(51,255,0));
            }
        }

    }

    //Display the range of a turret
    public void displayRange() {
        TurretType turretType = TurretUtil.getInstance().getTurretFromItem(player.getItemInHand());

        Block targetBlock = LocationUtil.getTargetBlock(player,50);

        int pointsPerRange = 20;
        double range = turretType.getRange();
        int points = (int) Math.floor(range * pointsPerRange);
        Location center = targetBlock.getLocation();
        center.add(0.5,0,0.5);
        center.setY(game.getMobPath().get(0).getBlockY() + 0.25);

        //Display the outer circle
        for (int i = 0; i < points; i++) {

            double angle = i * (360f / points);
            Location location = LocationUtil.getLocationInCircle(center,angle,range);

            player.spigot().playEffect(location, Effect.WITCH_MAGIC,0,0,0,0,0,0,1,70);
        }
    }

    //Display a placement square on the map
    public void displayPlacement(int centerX, int centerZ, int particlesPerSide, Color color) {
        double displayLevel = game.getMobPath().get(0).getBlockY() + 0.05;

        double range = 0.4;
        double xCorner1 = (centerX+0.5) - range;
        double zCorner1 = (centerZ+0.5) - range;
        double xCorner2 = (centerX+0.5) + range;
        double zCorner2 = (centerZ+0.5) + range;

        World world = game.getWorld();

        double sideLength = xCorner2 - xCorner1;
        double lengthPerPoint = sideLength / (particlesPerSide - 1);

        for (int i = 0; i < particlesPerSide; i++) {

            double length = i * lengthPerPoint;

            //Side 1
            Location side1 = new Location(world,xCorner1 + length,displayLevel,zCorner1);
            //Side 2
            Location side2 = new Location(world,xCorner2 - length,displayLevel,zCorner2);
            //Side 3
            Location side3 = new Location(world,xCorner1,displayLevel,zCorner1 + length);
            //Side 4
            Location side4 = new Location(world,xCorner2,displayLevel,zCorner2 - length);

            PacketUtil.sendRedstoneParticle(player,side1, color);
            PacketUtil.sendRedstoneParticle(player,side2, color);
            PacketUtil.sendRedstoneParticle(player,side3, color);
            PacketUtil.sendRedstoneParticle(player,side4, color);
        }
    }

    //Display the particles throughout the entire path
    private void showPathParticles(double offset, double distance) {

        double pathLength = game.getMap().getPathLength();
        double middlePath = game.getMap().getPathLength() / 2;

        int pathPoints = (int) Math.floor(pathLength / distance);
        double lengthPerPoint = pathLength / pathPoints;

        offset = offset % lengthPerPoint;

        Color startColor = Color.fromRGB(0, 255,0);
        Color middleColor = Color.fromRGB(255, 255,0);
        Color endColor = Color.fromRGB(255, 0,0);

        for (int i = 0; i < pathPoints; i++) {

            double placement = i * lengthPerPoint + offset;
            Location location = game.getRealLocation(game.getMap().getPathLocationAt(placement));
            location.add(0, 0.5, 0);

            Color color;

            //If it is over 50% of the map length, fade from MIDDLE to END color.
            if (placement >= middlePath) {
                double percent = (placement - middlePath) / middlePath;
                int red = (int) NumberUtil.getNumberCloseToTarget(middleColor.getRed(),endColor.getRed(), percent);
                int green = (int) NumberUtil.getNumberCloseToTarget(middleColor.getGreen(),endColor.getGreen(), percent);
                int blue = (int) NumberUtil.getNumberCloseToTarget(middleColor.getBlue(),endColor.getBlue(), percent);

                color = Color.fromRGB(red, green, blue);
            }

            //If it iis less 50% of the map length, fade from START to MIDDLE color.
            else {
                double percent = placement / middlePath;

                int red = (int) NumberUtil.getNumberCloseToTarget(startColor.getRed(),middleColor.getRed(), percent);
                int green = (int) NumberUtil.getNumberCloseToTarget(startColor.getGreen(),middleColor.getGreen(), percent);
                int blue = (int) NumberUtil.getNumberCloseToTarget(startColor.getBlue(),middleColor.getBlue(), percent);

                color = Color.fromRGB(red, green, blue);
            }

            for (int j = 0; j < 5; j++) {
                PacketUtil.sendRedstoneParticle(player.getPlayer(),location.clone().add(0,j * 0.1,0),color);
            }
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        //Open settings
        if (player.getInventory().getHeldItemSlot() == 8) {
            new SettingsMenu(player, game).open();
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.2f, 1.2f);
        }

        //Start round
        if (player.getInventory().getHeldItemSlot() == 7 && !game.isRoundActive()) {

            //Game is over
            if (!game.isAlive()) {
                player.sendMessage("§cDu kan ikke starte flere runder, du er død!");
                player.playSound(player.getLocation(),Sound.VILLAGER_NO,1f,1.3f);
                return;
            }

            //Game is over - Has completed all rounds
            if (game.hasWonGame()) {
                player.sendMessage("§cDu kan ikke starte flere runder, du har klaret alle runder!");
                player.playSound(player.getLocation(),Sound.VILLAGER_NO,1f,1.3f);
                return;
            }

            game.startRound();
            this.updateRoundItemSlot();
        }

        //Open turrets store
        if (player.getInventory().getHeldItemSlot() == 6) {

            //Game is over - Death
            if (!game.isAlive()) {
                player.sendMessage("§cDu kan ikke købe tårne, du er død!");
                player.playSound(player.getLocation(),Sound.VILLAGER_NO,1f,1.3f);
                return;
            }

            //Game is over - Has completed all rounds
            if (game.hasWonGame()) {
                player.sendMessage("§cDu kan ikke købe tårne, du har klaret alle runder!");
                player.playSound(player.getLocation(),Sound.VILLAGER_NO,1f,1.3f);
                return;
            }

            new TurretStoreMenu(player, game).open();
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.2f, 1.2f);
        }

    }

    //Locks the slots with interactive items
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != player) return;

        List<Integer> lockedSlots = Arrays.asList(6, 7, 8);
        if (lockedSlots.contains(event.getSlot()) || lockedSlots.contains(event.getHotbarButton()))
            event.setCancelled(true);
    }

}
