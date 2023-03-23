package doodieman.towerdefense.lobby.mapselector;

import lombok.Getter;

public class MapSelectorHandler {

    @Getter
    private final MapSelectorNPC npc;

    public MapSelectorHandler() {
        this.npc = new MapSelectorNPC();
    }

}
