package me.petersoj.nms;

import me.petersoj.listeners.events.SignUpdateEvent;
import org.bukkit.entity.Player;

public interface NMSHandler {

    void openSignInterface(Player player, String initialText, String finalText, int delayToFinal);

    void addSignUpdateListener(Player player, SignUpdateEvent signUpdateEvent);

}
