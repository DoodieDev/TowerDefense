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
        super("mapselector", new Location(Bukkit.getWorld("world"), 1257.5, 61, 982.5));
        setLookClose(true);
        setNameplateVisible(false);
        setHologram(Arrays.asList("§f§lSOL", "", "§fVelkommen til vores", "§oTower Defense§f server!", "", "{animation: lars.yml}§f for", "§fat komme igang!", "", "§7§nLars"));
        setSkinSignature("oqAdRyTD26YRvdA34PiiUtXLzN+NySXNvvMgb4S54cCX+evBMvGnfclKCXpzid2ZXR6wiB7MtG42hwZv3xRiIUsZ0xk84dhL6zOLwVLHeJebik3rEeRJCyCgohOx34eXMjKdikEfr66l8IRdYCgAosyOY2BpFrTH3/xXF/kV0oRraf/yxBy8o1U7m0B3Mn1btsLPkKPG0yqAzGZBPu67clbTn1bKOK4UGbeshFRJpBdPlISGCl3xaiYv4Q+YgIDr8JKPcWt6E1KhBjJvKv+NMd3yHOFwtJEroXJFDa7AI3hgOwsv4jFL7JtGrYtHTrKoI0lNS4eOLTN7mHVcRssHZtqFG7KXuGzCvd14PJVWFKjqI1Y77RCITfXxmLOukmKHl2MpAf4JPz7IpbinmS3EFGlB/X89NfuhBDc17hqaU0bFV+2p3uZ5srAPioOLGngy72yfl279tdMn5XBut1Y8Nd4S1FeR6kZ4ahvTp1rpAPqUO+us9LNUpyqWzrs0CcDTEtTmq9g6j20LeibJ2GvSEuVHWNxU3s00Su+fPdXpqiTTFn7iVYq6M3rOd9BfGVVgQYe1DjP3Q0gFcojkHGHYdI4luRbBiJsj3ozM1d4nPlSU50/AaOKYUJUd0A96Qs9aZH6kNzaw1HCNNkPY+0NHS3HYOLBu0230phfjMowSTyE=");
        setSkinTexture("ewogICJ0aW1lc3RhbXAiIDogMTYyMTUwODQxODQ0NywKICAicHJvZmlsZUlkIiA6ICJiMGQ3MzJmZTAwZjc0MDdlOWU3Zjc0NjMwMWNkOThjYSIsCiAgInByb2ZpbGVOYW1lIiA6ICJPUHBscyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9lY2JjYzg1NGQxMTE0MDg4NDgzMGY0ZWNkNDA3ZmNkMTNhODI4NTY2YzNkM2VjNzA1NWU2NDY1NDgxYTUwYzBlIiwKICAgICAgIm1ldGFkYXRhIiA6IHsKICAgICAgICAibW9kZWwiIDogInNsaW0iCiAgICAgIH0KICAgIH0KICB9Cn0=");
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
