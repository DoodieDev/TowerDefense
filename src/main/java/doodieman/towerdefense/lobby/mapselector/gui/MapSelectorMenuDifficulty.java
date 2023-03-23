package doodieman.towerdefense.lobby.mapselector.gui;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import lombok.Getter;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class MapSelectorMenuDifficulty extends GUI {

    @Getter
    private final Map map;

    private final HashMap<Integer, Difficulty> difficultySlots = new HashMap<>();

    public MapSelectorMenuDifficulty(Player player, Map map) {
        super(player, 4, "Vælg en sværhedsgrad");
        this.map = map;

        this.difficultySlots.put(12, Difficulty.EASY);
        this.difficultySlots.put(13, Difficulty.MEDIUM);
        this.difficultySlots.put(14, Difficulty.HARD);
    }

    @Override
    public void render() {
        //Bottom standard items
        for (int i = 27; i < 36; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(31, GUIItem.BACK.getItem());

        //Difficulties
        difficultySlots.forEach((slot, difficulty) -> {

            ItemBuilder itemBuilder = new ItemBuilder(difficulty.getItem());
            itemBuilder.name(difficulty.getTextColor()+"§n"+difficulty.getName());
            itemBuilder.lore(
                "",
                "§fBelønninger:",
                "§f+ §2$§a250",
                "§f+ §a1.000 §2XP",
                "",
                "§fTryk for at vælge",
                "§fdenne sværhedsgrad!"
            );
            itemBuilder.addItemFlag(ItemFlag.HIDE_POTION_EFFECTS);

            this.layout.put(slot, itemBuilder.build());
        });

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {
        if (slot == 31) {
            new MapSelectorMenuSingleplayer(player).open();
            this.playClickSound();
            return;
        }

        if (difficultySlots.containsKey(slot)) {
            Difficulty difficulty = difficultySlots.get(slot);
            GameUtil.getInstance().startGame(player, map, difficulty);
            this.playClickSound();
        }
    }

}
