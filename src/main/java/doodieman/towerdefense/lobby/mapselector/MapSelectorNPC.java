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
        setHologram(Arrays.asList("✦§f §f §f §lSOL §f §f✦", "", "§fVelkommen til vores", "§oTower Defense§f server!", "", "§n§lSnak med Lars§f for", "§fat komme igang!", "", "§7§nLars"));
        setSkinSignature("Sz33kBWoE8IlEhc2as8P9cTwK/hnfX0FtYeiVQU3RwnxKHQLYlRu1WS7aUqpEBp20r9U0u6t2VGoRaHOdTu8Zre3b7/2RTU78r/X51Pl0AXz1BlTOQAqAurVOv1a0vxRNGRiASHejKutAVPJGBiwElMHQfGNWrU2d1y+za2xvlgjdB5VPFjPDKlyMtBelEVd+HLE80acM0WfouRdQNEJ3rnwRbYerY6iou9YZwVNOIA9I2IBrb6CZ05s8//krqCBoof7m2CsHT3CSVy2KyXXgXdcj+ACd7TSD1dKE0IsEEQcEQZ7IS7OjZT+L8UVOYICfTWGoQlc4gZCPjxrgMXELkdCwiNZCEeDjBSYp9Nkp0+0ThmRhgfg40QSeUTTdfPuMOpidvWMlF1BIR6b9+yknv2F5L3cf9zORni5yvjfdXuAnRGP4O4UhoNyjHuZ0HB9U7HP+aPKCdMeqNJ/O3rN7IZe/BCaavAC7oebGzm8qhd5taN6cbdvpfPr7CryEXeuakVYNlMKDwlIqloOkG08DEhd0OskeFlSVugofaDkzeM3IFVY8iiNcr3N3fJvB2qEZQvnIQkcdody7bEQ6OgNBxOXfvF4S7jWMt7KYwXY7dSR38wCx6bq1UoVHHt2K3RumFE7Qp/BLab2DU5bsGeY5xT/5AXsaGwT8zjL6cqMTRw=");
        setSkinTexture("ewogICJ0aW1lc3RhbXAiIDogMTU5MDQzNTgwMjg4OSwKICAicHJvZmlsZUlkIiA6ICJhMjk1ODZmYmU1ZDk0Nzk2OWZjOGQ4ZGE0NzlhNDNlZSIsCiAgInByb2ZpbGVOYW1lIiA6ICJWaWVydGVsdG9hc3RpaWUiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWNiY2M4NTRkMTExNDA4ODQ4MzBmNGVjZDQwN2ZjZDEzYTgyODU2NmMzZDNlYzcwNTVlNjQ2NTQ4MWE1MGMwZSIKICAgIH0KICB9Cn0=");
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
