package me.petersoj.nms;

import org.bukkit.Location;

import java.util.UUID;

public abstract class RecordedPlayer {

    private UUID uuid;
    private String name;
    private String skinProfile;

    public RecordedPlayer(UUID uuid, String name, String skinProfile) {
        this.uuid = uuid;
        this.name = name;
        this.skinProfile = skinProfile;
    }

    public abstract void spawn();

    public abstract void look(float yaw, float pitch);

    public abstract void moveTo(Location location);

    public abstract void teleport(Location location);

    public abstract void setSneaking(boolean sneak);

    public abstract void swingArm();

    public abstract void setOnFire(boolean onFire);

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getSkinProfile() {
        return skinProfile;
    }
}
