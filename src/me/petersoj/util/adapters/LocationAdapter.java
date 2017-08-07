package me.petersoj.util.adapters;

import com.google.gson.*;
import me.petersoj.util.LocationUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.UUID;

/**
 * This class exists so that org.bukkit.World doesn't get serialized within the Location object.
 */
public class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

    private DecimalFormat decimalFormat = new DecimalFormat("###########.###");
    private boolean minimize = true;
    private boolean includeWorlds = true;
    private boolean checkMovementType = false;

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        if (location == null) {
            throw new NullPointerException("Location cannot be null!");
        }

        JsonObject object = new JsonObject();

        if (includeWorlds) {
            object.addProperty("world", location.getWorld().getUID().toString());
        }

        boolean addLocation;
        boolean addDirection;

        if (checkMovementType) {
            LocationUtils.MovementType movementType = LocationUtils.getMovementType(location);
            addLocation = (movementType == LocationUtils.MovementType.MOVE || movementType == LocationUtils.MovementType.MOVE_AND_LOOK);
            addDirection = (movementType == LocationUtils.MovementType.LOOK || movementType == LocationUtils.MovementType.MOVE_AND_LOOK);
        } else {
            addLocation = true;
            addDirection = true;
        }

        if (addLocation) {
            this.addNumberProperty(object, "x", location.getX());
            this.addNumberProperty(object, "y", location.getY());
            this.addNumberProperty(object, "z", location.getZ());
        }
        if (addDirection) {
            if (checkMovementType) {
                this.addNumberProperty(object, "yaw", location.getYaw());
                this.addNumberProperty(object, "pitch", location.getPitch());
            } else if (addLocation) { // Make sure to only do below if X, Y, Z are going to be serialized as well.
                // Only add if yaw or pitch are not 0 (to save space) and we don't need to check movement type
                if (location.getYaw() != 0f) {
                    this.addNumberProperty(object, "yaw", location.getYaw());
                }
                if (location.getPitch() != 0f) {
                    this.addNumberProperty(object, "pitch", location.getPitch());
                }
            }
        }

        return object;
    }

    private void addNumberProperty(JsonObject object, String key, Number value) {
        if (minimize) {
            object.addProperty(key, decimalFormat.format(value));
        } else {
            object.addProperty(key, value);
        }
    }

    @Override
    public Location deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(element instanceof JsonObject)) {
            throw new JsonParseException("The element given is not a JsonObject!");
        }

        JsonObject locationObj = (JsonObject) element;

        JsonElement worldElement = locationObj.get("world");
        JsonElement xElement = locationObj.get("x");
        JsonElement yElement = locationObj.get("y");
        JsonElement zElement = locationObj.get("z");
        JsonElement yawElement = locationObj.get("yaw");
        JsonElement pitchElement = locationObj.get("pitch");

        World world = worldElement == null ? null : Bukkit.getWorld(UUID.fromString(worldElement.getAsString()));
        double x = xElement == null ? (checkMovementType ? LocationUtils.LOOK_LOCATION_EXAMPLE.getX() : 0) : xElement.getAsDouble();
        double y = yElement == null ? (checkMovementType ? LocationUtils.LOOK_LOCATION_EXAMPLE.getY() : 0) : yElement.getAsDouble();
        double z = zElement == null ? (checkMovementType ? LocationUtils.LOOK_LOCATION_EXAMPLE.getZ() : 0) : zElement.getAsDouble();
        float yaw = yawElement == null ? (checkMovementType ? LocationUtils.MOVE_LOCATION_EXAMPLE.getYaw() : 0f) : yawElement.getAsFloat();
        float pitch = pitchElement == null ? (checkMovementType ? LocationUtils.MOVE_LOCATION_EXAMPLE.getYaw() : 0f) : pitchElement.getAsFloat();

        return new Location(world, x, y, z, yaw, pitch);
    }

    // synchronized just in case multiple threads need to change this when using Gson
    public synchronized void setMinimizeLocation(boolean minimize) {
        this.minimize = minimize;
    }

    // synchronized just in case multiple threads need to change this when using Gson
    public synchronized void setIncludeWorlds(boolean includeWorlds) {
        this.includeWorlds = includeWorlds;
    }

    // synchronized just in case multiple threads need to change this when using Gson
    public synchronized void setCheckMovementType(boolean check) {
        this.checkMovementType = check;
    }
}
