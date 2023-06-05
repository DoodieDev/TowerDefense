package doodieman.towerdefense.turretsetup.command.arguments;

import com.sk89q.worldedit.bukkit.selections.Selection;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.values.TurretType;
import doodieman.towerdefense.turretsetup.TurretSetupHandler;
import doodieman.towerdefense.utils.LocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class TurretSetupCmdSetregion {

    public TurretSetupCmdSetregion(Player player, String[] args, TurretSetupHandler handler) {
        if (args.length < 2) {
            player.sendMessage("§cSkriv /turretsetup setregion <turret ID>");
            return;
        }

        //Check if turret exists
        String turretID = args[1].toLowerCase();
        TurretType turretType = TurretType.getByID(turretID);
        if (turretType == null) {
            player.sendMessage("§CDette slags turret eksisterer ikke!");
            player.sendMessage("§cMuligheder: §7"+this.getTurretTypeList());
            return;
        }

        //Get players selection
        Selection selection = TowerDefense.getInstance().getWorldedit().getSelection(player);
        if (selection == null) {
            player.sendMessage("§cDu skal markere et område med WorldEdit");
            return;
        }

        //Save the locations in config
        FileConfiguration config = TowerDefense.getInstance().getConfig();
        Location corner1 = selection.getMinimumPoint();
        Location corner2 = selection.getMaximumPoint();

        config.set("turrets."+turretID+".corner1", LocationUtil.locationToString(corner1));
        config.set("turrets."+turretID+".corner2", LocationUtil.locationToString(corner2));
        TowerDefense.getInstance().saveConfig();

        //Send message to player
        player.sendMessage("§aDu har sat turret '"+turretID+"' region!");
        player.sendMessage("§a- §fHjørne 1: §7X: "+corner1.getX()+" Y: "+corner1.getY()+" Z: "+corner1.getZ());
        player.sendMessage("§a- §fHjørne 2: §7X: "+corner2.getX()+" Y: "+corner2.getY()+" Z: "+corner2.getZ());
    }


    private String getTurretTypeList() {
        StringJoiner joiner = new StringJoiner(", ");
        for (TurretType turretType : TurretType.values()) {
            joiner.add(turretType.getId());
        }
        return joiner.toString();
    }
}
