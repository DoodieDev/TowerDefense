package doodieman.towerdefense.game;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import doodieman.towerdefense.game.objects.Game;
import doodieman.towerdefense.game.turrets.GameTurret;
import doodieman.towerdefense.game.turrets.GameTurretArmorstand;
import doodieman.towerdefense.game.values.TurretType;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Constructor;

public class TurretUtil {

    final GameHandler handler;

    @Getter
    private static TurretUtil instance;

    public TurretUtil(GameHandler handler) {
        this.handler = handler;
        instance = this;
    }

    //Check if item is a turret
    public boolean isTurretItem(ItemStack item) {
        if (item == null || item.getType().equals(Material.AIR))
            return false;
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasNBTData() && nbtItem.hasKey("turret");
    }

    //Get turret type from itemstack
    public TurretType getTurretFromItem(ItemStack item) {
        NBTItem nbtItem = new NBTItem(item);
        NBTCompound compound = nbtItem.getCompound("turret");
        return TurretType.getByID(compound.getString("type"));
    }

    //Create a brand new turret
    public GameTurret createTurret(Game game, TurretType turretType, Location location) {

        try {
            Class<? extends GameTurret> turretClass = turretType.getTurretClass();
            Constructor<? extends GameTurret> turretConstructor = turretClass.getConstructor(Game.class, TurretType.class, Location.class);

            GameTurret turret = turretConstructor.newInstance(game, turretType, location);
            game.getTurrets().add(turret);

            return turret;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    //Checks for redstone blocks in a 1x1 and down to void
    public boolean canTurretBePlaced(Game game, Location location) {
        //Step 1 - Check for y-level
        Location mobSpawnLoc = game.getMobPath().get(0);
        if (location.getY() < mobSpawnLoc.getBlockY() || location.getY() > mobSpawnLoc.getBlockY())
            return false;

        //Step 2 - Check for redstone blocks
        Location corner1 = location.clone().add(-1,0,-1);
        corner1.setY(0);
        Location corner2 = location.clone().add(1,0,1);
        for (double y = corner1.getY(); y <= corner2.getY(); y++) {
            for (double x = corner1.getX(); x <= corner2.getX(); x++) {
                for (double z = corner1.getZ(); z <= corner2.getZ(); z++) {
                    Location loc = new Location(corner1.getWorld(),x,y,z);
                    Block block = corner1.getWorld().getBlockAt(loc);
                    if (block.getType() == Material.REDSTONE_BLOCK || block.getType() == Material.DIAMOND_BLOCK)
                        return false;
                }
            }
        }

        //All steps passed
        return true;
    }

    //Remove turret items from player inventory b
    public void removeTurretItems(Player player, TurretType turretType, int amount) {
        for (int i = 0; i < player.getInventory().getSize(); i++) {
            if (amount <= 0) return;
            ItemStack item = player.getInventory().getItem(i);
            if (item == null || item.getType() == Material.AIR) continue;
            if (!isTurretItem(item) || !getTurretFromItem(item).equals(turretType)) continue;

            int itemAmount = item.getAmount();

            //Needs to remove more than in this itemstack
            if (amount >= itemAmount) {
                player.getInventory().setItem(i, new ItemStack(Material.AIR));
                amount -= itemAmount;

            } else {
                item.setAmount(itemAmount - amount);
                amount = 0;
            }
        }
    }

    //Get turret object from an armorstand
    public GameTurret getTurretFromArmorStand(Game game, ArmorStand armorStand) {
        for (GameTurret turret : game.getTurrets()) {
            for (GameTurretArmorstand turretArmorstand : turret.getArmorStandList()) {
                if (!turretArmorstand.getArmorStand().getUniqueId().equals(armorStand.getUniqueId())) continue;
                return turret;
            }
        }
        return null;
    }



}
