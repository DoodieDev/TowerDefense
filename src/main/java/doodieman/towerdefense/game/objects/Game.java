package doodieman.towerdefense.game.objects;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.blocks.BaseBlock;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.regions.CuboidRegion;
import doodieman.towerdefense.TowerDefense;
import doodieman.towerdefense.game.interactive.GameInteractive;
import doodieman.towerdefense.game.values.Difficulty;
import doodieman.towerdefense.game.values.MobType;
import doodieman.towerdefense.game.values.Round;
import doodieman.towerdefense.mapgrid.MapGridHandler;
import doodieman.towerdefense.mapgrid.objects.GridLocation;
import doodieman.towerdefense.maps.MapUtil;
import doodieman.towerdefense.maps.objects.Map;
import doodieman.towerdefense.utils.StringUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public class Game {

    @Getter
    private final OfflinePlayer player;
    @Getter
    private final Map map;
    @Getter
    private final GridLocation gridLocation;
    @Getter
    private final GameInteractive gameInteractive;
    @Getter
    private final World world;
    @Getter
    private final Location zeroLocation;
    @Getter
    private final List<Location> mobPath;
    BukkitTask mobPathLoop;

    //Normal game values being used globally
    @Getter
    private double health;
    @Getter
    private double gold;
    @Getter
    private final Difficulty difficulty;
    @Getter //List of active mobs on the path
    private final List<GameMob> aliveMobs;
    @Getter
    private final List<GameTurret> turrets;
    @Getter //Check if the round is active
    private boolean roundActive;
    @Getter //Current round number
    private int currentRound;



    private Hologram startHologram;
    private TextLine roundTextLine;
    private TextLine goldTextLine;
    private TextLine healthTextLine;


    public Game(OfflinePlayer player, Map map, Difficulty difficulty) {
        this.player = player;
        this.map = map;
        this.gameInteractive = new GameInteractive(player, this);
        this.mobPath = new ArrayList<>();
        this.world = MapUtil.getInstance().getGameWorld();
        this.gridLocation = MapGridHandler.getInstance().generateLocation();
        this.gridLocation.register();
        this.zeroLocation = this.gridLocation.getLocation(this.world);

        this.difficulty = difficulty;
        this.health = difficulty.getHealth();
        this.gold = difficulty.getStartingGold();
        this.aliveMobs = new ArrayList<>();
        this.turrets = new ArrayList<>();
        this.roundActive = false;
        this.currentRound = 1;
        this.startHologram = null;
    }

    //Prepares the game, pasting schematic, etc
    public void prepare() {
        //Paste schematic async
        TowerDefense.runAsync(new BukkitRunnable() {
            @Override
            public void run() {
                map.pasteSchematic(zeroLocation);
            }
        });
        //Load the mob path
        for (Location location : map.getPath())
            this.mobPath.add(getRealLocation(location));
        //Create the hologram at mob starting point
        this.updateStartHologram();
    }

    //Starts the game. Teleports player, etc.
    public void start() {
        this.player.getPlayer().teleport(mobPath.get(0));
        this.gameInteractive.register();
        this.startLoop();
    }

    //Stops the game. Removing schematic, teleport player to spawn, etc.
    public void stop(boolean removeSchematic) {
        this.roundActive = false;
        this.mobPathLoop.cancel();
        this.gridLocation.unregister();
        this.gameInteractive.unregister();
        if (this.startHologram != null)
            this.startHologram.delete();

        //Remove schematic
        if (removeSchematic) {
            TowerDefense.runAsync(new BukkitRunnable() {
                @Override
                public void run() {
                    com.sk89q.worldedit.world.World editWorld = new BukkitWorld(world);
                    Location corner1 = getRealLocation(map.getCorner1());
                    Location corner2 = getRealLocation(map.getCorner2());
                    Vector pos1 = new Vector(corner1.getX(), corner1.getY(), corner1.getZ());
                    Vector pos2 = new Vector(corner2.getX(), corner2.getY(), corner2.getZ());
                    CuboidRegion region = new CuboidRegion(editWorld, pos1, pos2);
                    EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(editWorld, -1);
                    editSession.setBlocks(region, new BaseBlock(0));
                    editSession.flushQueue();
                }
            });
        }
    }

    //Starts a wave of monsters
    private void startLoop() {

        List<GameMob> mobsToRemove = new ArrayList<>();

        this.mobPathLoop = new BukkitRunnable() {

            int tick = 0;
            double displayPathOffset = 0;

            @Override
            public void run() {

                //While round is inactive - Display path
                if (!roundActive) {
                    showPathParticles(displayPathOffset,15);
                    displayPathOffset += 0.15;
                }

                //While round is active
                if (roundActive) {

                    //Move all the mobs on path
                    for (GameMob mob : aliveMobs) {
                        mob.move();
                        if (mob.isInGoal())
                            mobsToRemove.add(mob);
                    }

                    //Remove mobs in goal
                    for (GameMob mob : mobsToRemove) {
                        damage(mob.getMobType().getDamage());
                        gameInteractive.getGameAnimations().mobFinished(mob.getEntity().getLocation(), mob.getMobType());
                        mob.kill();
                    }
                    mobsToRemove.clear();

                    //Check if round is over
                    if (aliveMobs.size() <= 0)
                        finishRound();
                }

                tick++;
            }
        }.runTaskTimer(TowerDefense.getInstance(), 0L, 1L);
    }

    //Spawns a mob that automatically will go on the path
    public void spawnMob(MobType mobType) {
        GameMob mob = new GameMob(this, mobType);
        mob.spawn();
        aliveMobs.add(mob);
        gameInteractive.getGameAnimations().mobSpawned(mob.getEntity().getLocation());
    }

    //Starts the round
    public void startRound() {
        this.roundActive = true;
        this.gameInteractive.getGameAnimations().newRoundStarted();

        Round round = Round.getRound(currentRound);
        this.updateStartHologram();

        //Spawn the mobs slowly
        int i = 0;
        for (MobType mobType : round.getMobs()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (roundActive)
                        spawnMob(mobType);
                }
            }.runTaskLater(TowerDefense.getInstance(), i * round.getSpawnDelay());
            i++;
        }

        //The timer when the round is active
        new BukkitRunnable() {

            long roundTick = 0L;

            @Override
            public void run() {
                //Round not active anymore. Cancel the timer
                if (!roundActive) {
                    this.cancel();
                    return;
                }

                //Update all turrets
                for (GameTurret turret : getTurrets())
                    turret.update(roundTick);

                roundTick++;
            }
        }.runTaskTimer(TowerDefense.getInstance(),0L,1L);
    }

    //Called when the round is finished
    public void finishRound() {
        this.roundActive = false;
        this.wipeMobs();
        gameInteractive.updateRoundItemSlot();
        currentRound++;
    }

    //Removes all the active mobs
    public void wipeMobs() {
        for (GameMob mob : aliveMobs)
            mob.kill();
        aliveMobs.clear();
    }

    //When a mobs damages the player
    public void damage(double amount) {
        this.health -= amount;
        if (this.health < 0)
            this.health = 0;
        this.updateStartHologram();
    }

    public void setGold(double amount) {
        this.gold = amount;
        this.updateStartHologram();
    }
    //Get real location from location in config.
    //Example: The map is built and saved with different coordinates in a different world.
    //When its pasted in the real game world. The location varies.
    public Location getRealLocation(Location location) {
        Location mapZero = map.getCorner1();
        double xDiffer = -(mapZero.getX() - location.getX());
        double yDiffer = -(mapZero.getY() - location.getY());
        double zDiffer = -(mapZero.getZ() - location.getZ());
        Location newLocation = zeroLocation.clone().add(xDiffer,yDiffer,zDiffer);
        newLocation.setYaw(location.getYaw());
        newLocation.setPitch(location.getPitch());
        return newLocation;
    }

    private void showPathParticles(double offset, double distance) {
        double pathLength = map.getPathLength();
        int pathPoints = (int) Math.floor(pathLength / distance);
        double lengthPerPoint = pathLength / pathPoints;

        offset = offset % lengthPerPoint;

        for (int i = 0; i < pathPoints; i++) {
            double placement = i * lengthPerPoint + offset;
            Location loc = getRealLocation(map.getPathLocationAt(placement));
            loc.add(0, 0.5, 0);

            Color color = Color.fromRGB((int) (placement / pathLength * 255) , (int) ((1-(placement / pathLength)) * 255), 0);
            PacketPlayOutWorldParticles particle = new PacketPlayOutWorldParticles(
                EnumParticle.REDSTONE, true,
                (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(),
                (float)color.getRed()/255, (float)color.getGreen()/255, (float)color.getBlue()/255, (float) 1, 0
            );

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(particle);
        }

    }

    //Create or update the hologram at the start
    public void updateStartHologram() {
        Location location = this.mobPath.get(0).clone().add(0,3,0);
        //Create the hologram
        if (this.startHologram == null) {
            this.startHologram = HologramsAPI.createHologram(TowerDefense.getInstance(),location);
            startHologram.appendTextLine("§a§nStartlinje");
            startHologram.appendTextLine("");
            startHologram.appendTextLine("§7Map: §a"+map.getMapName());
            startHologram.appendTextLine("§7Sværhedsgrad: "+difficulty.getTextColor()+difficulty.getName());
            startHologram.appendTextLine("");
            this.roundTextLine = startHologram.appendTextLine("§7Runde: §f"+currentRound+"§7/§f"+difficulty.getRounds());
            this.healthTextLine = startHologram.appendTextLine("§7Liv: §f"+StringUtil.formatNum(health)+"§4❤");
            startHologram.appendTextLine("");
            this.goldTextLine = startHologram.appendTextLine("§7Guld: §e"+ StringUtil.formatNum(gold) +"g");
        }

        this.roundTextLine.setText("§7Runde: §f"+currentRound+"§7/§f"+difficulty.getRounds());
        this.healthTextLine.setText("§7Liv: §f"+StringUtil.formatNum(health)+"§4❤");
        this.goldTextLine.setText("§7Guld: §e"+ StringUtil.formatNum(gold) +"g");
    }
}
