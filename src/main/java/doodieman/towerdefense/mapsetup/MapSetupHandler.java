package doodieman.towerdefense.mapsetup;

import lombok.Getter;

public class MapSetupHandler {

    @Getter
    private final MapSetupListener listener;

    public MapSetupHandler() {
        this.listener = new MapSetupListener(this);
    }

}
