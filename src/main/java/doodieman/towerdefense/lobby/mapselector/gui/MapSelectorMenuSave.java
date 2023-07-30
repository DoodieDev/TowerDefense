package doodieman.towerdefense.lobby.mapselector.gui;

import dk.manaxi.unikpay.api.Config;
import dk.manaxi.unikpay.plugin.API.Internal;
import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.GameSave;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class MapSelectorMenuSave extends GUI {

    final Map map;
    final GameSave save;

    public MapSelectorMenuSave(Player player, Map map, GameSave save) {
        super(player, 4, "Vælg..");

        this.map = map;
        this.save = save;
    }

    @Override
    public void render() {

        //Bottom standard items
        for (int i = 27; i < 36; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(31, GUIItem.BACK.getItem());

        //Start new game
        ItemBuilder newGame = new ItemBuilder(Material.INK_SACK, 1, (byte) 14);
        newGame.name("§6§nStart forfra");
        newGame.lore("", "§fTryk for at starte forfra!", "", "§6Dette kan ikke fortrydes!");

        //Continue saved game
        ItemBuilder continueGame = new ItemBuilder(Material.PAPER);
        continueGame.name("§f§nSpil videre");
        continueGame.lore(
            "",
            "§7[§aSAVE§7] "+save.getFormattedDate(),
            "",
            "§7Gemt klokken: §f"+save.getFormattedTime().replace(":","§7:§f"),
            "§7Runde: §f"+save.getRound()+"§7/§f"+save.getDifficulty().getRounds(),
            "",
            "§fTryk for at spille videre!"
        );

        //Delete saved game
        ItemBuilder deleteSave = new ItemBuilder(Material.BARRIER);
        deleteSave.name("§c§nSlet dit save");
        deleteSave.lore("", "§fTryk for at slette dit save!", "", "§cDette kan ikke fortrydes!");

        this.layout.put(10, newGame.build());
        this.layout.put(13, continueGame.build());
        this.layout.put(16, deleteSave.build());

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {
        //Go back to previous menu
        if (slot == 31) {
            new MapSelectorMenuSingleplayer(player).open();
            this.playClickSound();
            return;
        }

        //New game
        if (slot == 10) {
            new MapSelectorMenuDifficulty(player, map).open();
            this.playClickSound();
            return;
        }

        //Continue game
        if (slot == 13) {
            this.playClickSound();
            player.closeInventory();
            GameUtil.getInstance().loadGame(player,map);
            return;
        }

        //Delete save
        if (slot == 16) {
            GameUtil.getInstance().deleteSavedGame(player,map);
            new MapSelectorMenuSingleplayer(player).open();
            this.playClickSound();
        }
        
    }

}
