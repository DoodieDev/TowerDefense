package doodieman.towerdefense.buycraft;

import dk.manaxi.unikpay.api.classes.Pakke;
import dk.manaxi.unikpay.api.classes.Subscription;
import dk.manaxi.unikpay.plugin.API.Internal;
import dk.manaxi.unikpay.plugin.event.OnSubscriptionPayment;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.buycraft.menu.BuycraftMenu;
import doodieman.towerdefense.utils.LuckPermsUtil;
import doodieman.towerdefense.utils.StringUtil;
import doodieman.towerdefense.utils.WorldGuardUtil;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.TimeUnit;

public class BuycraftListener implements Listener {

    final BuycraftHandler handler;

    public BuycraftListener(BuycraftHandler handler) {
        this.handler = handler;

        Bukkit.getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    @EventHandler
    public void onSubscribe(OnSubscriptionPayment event) {

        OfflinePlayer player = event.getPlayer();
        Subscription sub = event.getSub();
        //Pakke pakke = event.getPakke();

        Internal.acceptPackageRequest(event.getId());

        //Luckperms
        User user = LuckPermsUtil.getLuckPermsUser(player.getUniqueId());
        InheritanceNode node = InheritanceNode.builder("stjerne")
            .expiry(7, TimeUnit.DAYS)
            .build();
        user.data().add(node);
        TowerDefense.getInstance().getLuckPerms().getUserManager().saveUser(user);

        String prefix = LuckPermsUtil.getGroupPrefix("stjerne");
        TowerDefense.getInstance().announce("§f"+player.getName()+" §7har lige købt "+prefix+"§7i "+ StringUtil.formatNum(sub.getDuration().doubleValue())+" dage!");

        handler.fetchAllSubscriptions();
    }

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Location location = event.getRightClicked().getLocation();

        if (!WorldGuardUtil.isAtRegion(location,"buy")) return;

        new BuycraftMenu(player, handler).open();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) return;
        Location location = event.getClickedBlock().getLocation();

        if (!WorldGuardUtil.isAtRegion(location,"buy")) return;

        new BuycraftMenu(player, handler).open();
    }

}
