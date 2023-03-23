package doodieman.towerdefense.utils;

import doodieman.towerdefense.TowerDefense;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class GUI implements Listener {
    protected Player player;
    protected Inventory menu;

    public Map<Integer, ItemStack> layout = new HashMap<>();

    public final Long clickCooldown = 100L;
    private Long currentCooldown = 0L;


    private final int rows;
    private final String name;

    public GUI(Player player, int rows, String name) {
        this.player = player;
        this.menu = Bukkit.createInventory(null, (rows*9), name);

        this.rows = rows;
        this.name = name;
    }

    public void render() {
        menu.clear();

        for (Map.Entry<Integer, ItemStack> layoutItem : layout.entrySet()) {
            menu.setItem(layoutItem.getKey(),layoutItem.getValue());
        }
    }

    public enum GUIItem {

        EXIT_MENU(new ItemBuilder(Material.NETHER_STAR,1, (byte) 0,"§fLuk menuen", "§7§oTryk for at lukke menuen.").build()),
        GLASS_FILL(new ItemBuilder(Material.STAINED_GLASS_PANE, 1, (byte) 0, "§r").build()),
        BACK(new ItemBuilder(Material.ARROW, 1, (byte) 0, "§fTilbage","§7§oTryk for at gå tilbage","§7§otil den forrige menu.").build());

        private final ItemStack item;
        GUIItem(ItemStack item) {
            this.item = item;
        }
        public ItemStack getItem() {
            return this.item;
        }
    }

    //This is the event called when clicking.
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) { }
    public void closed() {}


    //Click cooldown.
    private void setCooldown() {
        this.currentCooldown = System.currentTimeMillis();
    }
    private boolean hasCooldown() {
        return System.currentTimeMillis() < currentCooldown + clickCooldown;
    }

    //Open the menu to a player here
    public void open() {
        this.render();
        player.openInventory(menu);
        TowerDefense.getInstance().getServer().getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    public void renameTitle(String newName) {
        this.menu = Bukkit.createInventory(null, (rows*9), newName);
        this.render();
        HandlerList.unregisterAll(this); // Unregister events while re-opening
        player.openInventory(menu);
        TowerDefense.getInstance().getServer().getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    //EVENT LISTENERS
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        if (!event.getInventory().equals(menu)) return;

        //TODO make it possible to allow clickable slots
        event.setCancelled(true);

        if(hasCooldown()) return;
        setCooldown();

        if(event.getClickedInventory() == null) return;

        this.click(event.getRawSlot(), event.getCurrentItem(), event.getClick(), event.getClickedInventory().getType());
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        if (!event.getInventory().equals(menu)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent event) {
        if (event.getInventory().equals(menu)) {
            HandlerList.unregisterAll(this);
            this.closed();
        }
    }
}
