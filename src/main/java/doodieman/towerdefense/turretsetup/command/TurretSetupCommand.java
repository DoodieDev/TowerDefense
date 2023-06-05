package doodieman.towerdefense.turretsetup.command;

import doodieman.towerdefense.turretsetup.TurretSetupHandler;
import doodieman.towerdefense.turretsetup.command.arguments.TurretSetupCmdSaveschematic;
import doodieman.towerdefense.turretsetup.command.arguments.TurretSetupCmdSetregion;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class TurretSetupCommand implements CommandExecutor {

    private final TurretSetupHandler handler;

    public void sendHelpMessage(Player player) {
        player.sendMessage("");
        player.sendMessage("§6/turretsetup setregion <turret ID> §fSæt region for turret");
        player.sendMessage("§6/turretsetup saveschematic <turret ID> §fGem schematic af turret");
        player.sendMessage("");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player))
            return true;

        Player player = (Player) sender;
        if (args.length < 1) {
            this.sendHelpMessage(player);
            return true;
        }

        String subcommand = args[0].toUpperCase();

        switch (subcommand) {

            case "SETREGION":
                new TurretSetupCmdSetregion(player, args, handler);
                break;

            case "SAVESCHEMATIC":
                new TurretSetupCmdSaveschematic(player, args, handler);
                break;


            default:
                this.sendHelpMessage(player);
                break;

        }

        return true;
    }

}
