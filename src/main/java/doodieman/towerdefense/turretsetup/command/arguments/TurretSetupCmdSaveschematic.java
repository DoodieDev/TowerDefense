package doodieman.towerdefense.turretsetup.command.arguments;

import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.values.TurretType;
import doodieman.towerdefense.turretsetup.TurretSetupHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class TurretSetupCmdSaveschematic {

    public TurretSetupCmdSaveschematic(Player player, String[] args, TurretSetupHandler handler) {
        if (args.length < 2) {
            player.sendMessage("§cSkriv /turretsetup saveschematic <turret ID>");
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

        //Check if region has been set
        FileConfiguration config = TowerDefense.getInstance().getConfig();
        if (!config.contains("turrets."+turretID+".corner1") || !config.contains("turrets."+turretID+".corner2")) {
            player.sendMessage("§cDer er ikke gemt en region af dette turret.");
            player.sendMessage("§cMarker turret området med /turretsetup setregion "+turretID);
            return;
        }

        //Save the schematic
        handler.saveSchematic(turretID);
        player.sendMessage("§aSchematic af turret '"+turretID+"' er blevet gemt!");
    }

    private String getTurretTypeList() {
        StringJoiner joiner = new StringJoiner(", ");
        for (TurretType turretType : TurretType.values()) {
            joiner.add(turretType.getId());
        }
        return joiner.toString();
    }

}
