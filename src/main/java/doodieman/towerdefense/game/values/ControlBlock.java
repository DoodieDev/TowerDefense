package doodieman.towerdefense.game.values;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;


public enum ControlBlock {

    ILLEGAL_PLACEMENT(Material.REDSTONE_BLOCK, false),
    OBSTACLE(Material.DIAMOND_BLOCK, false);

    @Getter
    private final Material blockType;
    @Getter
    private final boolean placeable;

    ControlBlock(Material blockType, boolean placeable) {
        this.blockType = blockType;
        this.placeable = placeable;
    }

    public static ControlBlock getControlBlock(int blockX, int blockZ, World world) {
        Block block = world.getBlockAt(blockX,0, blockZ);
        for (ControlBlock controlBlock : values()) {
            if (block.getType().equals(controlBlock.getBlockType()))
                return controlBlock;
        }
        return null;
    }

    public static boolean isPlaceable(int blockX, int blockZ, World world) {
        int xCorner1 = blockX - 1;
        int zCorner1 = blockZ - 1;
        int xCorner2 = blockX + 1;
        int zCorner2 = blockZ + 1;

        for (int x = xCorner1; x <= xCorner2; x++) {
            for (int z = zCorner1; z <= zCorner2; z++) {
                ControlBlock controlBlock = getControlBlock(x,z,world);
                if (controlBlock != null && !controlBlock.isPlaceable())
                    return false;
            }
        }
        return true;
    }




}
