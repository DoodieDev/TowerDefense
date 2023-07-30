package doodieman.towerdefense.lobby.mapselector.gui;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.objects.GameSave;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.enums.MapDifficulty;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.maps.objects.MapSlot;
import doodieman.towerdefense.playerdata.objects.PlayerData;
import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class MapSelectorMenuSingleplayer extends GUI {

    final List<MapSlot> mapSlotList;

    public MapSelectorMenuSingleplayer(Player player) {
        super(player, 6, "Vælg et map");
        this.mapSlotList = MapUtil.getInstance().getMapSlots();
    }

    @Override
    public void render() {

        //Bottom standard items
        for (int i = 45; i < 54; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(49, GUIItem.BACK.getItem());

        //Loop all mapslots
        MapUtil.getInstance().getMapSlots().forEach(mapSlot -> {

            ItemBuilder itemBuilder;
            if (mapSlot.getMapID() != null) {

                Map map = MapUtil.getInstance().getMap(mapSlot.getMapID());

                //Doesn't have a save
                if (!GameUtil.getInstance().hasSavedGame(player,map)) {

                    itemBuilder = new ItemBuilder(Material.EMPTY_MAP);
                    itemBuilder.name("§f§n"+map.getMapName());
                    itemBuilder.lore("§7- §7§o"+mapSlot.getDifficulty().getColor()+mapSlot.getDifficulty().getName(), "", "§fTryk for at spille!");
                    if (map.getMapVisual().size() > 0) {
                        itemBuilder.addLore("");
                        itemBuilder.addLore(map.getMapVisual());
                    }
                }

                //Has a save
                else {
                    GameSave save = GameUtil.getInstance().getSavedGame(player,map);

                    itemBuilder = new ItemBuilder(Material.EMPTY_MAP);
                    itemBuilder.name("§f§n"+map.getMapName());
                    itemBuilder.lore(
                        "§7- §7§o"+mapSlot.getDifficulty().getColor()+mapSlot.getDifficulty().getName(),
                        "",
                        "§7[§aSAVE§7] "+save.getFormattedDate(),
                        "",
                        "§7Gemt klokken: §f"+save.getFormattedTime().replace(":","§7:§f"),
                        "§7Runde: §f"+save.getRound()+"§7/§f"+save.getDifficulty().getRounds(),
                        "",
                        "§fTryk for at spille!"
                    );

                    //Add the visual of the map
                    if (map.getMapVisual().size() > 0) {
                        itemBuilder.addLore("");
                        itemBuilder.addLore(map.getMapVisual());
                    }
                }

            }

            //Map has not been added
            else {
                itemBuilder = new ItemBuilder(Material.PAPER);
                itemBuilder.name("§f§n???");
                itemBuilder.lore("§f- "+mapSlot.getDifficulty(), "", "§fKommer snart..");
            }

            this.layout.put(mapSlot.getMenuSlot(), itemBuilder.build());
        });

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {

        //Go back to previous menu
        if (slot == 49) {
            new MapSelectorMenu(player).open();
            this.playClickSound();
            return;
        }

        //Open select difficulty menu
        MapSlot mapSlot = MapUtil.getInstance().getMapSlot(slot);
        if (mapSlot != null) {
            if (mapSlot.getMapID() == null) return;

            Map map = MapUtil.getInstance().getMap(mapSlot.getMapID());

            //Does not have any saved games. Just open difficulty menu
            if (!GameUtil.getInstance().hasSavedGame(player,map)) {
                new MapSelectorMenuDifficulty(player, map).open();
                this.playClickSound();
                return;
            }

            GameSave save = GameUtil.getInstance().getSavedGame(player,map);
            new MapSelectorMenuSave(player,map,save).open();
            this.playClickSound();
        }
    }
}
