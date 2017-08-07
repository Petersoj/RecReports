package me.petersoj.util;

import org.bukkit.Location;

// This class helps will [de]serializing locations and other utility functions.
public class LocationUtils {

    // Resembles PacketPlayOutEntityLook for [de]serialization.
    public static final Location LOOK_LOCATION_EXAMPLE = new Location(null, Integer.MIN_VALUE, 0, 0);
    // Resembles PacketPlayOutEntityRelMove or Teleport for [de]serialization.
    public static final Location MOVE_LOCATION_EXAMPLE = new Location(null, 0, 0, 0, 360f, 360f);

    /**
     * This method determines what type of movement a 'to' location is.
     *
     * @param location the location to check.
     * @return the type of movement
     */
    public static MovementType getMovementType(Location location) {
        if (location == null) {
            throw new NullPointerException("The Location cannot be null!");
        }

        if ((int) location.getX() == Integer.MIN_VALUE && location.getY() == 0 && location.getZ() == 0) {
            return MovementType.LOOK;
        } else if (location.getYaw() == 360f && location.getPitch() == 360f) {
            return MovementType.MOVE;
        } else {
            return MovementType.MOVE_AND_LOOK;
        }
    }

    public enum MovementType {
        LOOK, MOVE, MOVE_AND_LOOK
    }
}
