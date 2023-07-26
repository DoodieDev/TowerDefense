package doodieman.towerdefense.game.interactive.settings;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class SettingsMenu extends GUI {

    public SettingsMenu(Player player) {
        super(player, 4, "Indstillinger");
    }

    @Override
    public void render() {
        //Bottom standard items
        for (int i = 27; i < 36; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(31, GUIItem.EXIT_MENU.getItem());

        //Leave game
        ItemBuilder leaveGame = new ItemBuilder(Material.INK_SACK, 1, (short) 1);
        leaveGame.name("§c§nForlad og gem");
        leaveGame.lore("§7§oDine fremskridt vil blive gemt! §c(Ikke lavet)", "", "§fTryk for at forlade spillet!");
        this.layout.put(15, leaveGame.build());

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {
        if (slot == 31) player.closeInventory();

        if (slot == 15) {
            player.closeInventory();
            GameUtil.getInstance().exitGame(player, true);
        }

    }

}
