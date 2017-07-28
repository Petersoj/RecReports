package me.petersoj.nms;

import me.petersoj.listeners.events.SignUpdateEvent;
import org.bukkit.entity.Player;

public interface NMSHandler {

    void addSignUpdateListener(Player player, SignUpdateEvent signUpdateEvent);

}
