package doodieman.towerdefense.game.turrets.menu;

import doodieman.towerdefense.game.turrets.GameTurret;
import doodieman.towerdefense.game.values.TurretType;
import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import doodieman.towerdefense.utils.StringUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class TurretMenu extends GUI {

    final GameTurret turret;
    final TurretType turretType;

    public TurretMenu(Player player, GameTurret turret) {
        super(player, 4, turret.getTurretType().getName());

        this.turret = turret;
        this.turretType = turret.getTurretType();
    }

    @Override
    public void render() {
        //Bottom standard items
        for (int i = 27; i < 36; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(31, GUIItem.EXIT_MENU.getItem());


        //Information
        String turretColor = StringUtil.colorize(turretType.getTextColor());
        ItemBuilder informationBuilder = new ItemBuilder(Material.BOOK);
        informationBuilder.name(turretColor+"§nInformation");
        informationBuilder.lore(
            "",
            "§c§oKommer snart.."
        );

        //Upgrades
        ItemBuilder upgradesBuilder = new ItemBuilder(Material.GOLD_INGOT);
        upgradesBuilder.name("§e§nOpgraderinger");
        upgradesBuilder.lore(
            "",
            "§c§oKommer snart.."
        );

        //Delete
        ItemBuilder deleteBuilder = new ItemBuilder(Material.BARRIER);
        deleteBuilder.name("§c§nSlet tårn");
        deleteBuilder.lore(
            "",
            "§fTryk for at",
            "§fslette tårnet!",
            "",
            "§7Du vil modtage §e"+StringUtil.formatNum(turret.getSellPrice())+"g"
        );

        this.layout.put(11, informationBuilder.build());
        this.layout.put(13, upgradesBuilder.build());
        this.layout.put(15, deleteBuilder.build());

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {

        if (slot == 31) {
            player.closeInventory();
            this.playClickSound();
            return;
        }

        if (slot == 15) {
            player.closeInventory();
            player.playSound(player.getLocation(), Sound.ITEM_PICKUP,1f,1f);

            String turretColor = StringUtil.colorize(turretType.getTextColor());
            player.sendMessage("§7Du solgte dit "+turretColor+turretType.getName()+" §7for §e"+StringUtil.formatNum(turret.getSellPrice())+"g§7!");

            turret.getGame().addGold(turret.getSellPrice());
            turret.removeTurret();
        }

    }

}
