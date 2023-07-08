package doodieman.towerdefense.lobby.bench;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;

public class BenchSpot {

    final BenchHandler handler;
    @Getter
    final Player player;
    @Getter
    final Block block;
    @Getter
    final String benchID;
    @Getter
    ArmorStand armorStand;

    public BenchSpot(Player player, Block block, BenchHandler handler, String benchID) {
        this.player = player;
        this.block = block;

        this.handler = handler;
        this.benchID = benchID;
    }

    public void startSitting() {
        Location roundedLoc = block.getLocation().clone().add(0.5, 0.3, 0.5);
        roundedLoc.setYaw(this.getRelativeYaw());
        this.armorStand = block.getWorld().spawn(roundedLoc, ArmorStand.class);
        this.armorStand.setMarker(true);
        this.armorStand.setVisible(false);
        this.armorStand.setCustomName("bench");
        this.armorStand.setPassenger(player);

        handler.getOccupiedBenches().add(this);
    }

    public void stopSitting() {
        this.armorStand.remove();
        handler.getOccupiedBenches().remove(this);
    }

    private BlockFace getBlockFace() {
        if (block.getState().getData() instanceof Directional) {
            Directional directionalData = (Directional) block.getState().getData();
            return directionalData.getFacing();
        }
        return BlockFace.NORTH;
    }

    private float getRelativeYaw() {
        switch (this.getBlockFace()) {
            case NORTH:
                return 180;
            case EAST:
                return -90;
            case WEST:
                return 90;
            case SOUTH:
            default:
                return 0;
        }
    }

}
