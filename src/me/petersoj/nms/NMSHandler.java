package me.petersoj.nms;

import me.petersoj.RecReportsPlugin;
import me.petersoj.listeners.events.SignUpdateEvent;
import me.petersoj.report.ReportPlayer;
import org.bukkit.entity.Player;

public abstract class NMSHandler {

    private RecReportsPlugin plugin;

    public NMSHandler(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract RecordedPlayer getNewRecordedPlayer(RecReportsPlugin plugin, ReportPlayer reportPlayer, Player sendPacketsPlayer);

    public abstract void openSignInterface(Player player, String[] initialText, String[] finalText, int delayToFinal);

    public abstract void addSignUpdateListener(Player player, SignUpdateEvent signUpdateEvent);

    public RecReportsPlugin getPlugin() {
        return plugin;
    }
}
