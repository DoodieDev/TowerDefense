package doodieman.towerdefense.maps.objects;

import doodieman.towerdefense.maps.enums.MapDifficulty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapSlot {

    @Getter
    private final String mapID;
    @Getter
    private final MapDifficulty difficulty;
    @Getter
    private final int menuSlot;
}
