package doodieman.towerdefense.game.interactive.turretstore;

import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.values.TurretType;
import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import doodieman.towerdefense.utils.StringUtil;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TurretStoreMenu extends GUI {

    final Game game;
    public HashMap<Integer, TurretType> turretSlots = new HashMap<>();

    public TurretStoreMenu(Player player, Game game) {
        super(player, 4, "Butik");
        this.game = game;

        this.turretSlots.put(9, TurretType.WOOD_TOWER);
        this.turretSlots.put(10, TurretType.STONE_TOWER);
        this.turretSlots.put(11, TurretType.EYE_TOWER);
        this.turretSlots.put(12, TurretType.TNT_TOWER);
        this.turretSlots.put(13, TurretType.FIRE_TOWER);
        this.turretSlots.put(14, TurretType.LASER_TOWER);
        this.turretSlots.put(15, TurretType.SNOWBALL_TOWER);
        this.turretSlots.put(16, TurretType.SAND_TOWER);
        this.turretSlots.put(17, TurretType.ANGEL_TOWER);
    }

    @Override
    public void render() {
        //Bottom standard items
        for (int i = 27; i < 36; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(31, GUIItem.EXIT_MENU.getItem());

        //Render turrets
        turretSlots.forEach((slot, turretType) -> {
            ItemBuilder builder = new ItemBuilder(turretType.getFormattedItem());

            double price = turretType.getRealPrice(game.getDifficulty());

            builder.addLore("", "§7Pris: §e"+ StringUtil.formatNum(price)+"g", "", "§fTryk for at købe!");
            this.layout.put(slot, builder.build());
        });

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {
        if (slot == 31) player.closeInventory();

        //Clicked on a turret
        if (turretSlots.containsKey(slot)) {

            TurretType turretType = turretSlots.get(slot);
            double price = turretType.getRealPrice(game.getDifficulty());

            //Cannot afford the turret
            if (game.getGold() < price) {
                double missingGold = price - game.getGold();
                player.sendMessage("§cDet har du ikke råd til. Du mangler §e"+StringUtil.formatNum(missingGold)+"g§c!");
                player.playSound(player.getLocation(), Sound.VILLAGER_NO, 0.5f, 1.2f);
                return;
            }

            //Withdraw gold
            game.setGold(game.getGold() - price);

            //Give the turret item
            player.getInventory().addItem(turretType.getFormattedItem());
            player.playSound(player.getLocation(),Sound.ITEM_PICKUP, 1f, 0.9f);
            player.sendMessage("§7Du købte et "+turretType.getTextColor()+"§n"+turretType.getName()+"§7 for §e"+StringUtil.formatNum(price)+"g§7!");
        }

    }

}
