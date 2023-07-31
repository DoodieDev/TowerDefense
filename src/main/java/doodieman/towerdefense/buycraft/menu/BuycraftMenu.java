package doodieman.towerdefense.buycraft.menu;

import dk.manaxi.unikpay.api.classes.DurationType;
import dk.manaxi.unikpay.api.classes.Pakke;
import dk.manaxi.unikpay.api.classes.Subscription;
import dk.manaxi.unikpay.plugin.API.Internal;
import doodieman.towerdefense.buycraft.BuycraftHandler;
import doodieman.towerdefense.utils.GUI;
import doodieman.towerdefense.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class BuycraftMenu extends GUI {

    final BuycraftHandler handler;

    public BuycraftMenu(Player player, BuycraftHandler handler) {
        super(player, 4, "Butik");

        this.handler = handler;
    }

    @Override
    public void render() {

        //Bottom standard items
        for (int i = 27; i < 36; i++) {
            this.layout.put(i, GUIItem.GLASS_FILL.getItem());
        }
        this.layout.put(31, GUIItem.EXIT_MENU.getItem());

        Subscription subscription = handler.getSubscription(player);

        //Has donator rank, it is still active
        if (subscription != null && player.hasPermission("group.stjerne") && subscription.getStatus().equalsIgnoreCase("ACCEPTED")) {

            ItemBuilder itemBuilder = new ItemBuilder(Material.BOOK);
            itemBuilder.name("§3[§bStjerne§3] §7(abonnement)");
            itemBuilder.lore(
                "",
                "§fStjerne ranken er den eneste",
                "§fDonator rank vi tilbyder på Sol.",
                "",
                "§3§nFordele:",
                "§3- §bet sejt prefix i chatten",
                "§3- §ben sej farve i TAB",
                "§3- §badgang til at flyve inde i spil",
                "",
                "§7§oDit abonnement udløber <ikke lavet>",
                "",
                "§cTryk for at stoppe dit abonnement!"
            );
            itemBuilder.makeGlowing();

            this.layout.put(13,itemBuilder.build());
        }

        //Has donator rank. - Is cancelled
        if (subscription != null && player.hasPermission("group.stjerne") && subscription.getStatus().equalsIgnoreCase("CANCELLED")) {

            ItemBuilder itemBuilder = new ItemBuilder(Material.BARRIER);
            itemBuilder.name("§c§nDu har allerede Stjerne rank");
            itemBuilder.lore(
                "",
                "§fDu kan først abonnere igen,",
                "§fnår din stjerne rank er udløbet.",
                "",
                "§7§oDit stjerne rank udløber <ikke lavet>"
            );
            itemBuilder.makeGlowing();

            this.layout.put(13,itemBuilder.build());
        }

        //Does not have donator rank
        else {
            ItemBuilder itemBuilder = new ItemBuilder(Material.BOOK);
            itemBuilder.name("§3[§bStjerne§3] §7(abonnement)");
            itemBuilder.lore(
                "",
                "§fStjerne ranken er den eneste",
                "§fdonator rank vi tilbyder på Sol.",
                "",
                "§3§nFordele:",
                "§3- §bet sejt prefix i chatten",
                "§3- §ben sej farve i TAB",
                "§3- §badgang til at flyve inde i spil",
                "",
                "§7Pris: §a750 §2ems §7i ugen",
                "",
                "§fTryk for at abonnere!"
            );
            itemBuilder.makeGlowing();
            this.layout.put(13, itemBuilder.build());
        }

        super.render();
    }

    @Override
    public void click(int slot, ItemStack clickedItem, ClickType clickType, InventoryType inventoryType) {

        if (slot == 31) {
            player.closeInventory();
        }

        //Stjerne rank
        if (slot == 13) {

            Subscription subscription = handler.getSubscription(player);

            //Already has a subscription
            if (subscription != null) {
                if (subscription.getStatus().equalsIgnoreCase("CANCELLED")) return;

                player.closeInventory();
                Internal.cancelSubscription(subscription);
                player.sendMessage("§cDit abonnement er blevet afmeldt!");

                return;
            }

            player.closeInventory();
            player.playSound(player.getLocation(), Sound.LEVEL_UP,1f,1.2f);

            //Send UnikPay request
            Pakke pakke = new Pakke(750,"Stjerne Abonnement","stjerne");
            Internal.sendSubscriptionRequest(player,pakke,7, DurationType.DAY);
        }

    }

}
