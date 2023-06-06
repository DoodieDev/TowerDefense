package doodieman.towerdefense.lobby.mapselector.gui;

import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
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

public class MapSelectorMenuSingleplayer extends GUI {

    private final HashMap<Integer, MapSlot> mapSlots = new HashMap<>();

    public MapSelectorMenuSingleplayer(Player player) {
        super(player, 6, "Vælg et map");

        mapSlots.put(10, new MapSlot("eventyr", "Begynder"));
        mapSlots.put(19, new MapSlot("træstammen", "Begynder"));
        mapSlots.put(28, new MapSlot(null, "Begynder"));

        mapSlots.put(12, new MapSlot("haven", "Øvede"));
        mapSlots.put(21, new MapSlot("grønland", "Øvede"));
        mapSlots.put(30, new MapSlot(null, "Øvede"));

        mapSlots.put(14, new MapSlot("udflugten", "Avanceret"));
        mapSlots.put(23, new MapSlot(null, "Avanceret"));
        mapSlots.put(32, new MapSlot(null, "Avanceret"));

        mapSlots.put(16, new MapSlot(null, "Expert"));
        mapSlots.put(25, new MapSlot(null, "Expert"));
        mapSlots.put(34, new MapSlot(null, "Expert"));

    }

    @Override
    public void render() {
        //Bottom standard items
        for (int i = 45; i < 54; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(49, GUIItem.BACK.getItem());

        mapSlots.forEach((slot, mapSlot) -> {
            ItemBuilder itemBuilder;

            if (mapSlot.getMapID() != null) {
                Map map = MapUtil.getInstance().getMap(mapSlot.getMapID());

                itemBuilder = new ItemBuilder(Material.EMPTY_MAP);
                itemBuilder.name("§f§n"+map.getMapName());
                itemBuilder.lore("§f- §7§o"+mapSlot.getDifficulty(), "", "§fTryk for at spille!");
                if (map.getMapVisual().size() > 0) {
                    itemBuilder.addLore("");
                    itemBuilder.addLore(map.getMapVisual());
                }

            } else {
                itemBuilder = new ItemBuilder(Material.PAPER);
                itemBuilder.name("§f§n???");
                itemBuilder.lore("§f- §7§o"+mapSlot.getDifficulty(), "", "§fKommer snart..");

            }

            this.layout.put(slot, itemBuilder.build());
        });

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {
        if (slot == 49) {
            new MapSelectorMenu(player).open();
            this.playClickSound();
            return;
        }

        //Open select difficulty menu
        if (mapSlots.containsKey(slot)) {
            MapSlot mapSlot = mapSlots.get(slot);
            if (mapSlot.getMapID() == null) return;
            Map map = MapUtil.getInstance().getMap(mapSlot.getMapID());

            new MapSelectorMenuDifficulty(player, map).open();
            this.playClickSound();
        }

    }

    @RequiredArgsConstructor
    private class MapSlot {
        @Getter
        private final String mapID;
        @Getter
        private final String difficulty;
    }

}
