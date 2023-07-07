package doodieman.towerdefense.lobby.bench.events;

import doodieman.towerdefense.lobby.bench.BenchSpot;
import lombok.Getter;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BenchSitEvent extends Event implements Cancellable {

    @Getter
    private final Player player;
    @Getter
    private final BenchSpot benchSpot;

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled;

    public BenchSitEvent(Player player, BenchSpot benchSpot) {
        this.player = player;
        this.benchSpot = benchSpot;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }


}
