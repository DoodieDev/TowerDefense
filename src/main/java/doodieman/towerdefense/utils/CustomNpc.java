package doodieman.towerdefense.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import doodieman.towerdefense.TowerDefense;
import lombok.Getter;
import lombok.Setter;
import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public abstract class CustomNpc implements Listener {
    @Getter
    private NPC npc;
    @Getter
    private Hologram hologram;
    @Getter
    private final String ID;
    @Getter
    private final Location location;

    @Getter @Setter
    private String npcName;
    @Setter
    private String skinTexture;
    @Setter
    private String skinSignature;
    @Setter
    private boolean lookClose;
    @Setter
    private boolean nameplateVisible;

    public CustomNpc(String ID, Location location) {
        this.ID = ID;
        this.location = location;

        //Default values
        this.npcName = "§7Intet navn";
        this.lookClose = false;
        this.nameplateVisible = true;

        //Default skin
        this.skinTexture = "ewogICJ0aW1lc3RhbXAiIDogMTY1MDkwNTQwNDA1NCwKICAicHJvZmlsZUlkIiA6ICI0ODI5MmJkMjI1OTc0YzUwOTZiMTZhNjEyOGFmMzY3NSIsCiAgInByb2ZpbGVOYW1lIiA6ICJLVVJPVE9ZVEIyOCIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS84OWJiYmRlNjJiYzU4Yzg1YTllZDRlZTYzMDI1MjU4MDQwOTc5YjZkY2VkMmNlNmI0YmZmOTJhZjIzMTVmNWVlIgogICAgfQogIH0KfQ==";
        this.skinSignature = "efk5L0Wr39xbwKc18cOGg4uhVLbAUwPuj8+DkNQLcOP3alxjFmI5BMXyz9aRt4WpNelit6TY/yi4GIO5vKYtVkYCkWBziHE6rm9YKsZ4p8uiYNg/F9tWsMffIJC3WCKJ+6O622WM9e6XxiY2we0VGLqDq8ZPrFAZEN44QjwZfjUCKM9RKcc+Jk/fjmn3AwA8KWTIHVNYQMWRroF8FBGVx6z4u2B7BQQuGzQ6630GgNzrCtv0uOz/4qHXDNvGOR4x3ehpnWg8haaw2v/1KO4erjAZiNsSKnuCXCfcKJbjM0kL6Q2STqPzuB7BErfdY2YdA4jLoWgBjpJGXfm/U5OPskx/QvHjTsV1RGoXjVdeujIkTigorHPbsjRdpKgUcv8+olkRcgt53dFlFDyzO+s/bPZMmRS6+/1zcSoSaAJdVzP7m+88F6m15WTP8bUc0m7jPA87UjOn8JDjDNcWrw/dV9zIHO63NZ9OclaZwYGtH6gPyyamyQZ+DFlgcxk3TyNhFXl201DyvJtJgI61DZsSruPSlm8cL4mAQRG4zd0LMzq/gZU5wdsP528DQmOdfbV774e6xl88JQ7D8pwe0TeBt1RlFldAmLv5XGaGELtmVPsY3bXdP/wm6cKrCw9T+XUHlvX/7Hn7g1eL/p7AjTKhV3+/lQLaxJ+NdCmNG5m5fGs=";

        //No hologram as default
        this.hologram = null;

        //Register listener
        TowerDefense.getInstance().getServer().getPluginManager().registerEvents(this, TowerDefense.getInstance());
    }

    public void spawnNpc() {
        this.npc = TowerDefense.getInstance().getNpcRegistry().createNPC(EntityType.PLAYER, npcName);

        //Look close
        npc.getOrAddTrait(LookClose.class).lookClose(lookClose);

        //Set the skin
        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent("npcTexture", skinSignature, skinTexture);

        //Nameplate
        npc.data().set(NPC.NAMEPLATE_VISIBLE_METADATA, nameplateVisible);

        npc.spawn(location);
    }

    public void setHologram(List<String> lines) {
        //1 = 2.25
        double offset = lines.size() * 0.25;
        Location hologramLocation = location.clone().add(0, 2+offset, 0);

        if (hologram == null)
            this.hologram = HologramsAPI.createHologram(TowerDefense.getInstance(), hologramLocation);
        else
            this.hologram.clearLines();
        this.hologram.setAllowPlaceholders(true);

        for (String line : lines)
            this.hologram.appendTextLine(StringUtil.colorize(line));
    }



    public abstract void onRightClick(NPCRightClickEvent event);
    @EventHandler
    public void onNpcRightClick(NPCRightClickEvent event) {
        if (!event.getNPC().equals(npc)) return;
        onRightClick(event);
    }

    public abstract void onLeftClick(NPCLeftClickEvent event);
    @EventHandler
    public void onNpcLeftClick(NPCLeftClickEvent event) {
        if (!event.getNPC().equals(npc)) return;
        onLeftClick(event);
    }
}
