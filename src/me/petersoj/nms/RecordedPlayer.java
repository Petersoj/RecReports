package me.petersoj.nms;

import me.petersoj.RecReportsPlugin;
import me.petersoj.report.ReportPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class RecordedPlayer {

    private RecReportsPlugin plugin;

    private ReportPlayer reportPlayer;
    private Player sendPacketsPlayer;

    public RecordedPlayer(RecReportsPlugin plugin, ReportPlayer reportPlayer, Player sendPacketsPlayer) {
        this.plugin = plugin;
        this.reportPlayer = reportPlayer;
        this.sendPacketsPlayer = sendPacketsPlayer;
    }

    public abstract void spawn(Location location);

    public abstract void despawn();

    public abstract void look(float yaw, float pitch);

    /**
     * Deprecated because of the loss of accuracy and I'm doing something wrong here, because
     * over time, the player will start to get off course and gradually lose it's intended location.
     * IDK, what I'm doing wrong...
     *
     * @deprecated Issues with accuracy.
     */
    public abstract void moveTo(Location location);

    public abstract void teleport(Location location);

    public abstract void changeWorld(Location newWorld);

    public abstract void setSneaking(boolean sneak);

    public abstract void setOnFire(boolean onFire);

    public abstract void swingArm();

    public abstract void doDamageAnimation();

    public abstract void setEquipment(int recordedSlotNumber, ItemStack item);

    public RecReportsPlugin getPlugin() {
        return plugin;
    }

    public ReportPlayer getReportPlayer() {
        return reportPlayer;
    }

    public Player getSendPacketsPlayer() {
        return sendPacketsPlayer;
    }
}