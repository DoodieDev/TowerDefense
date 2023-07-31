package doodieman.towerdefense.staff.commands.staff;

import doodieman.towerdefense.game.GameUtil;
import doodieman.towerdefense.staff.StaffHandler;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class StaffCommand implements CommandExecutor {

    final StaffHandler staffHandler;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (GameUtil.getInstance().isInGame(player)) {
            player.sendMessage("§cDu kan ikke gå i staffmode ingame!");
            return true;
        }

        //Toggle status
        boolean currentStatus = staffHandler.isInStaffMode(player);
        staffHandler.setStaffMode(player,!currentStatus);

        return true;
    }

}
