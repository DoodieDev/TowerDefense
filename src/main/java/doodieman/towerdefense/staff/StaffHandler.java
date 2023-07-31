package doodieman.towerdefense.staff;

import doodieman.towerdefense.playerdata.PlayerDataUtil;
import doodieman.towerdefense.playerdata.objects.PlayerData;
import doodieman.towerdefense.utils.InventoryUtil;
import doodieman.towerdefense.utils.LocationUtil;
import doodieman.towerdefense.utils.LuckPermsUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class StaffHandler {

    @Getter
    private final List<Player> staffModePlayers = new ArrayList<>();
    @Getter
    private final StaffListener listener;
    @Getter
    private static StaffHandler instance;

    public StaffHandler() {
        instance = this;
        this.listener = new StaffListener(this);
    }

    public boolean isInStaffMode(Player player) {
        return this.staffModePlayers.contains(player);
    }

    public void exitAllStaffModes() {
        for (Player player : this.staffModePlayers)
            this.setStaffMode(player,false);
    }

    public void setStaffMode(Player player, boolean newStatus) {

        PlayerData playerData = PlayerDataUtil.getData(player);
        if (!playerData.getFile().contains("staff.staffmode"))
            playerData.getFile().createSection("staff.staffmode");
        ConfigurationSection section = playerData.getFile().getConfigurationSection("staff.staffmode");

        section.set("status", newStatus);

        //Set staff mode to true
        if (newStatus && !this.isInStaffMode(player)) {

            section.set("inventory", InventoryUtil.toBase64(player.getInventory()));
            section.set("location", LocationUtil.locationToString(player.getLocation()));

            player.setGameMode(GameMode.SPECTATOR);
            Bukkit.broadcastMessage("§8[§c-§8] "+ LuckPermsUtil.getRankColor(player)+player.getName());

            this.staffModePlayers.add(player);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("staff.staffmode")) continue;
                p.hidePlayer(player);
            }
        }

        //Set staff mode to false
        else if (!newStatus && this.isInStaffMode(player)) {

            player.setGameMode(GameMode.ADVENTURE);

            //Restore location
            Location location = LocationUtil.stringToLocation(section.getString("location"));
            player.teleport(location);

            //Restore inventory
            Inventory inventory = InventoryUtil.fromBase64(section.getString("inventory"));
            for (int i = 0; i < inventory.getSize(); i++)
                player.getPlayer().getInventory().setItem(i,inventory.getItem(i));

            Bukkit.broadcastMessage("§8[§a+§8] "+ LuckPermsUtil.getRankColor(player)+player.getName());

            this.staffModePlayers.remove(player);
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.hasPermission("staff.staffmode")) continue;
                p.showPlayer(player);
            }
        }

        playerData.save();
    }

}
