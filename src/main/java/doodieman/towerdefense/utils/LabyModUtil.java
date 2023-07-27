package doodieman.towerdefense.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import doodieman.towerdefense.TowerDefense;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.nio.charset.Charset;
import java.util.List;
import java.util.UUID;

public class LabyModUtil {

    public static void setSubtitle(List<Player> receivers, Player target, String value, double size) {
        for (Player receiver : receivers) {
            setSubtitle(receiver,target,value,size);
        }
    }

    public static void setSubtitle(Player receiver, Player target, String value, double size) {
        JsonArray array = new JsonArray();
        JsonObject subtitle = new JsonObject();
        subtitle.addProperty( "uuid", target.getUniqueId().toString() );
        subtitle.addProperty( "size", size );
        if(value != null)
            subtitle.addProperty( "value", value );
        array.add(subtitle);
        sendLabyModMessage( receiver, "account_subtitle", array );
    }

    public void sendServerBanner(Player player, String imageUrl) {
        JsonObject object = new JsonObject();
        object.addProperty("url", imageUrl); // Url of the image
        sendLabyModMessage(player, "server_banner", object);
    }

    public static void startCinematic(Player player, List<Location> locations, long duration, List<Integer> tilts) {
        JsonObject cinematic = new JsonObject();
        JsonArray points = new JsonArray();
        int num = 0;
        for (Location loc : locations) {
            JsonObject point = new JsonObject();
            point.addProperty( "x", loc.getX() );
            point.addProperty( "y", loc.getY() );
            point.addProperty( "z", loc.getZ() );
            point.addProperty( "yaw", loc.getYaw() );
            point.addProperty( "pitch", loc.getPitch() );
            point.addProperty( "tilt", tilts.get(num));
            points.add( point );
            num++;
        }
        cinematic.add( "points", points);
        //cinematic.addProperty("clear_chat", false);
        cinematic.addProperty( "duration", duration );
        player.teleport(locations.get(0));
        sendLabyModMessage(player, "cinematic", cinematic);
    }

    public static void sendCineScope( Player player, int coveragePercent, long duration, long delay) {
        JsonObject object = new JsonObject();
        object.addProperty( "coverage", coveragePercent);
        object.addProperty( "duration", duration);
        new BukkitRunnable(){
            public void run() {
                sendLabyModMessage(player, "cinescopes", object);
            }
        }.runTaskLater(TowerDefense.getInstance(), (delay/1000)*20);
    }


    public static void sendLabyModMessage(Player player, String key, JsonElement messageContent) {
        byte[] bytes = getBytesToSend(key, messageContent.toString());

        PacketDataSerializer pds = new PacketDataSerializer(Unpooled.wrappedBuffer(bytes));
        PacketPlayOutCustomPayload payloadPacket = new PacketPlayOutCustomPayload("labymod3:main", pds);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(payloadPacket);
    }
    public static void sendCurrentPlayingGamemode( Player player, boolean visible, String gamemodeName ) {
        JsonObject object = new JsonObject();
        object.addProperty( "show_gamemode", visible ); // Gamemode visible for everyone
        object.addProperty( "gamemode_name", gamemodeName ); // Name of the current playing gamemode

        // Send to LabyMod using the API
        sendLabyModMessage( player, "server_gamemode", object );
    }

    private static byte[] getBytesToSend(String messageKey, String messageContents) {
        // Getting an empty buffer
        ByteBuf byteBuf = Unpooled.buffer();

        // Writing the message-key to the buffer
        writeString(byteBuf, messageKey);

        // Writing the contents to the buffer
        writeString(byteBuf, messageContents);

        // Copying the buffer's bytes to the byte array
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);

        // Release the buffer
        byteBuf.release();

        // Returning the byte array
        return bytes;
    }

    private static void writeVarIntToBuffer(ByteBuf buf, int input) {
        while ((input & -128) != 0) {
            buf.writeByte(input & 127 | 128);
            input >>>= 7;
        }

        buf.writeByte(input);
    }

    private static void writeString(ByteBuf buf, String string) {
        byte[] abyte = string.getBytes(Charset.forName("UTF-8"));

        if (abyte.length > Short.MAX_VALUE) {
            throw new EncoderException("String too big (was " + string.length() + " bytes encoded, max " + Short.MAX_VALUE + ")");
        } else {
            writeVarIntToBuffer(buf, abyte.length);
            buf.writeBytes(abyte);
        }
    }

    private static int readVarIntFromBuffer(ByteBuf buf) {
        int i = 0;
        int j = 0;

        byte b0;
        do {
            b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;
            if (j > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b0 & 128) == 128);

        return i;
    }

    public static String readString(ByteBuf buf, int maxLength) {
        int i = readVarIntFromBuffer(buf);

        if (i > maxLength * 4) {
            throw new DecoderException("The received encoded string buffer length is longer than maximum allowed (" + i + " > " + maxLength * 4 + ")");
        } else if (i < 0) {
            throw new DecoderException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            byte[] bytes = new byte[i];
            buf.readBytes(bytes);

            String s = new String(bytes, Charset.forName("UTF-8"));
            if (s.length() > maxLength) {
                throw new DecoderException("The received string length is longer than maximum allowed (" + i + " > " + maxLength + ")");
            } else {
                return s;
            }
        }
    }


    /**
     * Add a secret key to the json object
     * @param jsonObject json object
     * @param hasKey has key name
     * @param key key name
     * @param secret the secret key
     * @return json object
     */
    private JsonObject addSecret(JsonObject jsonObject, String hasKey, String key, UUID secret, String domain) {
        jsonObject.addProperty( hasKey, true );
        jsonObject.addProperty( key, secret.toString() + ":" + domain );
        return jsonObject;
    }

}
