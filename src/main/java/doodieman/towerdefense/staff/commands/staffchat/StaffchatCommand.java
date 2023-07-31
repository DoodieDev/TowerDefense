package doodieman.towerdefense.staff.commands.staffchat;

import doodieman.towerdefense.utils.LuckPermsUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.StringJoiner;

@RequiredArgsConstructor
public class StaffchatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;

        if (args.length <= 0) {
            player.sendMessage("§cDu skal skrive en besked!");
            player.sendMessage("§cBenyt: /sc <besked>");
            return true;
        }

        //Get the map name
        StringJoiner messageBuilder = new StringJoiner(" ");
        int i = 0;
        while (args.length > i) {
            messageBuilder.add(args[i]);
            i++;
        }
        String message = messageBuilder.toString();

        String prefix = LuckPermsUtil.getPrefix(player);
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.hasPermission("staff.staffchat")) continue;
            p.sendMessage("§4[SC] "+prefix+player.getName()+": §d"+message);
        }

        return true;
    }

}
