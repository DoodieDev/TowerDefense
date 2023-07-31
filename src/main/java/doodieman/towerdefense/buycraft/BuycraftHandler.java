package doodieman.towerdefense.buycraft;

import dk.manaxi.unikpay.api.classes.Subscription;
import dk.manaxi.unikpay.plugin.API.Internal;
import doodieman.towerdefense.TowerDefense;
import jdk.internal.loader.AbstractClassLoaderValue.Sub;
import lombok.Getter;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BuycraftHandler implements Listener {

    @Getter
    private final BuycraftListener listener;

    private List<Subscription> subscriptionList = new ArrayList<>();

    public BuycraftHandler() {
        this.listener = new BuycraftListener(this);
        this.startSyncingSubscriptions();
    }

    public void startSyncingSubscriptions() {
        new BukkitRunnable() {
            @Override
            public void run() {
                fetchAllSubscriptions();
            }
        }.runTaskTimer(TowerDefense.getInstance(),0L, 2400L);
    }

    public void fetchAllSubscriptions() {
        TowerDefense.runAsync(() -> {
            subscriptionList = Internal.getSubscriptionsRequest();
        });
    }

    public Subscription getSubscription(OfflinePlayer player) {
        for (Subscription subscription : this.subscriptionList) {
            UUID uuid = subscription.getMcaccount().getUuid();
            if (!uuid.toString().equals(player.getUniqueId().toString())) continue;
            return subscription;
        }
        return null;
    }

}
