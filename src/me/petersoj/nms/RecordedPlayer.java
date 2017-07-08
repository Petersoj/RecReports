package me.petersoj.nms;

import org.bukkit.Location;

import java.util.UUID;

public interface RecordedPlayer {

    void spawn(UUID uuid);

    void moveTo(Location location);



}
