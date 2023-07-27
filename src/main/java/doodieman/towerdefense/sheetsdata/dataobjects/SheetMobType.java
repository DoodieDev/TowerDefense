package doodieman.towerdefense.sheetsdata.dataobjects;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class SheetMobType {

    @Getter
    private final String name;
    @Getter
    private final double health;
    @Getter
    private final double speed;
    @Getter
    private final double damage;
    @Getter
    private final double gold;
    @Getter
    private final Material hand;
    @Getter
    private final Material helmet;
    @Getter
    private final Material chestplate;
    @Getter
    private final Material leggings;
    @Getter
    private final Material boots;
    @Getter
    private final EntityType entityType;
    @Getter
    private final double hologramOffset;

    public SheetMobType(List<Object> mobData) throws Exception {
        this.name = (String) mobData.get(0);
        this.health = (double) mobData.get(1);
        this.speed = ((double) mobData.get(2)) / 20;
        this.damage = (double) mobData.get(3);
        this.gold = Math.round((double) mobData.get(4));
        this.hand = Material.getMaterial((String) mobData.get(5));
        this.helmet = Material.getMaterial((String) mobData.get(6));
        this.chestplate = Material.getMaterial((String) mobData.get(7));
        this.leggings = Material.getMaterial((String) mobData.get(8));
        this.boots = Material.getMaterial((String) mobData.get(9));
        this.entityType = EntityType.valueOf((String) mobData.get(10));
        this.hologramOffset = (double) mobData.get(11);
    }

    public void initializeEntity(Entity entity) {

        PacketPlayOutEntityEquipment packetTool = new PacketPlayOutEntityEquipment(entity.getEntityId(), 0, CraftItemStack.asNMSCopy(new ItemStack(hand == null ? Material.AIR : hand)));
        PacketPlayOutEntityEquipment packetHelmet = new PacketPlayOutEntityEquipment(entity.getEntityId(), 4, CraftItemStack.asNMSCopy(new ItemStack(helmet == null ? Material.AIR : helmet)));
        PacketPlayOutEntityEquipment packetChestplate = new PacketPlayOutEntityEquipment(entity.getEntityId(), 3, CraftItemStack.asNMSCopy(new ItemStack(chestplate == null ? Material.AIR : chestplate)));
        PacketPlayOutEntityEquipment packetLeggings = new PacketPlayOutEntityEquipment(entity.getEntityId(), 2, CraftItemStack.asNMSCopy(new ItemStack(leggings == null ? Material.AIR : leggings)));
        PacketPlayOutEntityEquipment packetBoots = new PacketPlayOutEntityEquipment(entity.getEntityId(), 1, CraftItemStack.asNMSCopy(new ItemStack(boots == null ? Material.AIR : boots)));

        for (Player player : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetTool);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetHelmet);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetChestplate);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetLeggings);
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packetBoots);
        }

        if (entity.isInsideVehicle())
            entity.getVehicle().remove();
        if (entity.getPassenger() != null)
            entity.getPassenger().remove();

        switch (entityType) {

            case ZOMBIE:
                Zombie zombie = (Zombie) entity;
                zombie.setBaby(false);
                zombie.setVillager(false);
                break;

            case PIG_ZOMBIE:
                PigZombie pigzombie = (PigZombie) entity;
                pigzombie.setBaby(false);
                pigzombie.setVillager(false);
                break;

        }
    }

}
