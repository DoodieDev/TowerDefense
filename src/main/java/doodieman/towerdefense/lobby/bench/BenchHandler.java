package doodieman.towerdefense.lobby.bench;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.utils.WorldGuardUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BenchHandler {

    @Getter
    public List<BenchSpot> occupiedBenches = new ArrayList<>();

    public BenchHandler() {
        Bukkit.getPluginManager().registerEvents(new BenchListener(this), TowerDefense.getInstance());

        Bukkit.getWorld("world").getEntitiesByClass(ArmorStand.class)
            .stream()
            .filter(armorStand -> armorStand.getCustomName() != null && armorStand.getCustomName().equalsIgnoreCase("bench"))
            .forEach(Entity::remove);
    }

    public boolean isBench(Location location) {
        return WorldGuardUtil.getRegionsAt(location)
            .stream()
            .anyMatch(protectedRegion -> protectedRegion.getId().startsWith("bench-"));
    }

    public String getBenchID(Location location) {
        return WorldGuardUtil.getRegionsAt(location)
            .stream()
            .filter(protectedRegion -> protectedRegion.getId().startsWith("bench-"))
            .findFirst()
            .get()
            .getId()
            .replace("bench-","");
    }

    public BenchSpot getBenchSpot(Player player) {
        for (BenchSpot spot : occupiedBenches) {
            if (spot.getPlayer().equals(player))
                return spot;
        }
        return null;
    }

    public BenchSpot getBenchSpot(Block block) {
        for (BenchSpot spot : occupiedBenches) {
            if (spot.getBlock().equals(block))
                return spot;
        }
        return null;
    }

}
