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

    public abstract void moveTo(Location location);

    public abstract void teleport(Location location);

    public abstract void setSneaking(boolean sneak);

    public abstract void swingArm();

    public abstract void setOnFire(boolean onFire);


    public abstract void setMainHandItem(ItemStack item);

    public abstract void setOffHandItemv1_12(ItemStack item);

    public abstract void setHelmet(ItemStack item);

    public abstract void setChestplate(ItemStack item);

    public abstract void setLeggings(ItemStack item);

    public abstract void setBoots(ItemStack item);

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