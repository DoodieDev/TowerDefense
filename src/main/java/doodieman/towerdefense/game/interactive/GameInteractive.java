package doodieman.towerdefense.game.interactive;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.interactive.settings.SettingsMenu;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class GameInteractive implements Listener {

    private final OfflinePlayer offlinePlayer;
    private final Player player;
    private final Game game;

    public GameInteractive(OfflinePlayer player, Game game) {
        this.offlinePlayer = player;
        this.player = offlinePlayer.getPlayer();
        this.game = game;
    }

    //Sets op the interactive board. (Give player items, etc)
    public void register() {
        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());

        //Settings item
        ItemBuilder settings = new ItemBuilder(Material.NETHER_STAR);
        settings.name("§f§nIndstillinger§r §7(Højreklik)");
        player.getInventory().setItem(8, settings.build());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getPlayer() != player) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (player.getInventory().getHeldItemSlot() == 8) {
            new SettingsMenu(player).open();
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != player) return;

        if (event.getSlot() == 8 || event.getHotbarButton() == 8) {
            event.setCancelled(true);
        }
    }

}
