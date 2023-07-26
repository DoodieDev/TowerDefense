package doodieman.towerdefense.game.interactive;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.GameHandler;
import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.interactive.settings.SettingsMenu;
import doodieman.towerdefense.game.interactive.turretstore.TurretStoreMenu;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.utils.TurretUtil;
import doodieman.towerdefense.game.values.ControlBlock;
import doodieman.towerdefense.utils.ItemBuilder;
import doodieman.towerdefense.utils.PacketUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.World;
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

    public GameInteractive(OfflinePlayer player, Game game) {
        this.offlinePlayer = player;
        this.player = offlinePlayer.getPlayer();
        this.game = game;

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

        } else {
            ItemBuilder builder = new ItemBuilder(Material.EMERALD);
            builder.name("§a§nStart runde§r §7(Højreklik)");
            player.getInventory().setItem(7, builder.build());
        }

    }

    //Called every tick from the Game class
    public void doTick(int tick) {


        if (tick % 4 == 0)
            this.displayPlacementOptions();
    }

    //Display where the player can place turrets
    public void displayPlacementOptions() {
        if (!TurretUtil.getInstance().isTurretItem(player.getItemInHand())) return;

        //Example 7 = 196 blocks to scan
        int scanRange = 5;

        //Corners to scan
        int xCorner1 = player.getLocation().getBlockX() - scanRange;
        int zCorner1 = player.getLocation().getBlockZ() - scanRange;
        int xCorner2 = player.getLocation().getBlockX() + scanRange;
        int zCorner2 = player.getLocation().getBlockZ() + scanRange;

        for (int x = xCorner1; x <= xCorner2; x++) {
            for (int z = zCorner1; z <= zCorner2; z++) {
                if (ControlBlock.isPlaceable(x,z,game.getWorld())) {
                    this.displayPlacement(x,z,4, Color.fromRGB(51,255,0));
                }

                else {
                    this.displayPlacement(x,z,4, Color.fromRGB(255,0,0));
                }
            }
        }

    }

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


    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        //Open settings
        if (player.getInventory().getHeldItemSlot() == 8) {
            new SettingsMenu(player).open();
            player.playSound(player.getLocation(), Sound.CHEST_OPEN, 0.2f, 1.2f);
        }

        //Start round
        if (player.getInventory().getHeldItemSlot() == 7 && !game.isRoundActive()) {
            game.startRound();
            this.updateRoundItemSlot();
        }

        //Open turrets store
        if (player.getInventory().getHeldItemSlot() == 6) {
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
