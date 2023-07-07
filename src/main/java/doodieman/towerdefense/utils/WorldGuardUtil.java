package doodieman.towerdefense.utils;

import com.sk89q.worldguard.bukkit.RegionContainer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldGuardUtil {

    private static final WorldGuardPlugin worldGuardPlugin = WorldGuardPlugin.inst();

    public static List<Player> playersWithinRegion(Collection<? extends Player> players, World world, @Nullable String checkRegionId) {
        RegionManager regionManager = regionManager(world);

        return players.stream()
            .filter(player -> {
                ApplicableRegionSet regionSet = regionManager.getApplicableRegions(player.getLocation());

                for (ProtectedRegion region : regionSet)
                    if (Objects.equals(region.getId(), checkRegionId)) return true;

                return false;
            })
            .collect(Collectors.toList());
    }

    public static Set<ProtectedRegion> getRegionsAt(Location loc) {
        RegionManager regionManager = worldGuardPlugin.getRegionManager(loc.getWorld());
        return regionManager.getApplicableRegions(loc).getRegions();
    }

    public static boolean isAtRegion(Location location, String regionID) {
        for (ProtectedRegion region : getRegionsAt(location)) {
            if (region.getId().equalsIgnoreCase(regionID)) return true;
        }
        return false;
    }

    public static RegionContainer regionContainer() {
        return worldGuardPlugin.getRegionContainer();
    }

    public static RegionManager regionManager(Location location) {
        return regionManager(location.getWorld());
    }

    public static RegionManager regionManager(World world) {
        return regionContainer().get(world);
    }


}
