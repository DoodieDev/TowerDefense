package doodieman.towerdefense.buycraft;

import dk.manaxi.unikpay.api.classes.DurationType;
import dk.manaxi.unikpay.api.classes.Pakke;
import dk.manaxi.unikpay.plugin.API.Internal;
import dk.manaxi.unikpay.plugin.fetch.Payments;
import doodieman.towerdefense.buycraft.menu.BuycraftMenu;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class BuycraftCommand implements CommandExecutor {

    final BuycraftHandler handler;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        new BuycraftMenu(player, handler).open();
        return true;
    }

}
