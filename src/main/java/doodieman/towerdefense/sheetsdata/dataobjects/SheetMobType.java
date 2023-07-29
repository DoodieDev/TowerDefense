package doodieman.towerdefense.sheetsdata.dataobjects;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Style;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Pig;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Getter
    private String rawDataField = null;

    final Map<String, String> mobDataFields = new HashMap<>();

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

        if(mobData.size() >= 13 && mobData.get(12) != null && !mobData.get(12).toString().isEmpty()) {
            this.rawDataField = (String) mobData.get(12);
            this.formatDataField();
        }
    }

    private void formatDataField() {
        for (String field : this.rawDataField.split("\\|")) {
            String[] fieldSplit = field.split(":");
            String key = fieldSplit[0];
            String value = fieldSplit[1];
            this.mobDataFields.put(key, value);
        }
    }
    public String getDataString(String key, String defaultValue) {
        return this.mobDataFields.getOrDefault(key,defaultValue);
    }
    public boolean getDataBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(this.mobDataFields.getOrDefault(key,""+defaultValue));
    }
    public double getDataDouble(String key, double defaultValue) {
        return Double.parseDouble(this.mobDataFields.getOrDefault(key,""+defaultValue));
    }
    public int getDataInteger(String key, int defaultValue) {
        return Integer.parseInt(this.mobDataFields.getOrDefault(key,""+defaultValue));
    }
    public Color getDataColor(String key, Color defaultValue) {
        String rawString = this.getDataString(key,defaultValue.getRed()+"."+defaultValue.getGreen()+"."+defaultValue.getBlue());
        String[] split = rawString.split("\\.");
        int r = Integer.parseInt(split[0]);
        int g = Integer.parseInt(split[1]);
        int b = Integer.parseInt(split[2]);
        return Color.fromRGB(r,g,b);
    }


    public void initializeEntity(Entity entity) {

        ItemStack hand = new ItemStack(this.getHand() == null ? Material.AIR : this.getHand());
        ItemStack helmet = new ItemStack(this.getHelmet() == null ? Material.AIR : this.getHelmet());
        ItemStack chestplate = new ItemStack(this.getChestplate() == null ? Material.AIR : this.getChestplate());
        ItemStack leggings = new ItemStack(this.getLeggings() == null ? Material.AIR : this.getLeggings());
        ItemStack boots = new ItemStack(this.getBoots() == null ? Material.AIR : this.getBoots());

        if (entity.isInsideVehicle())
            entity.getVehicle().remove();
        if (entity.getPassenger() != null)
            entity.getPassenger().remove();

        switch (entityType) {

            case ZOMBIE:
                Zombie zombie = (Zombie) entity;
                zombie.setBaby(this.getDataBoolean("baby",false));
                zombie.setVillager(this.getDataBoolean("villager",false));

                zombie.getEquipment().setItemInHand(hand);
                zombie.getEquipment().setHelmet(helmet);
                zombie.getEquipment().setChestplate(chestplate);
                zombie.getEquipment().setLeggings(leggings);
                zombie.getEquipment().setBoots(boots);
                break;

            case PIG_ZOMBIE:
                PigZombie pigzombie = (PigZombie) entity;
                pigzombie.setBaby(this.getDataBoolean("baby",false));

                pigzombie.getEquipment().setItemInHand(hand);
                pigzombie.getEquipment().setHelmet(helmet);
                pigzombie.getEquipment().setChestplate(chestplate);
                pigzombie.getEquipment().setLeggings(leggings);
                pigzombie.getEquipment().setBoots(boots);
                break;

            case SKELETON:
                Skeleton skeleton = (Skeleton) entity;

                skeleton.getEquipment().setItemInHand(hand);
                skeleton.getEquipment().setHelmet(helmet);
                skeleton.getEquipment().setChestplate(chestplate);
                skeleton.getEquipment().setLeggings(leggings);
                skeleton.getEquipment().setBoots(boots);
                break;

            case ENDERMAN:
                Enderman enderman = (Enderman) entity;
                enderman.getEquipment().setItemInHand(hand);
                break;


            case SHEEP:
                Sheep sheep = (Sheep) entity;

                DyeColor dyeColor = DyeColor.valueOf(this.getDataString("color","WHITE"));
                sheep.setColor(dyeColor);

                if (this.getDataBoolean("baby",false))
                    sheep.setBaby();
                else
                    sheep.setAdult();
                sheep.setSheared(this.getDataBoolean("sheared",false));
                break;

            case COW:
                Cow cow = (Cow) entity;
                if (this.getDataBoolean("baby",false))
                    cow.setBaby();
                else
                    cow.setAdult();
                break;
            case CHICKEN:
                Chicken chicken = (Chicken) entity;
                if (this.getDataBoolean("baby",false))
                    chicken.setBaby();
                else
                    chicken.setAdult();
                break;

            case PIG:
                Pig pig = (Pig) entity;
                if (this.getDataBoolean("baby",false))
                    pig.setBaby();
                else
                    pig.setAdult();
                break;

            case SLIME:
                Slime slime = (Slime) entity;
                slime.setSize(this.getDataInteger("slimesize",0));
                break;

            case MAGMA_CUBE:
                MagmaCube magmaCube = (MagmaCube) entity;
                magmaCube.setSize(this.getDataInteger("slimesize",0));
                break;

            case HORSE:
                Horse horse = (Horse) entity;

                horse.setVariant(Variant.valueOf(this.getDataString("horsevariant","HORSE")));
                horse.setStyle(Style.valueOf(this.getDataString("horsestyle","NONE")));
                horse.getInventory().setArmor(hand);

                horse.setCarryingChest(this.getDataBoolean("chest",false));
                if (this.getDataBoolean("saddle",false))
                    horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));

                if (this.getDataBoolean("baby",false))
                    horse.setBaby();
                else
                    horse.setAdult();
                break;
        }
    }

}
