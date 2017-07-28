package me.petersoj.listeners.events;

import org.bukkit.Location;

public interface SignUpdateEvent {

    void onSignUpdate(Location location, String[] lines);

}
