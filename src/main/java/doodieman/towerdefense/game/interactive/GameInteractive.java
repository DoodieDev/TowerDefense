package doodieman.towerdefense.game.interactive;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.interactive.settings.SettingsMenu;
import doodieman.towerdefense.game.interactive.turretstore.TurretStoreMenu;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

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
