package me.petersoj.util.adapters;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

/**
 * This class exists so that org.bukkit.World doesn't get serialized within the Location object.
 */
public class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("world", location.getWorld().getUID().toString());
        object.addProperty("x", location.getX());
        object.addProperty("y", location.getY());
        object.addProperty("z", location.getZ());
        // Only add if yaw or pitch are not 0 (to save space)
        if (location.getYaw() != 0f) {
            object.addProperty("yaw", location.getYaw());
        } else if (location.getPitch() != 0f) {
            object.addProperty("pitch", location.getPitch());
        }
        return object;
    }

    @Override
    public Location deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(element instanceof JsonObject)) {
            throw new JsonParseException("The element given is not a JsonObject!");
        }

        JsonObject locationObj = (JsonObject) element;

        World world = Bukkit.getWorld(locationObj.get("world").getAsString());
        double x = locationObj.get("x").getAsDouble();
        double y = locationObj.get("y").getAsDouble();
        double z = locationObj.get("z").getAsDouble();

        JsonElement yawElement = locationObj.get("yaw");
        JsonElement pitchElement = locationObj.get("pitch");
        float yaw = yawElement == null ? 0f : yawElement.getAsFloat();
        float pitch = pitchElement == null ? 0f : pitchElement.getAsFloat();

        return new Location(world, x, y, z, yaw, pitch);
    }
}
