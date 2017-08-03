package me.petersoj.nms;

import me.petersoj.RecReportsPlugin;
import me.petersoj.listeners.events.SignUpdateEvent;
import me.petersoj.report.ReportPlayer;
import org.bukkit.entity.Player;

public interface NMSHandler {

    RecordedPlayer getNewRecordedPlayer(RecReportsPlugin plugin, ReportPlayer reportPlayer, Player sendPacketsPlayer);

    void openSignInterface(Player player, String initialText, String finalText, int delayToFinal);

    void addSignUpdateListener(Player player, SignUpdateEvent signUpdateEvent);

}
