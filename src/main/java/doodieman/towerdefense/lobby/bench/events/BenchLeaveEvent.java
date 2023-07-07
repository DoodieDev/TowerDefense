package doodieman.towerdefense.lobby.bench.events;

import doodieman.towerdefense.lobby.bench.BenchSpot;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BenchLeaveEvent extends Event {

    @Getter
    private final Player player;
    @Getter
    private final BenchSpot benchSpot;

    private static final HandlerList HANDLERS = new HandlerList();

    public BenchLeaveEvent(Player player, BenchSpot benchSpot) {
        this.player = player;
        this.benchSpot = benchSpot;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
