package me.petersoj.nms;

import me.petersoj.report.ReportPlayer;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public abstract class RecordedPlayer {

    private int recordedPlayerID; // The ID used for referencing this player in a recording
    private ReportPlayer reportPlayer;

    public RecordedPlayer(int recordedPlayerID, ReportPlayer reportPlayer) {
        this.recordedPlayerID = recordedPlayerID;
        this.reportPlayer = reportPlayer;
    }

    public abstract void spawn();

    public abstract void despawn();

    public abstract void look(float yaw, float pitch);

    public abstract void moveTo(Location location);

    public abstract void teleport(Location location);

    public abstract void setSneaking(boolean sneak);

    public abstract void swingArm();

    public abstract void setOnFire(boolean onFire);

    public abstract void chatMessage(String message);


    public abstract void setMainHandItem(ItemStack item);

    public abstract void setOffHandItemv1_12(ItemStack item);

    public abstract void setHelmet(ItemStack item);

    public abstract void setChestplate(ItemStack item);

    public abstract void setLeggings(ItemStack item);

    public abstract void setBoots(ItemStack item);

    public int getRecordedPlayerID() {
        return recordedPlayerID;
    }

    public ReportPlayer getReportPlayer() {
        return reportPlayer;
    }
}