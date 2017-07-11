package me.petersoj.nms;

import org.bukkit.Location;

import java.util.UUID;

public abstract class RecordedPlayer {

    private String name;
    private UUID uuid;
    private String skinProfile;

    public RecordedPlayer(String name, UUID uuid, String skinProfile) {
        this.name = name;
        this.uuid = uuid;
        this.skinProfile = skinProfile;

    }

    public abstract void spawn();

    public abstract void moveTo(Location location);

    public abstract void teleport(Location location);

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getSkinProfile() {
        return skinProfile;
    }
}
