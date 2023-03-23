package doodieman.towerdefense.lobby.mapselector.gui;

import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public class MapSelectorMenu extends GUI {

    public MapSelectorMenu(Player player) {
        super(player, 4, "Start et spil");
    }

    @Override
    public void render() {
        //Bottom standard items
        for (int i = 27; i < 36; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(31, GUIItem.EXIT_MENU.getItem());

        //Singleplayer
        ItemBuilder singleplayer = new ItemBuilder(Material.IRON_SWORD);
        singleplayer.name("§f§nSpil alene");
        singleplayer.lore("", "§fTryk for at spille alene!");
        singleplayer.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        this.layout.put(12, singleplayer.build());

        //CO-OP
        ItemBuilder multiplayer = new ItemBuilder(Material.IRON_HELMET);
        multiplayer.name("§f§nCO-OP");
        multiplayer.lore("", "§fKommer snart..");
        multiplayer.addItemFlag(ItemFlag.HIDE_ATTRIBUTES);
        this.layout.put(14, multiplayer.build());

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {
        if (slot == 31) player.closeInventory();

        if (slot == 12) {
            new MapSelectorMenuSingleplayer(player).open();
        }

    }

}
