package doodieman.towerdefense.simpleevents.region;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RegionLeaveEvent extends Event implements Cancellable {

    @Getter
    private Player player;
    @Getter
    private ProtectedRegion region;


    private static final HandlerList HANDLERS = new HandlerList();
    private boolean isCancelled;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public RegionLeaveEvent(Player player, ProtectedRegion region) {
        this.isCancelled = false;
        this.player = player;
        this.region = region;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
