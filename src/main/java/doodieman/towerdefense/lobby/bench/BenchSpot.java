package doodieman.towerdefense.lobby.bench;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class BenchSpot {

    final BenchHandler handler;
    @Getter
    final Player player;
    @Getter
    final Block block;
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


}
