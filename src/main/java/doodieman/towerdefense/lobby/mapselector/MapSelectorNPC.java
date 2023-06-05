package doodieman.towerdefense.lobby.mapselector;

import doodieman.towerdefense.lobby.mapselector.gui.MapSelectorMenu;
import doodieman.towerdefense.utils.CustomNpc;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class MapSelectorNPC extends CustomNpc {

    public MapSelectorNPC() {
        super("mapselector", new Location(Bukkit.getWorld("world"), 1996.5, 13, 2000.5));
        setLookClose(true);
        setNameplateVisible(false);
        setHologram(Arrays.asList("§lTryk på mig §rfor at", "starte et spil!", "§c(Langt fra færdigt)"));
        setSkinSignature("Ta4ihp1x/s4dWbUJ49bbdF3GE5SLzbDrLRL1Ir/H/QpDV92T7aIZc+r8CSqxY7zKdqmPR1At9Z0yTHrbBgFjYYreRzvJTmYTvVM+fuFyJw7cfW7eq/vpnN5gDaeADIwefih9y7Mlchafji9KNSm6H7bN0rPl+Q7y5zYPEhQVB3+hjYCsX/xx1mKeMR7nrLYMYuTH+eXlDT19plOg6ELjldI1HAorVVLgxEp7u6G3J31MslP9frpBXf7qEloT0nY3hdFKptQAM6Z8OQwPNi9Fo9pwIfa6+ZvMeuvKfHJAl2cYonSoow/RcEAWixPzTVesKHjPB0yh+RuyzA6Cin6p57jRecdeWArqyAvwzLiF+7Ar7CHk3L75LVTMNinm1VWJfg/75h34BWHIYfPUugSY1GyWl97XHT5VYXorZ1de3uT9yfghynGvqMOZA7v7DmCItom3fGG9IfOrWzSglBY714DO/eNuj+/qxuSAzDYRCkCOiu2KsUUx1vef2/lNNGBEiAlZCHSRfSK0kwA/OH1YwIPFPW0s8rLL53kdnnRTmld2c72OiszSGYUJHCSH+XEiO9BkcnibaPA/eSNMEWk11Qmt77vPWD51pB+Jdn1bQ+G8isxqdSHkY4nOrOdSzHwTWPFYBnl7139FQGRDTfgjF8lRluPv7P7y0ASsGD2yUqQ=");
        setSkinTexture("ewogICJ0aW1lc3RhbXAiIDogMTY3OTQ5NjUxMDQ1MywKICAicHJvZmlsZUlkIiA6ICJmNTljYmZjMzc5MDQ0YmZlODk0YjE5MWI5ZDU2MmY3ZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJIYW5uZSIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS80OTc5NWQxMzBjOGQxM2JiNDdkMTRiMzE5MWEyOWJjOTg5ZTY2MTY1NjkwMDE5YjcwZDJjYjZjMTEyZDg4N2RiIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");
        spawnNpc();
    }

    @Override
    public void onRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();
        new MapSelectorMenu(player).open();
    }

    @Override
    public void onLeftClick(NPCLeftClickEvent event) {

    }

}
