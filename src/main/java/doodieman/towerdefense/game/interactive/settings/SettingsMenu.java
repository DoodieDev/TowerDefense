package doodieman.towerdefense.game.interactive.settings;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.values.GameSetting;
import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class SettingsMenu extends GUI {

    final Game game;

    public SettingsMenu(Player player, Game game) {
        super(player, 4, "Indstillinger");
        this.game = game;
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
        leaveGame.lore("§7§oDine fremskridt vil blive gemt!", "", "§fTryk for at forlade spillet!");
        this.layout.put(16, leaveGame.build());

        //Auto start
        boolean autoStartStatus = game.getGameSetting(GameSetting.AUTO_START);
        ItemBuilder autoStart = new ItemBuilder(Material.EMERALD);
        autoStart.name("§f§nAuto start");
        autoStart.lore(
            "",
            "§fStatus: "+ (autoStartStatus ? "§aTIL" : "§cFRA"),
            "",
            autoStartStatus ? "§7Tryk for at slå auto start fra." : "§7Tryk for at slå auto start til."
        );
        this.layout.put(10, autoStart.build());

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {
        if (slot == 31) player.closeInventory();

        if (slot == 16) {
            player.closeInventory();
            this.playClickSound();
            GameUtil.getInstance().exitGame(player, true);
        }

        if (slot == 10) {
            this.playClickSound();
            game.setGameSetting(GameSetting.AUTO_START,!game.getGameSetting(GameSetting.AUTO_START));
            this.render();
        }

    }

}
