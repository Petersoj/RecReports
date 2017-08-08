package me.petersoj.record;

import me.petersoj.report.Report;
import me.petersoj.report.ReportPlayer;
import me.petersoj.util.LocationUtils;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a frame in a recording.
 */
public class Frame {

    public static int MAX_FRAME_COUNT; // Maximum frame count aka recording time limit.

    private HashMap<ReportPlayer, Location> spawnedPlayers;
    private String reportedPlayerQuitMessage;
    private ArrayList<Integer> despawnedPlayerIDs;
    private ArrayList<Report> reportsInFrame;
    private HashMap<ReportPlayer, Location> playerLocations;
    private Location reportedPlayerWorldChange;
    private HashMap<ReportPlayer, Boolean> playersSneaking;
    private HashMap<ReportPlayer, Boolean> playersOnFire;
    private ArrayList<ReportPlayer> swingingArms;
    private ArrayList<ReportPlayer> damageAnimations;
    private HashMap<ReportPlayer, HashMap<Integer, ItemStack>> equipmentChanges;

    Frame() {
        this.spawnedPlayers = new HashMap<>();
        this.despawnedPlayerIDs = new ArrayList<>();
        this.reportsInFrame = new ArrayList<>();
        this.playerLocations = new HashMap<>();
        this.playersSneaking = new HashMap<>();
        this.playersOnFire = new HashMap<>();
        this.swingingArms = new ArrayList<>();
        this.damageAnimations = new ArrayList<>();
        this.equipmentChanges = new HashMap<>();
    }

    /**
     * This method is used if the frame object be used for multiple frames
     * rather than being re-instantiated for each frame in a recording.
     */
    public void resetFrame() {
        this.spawnedPlayers.clear();
        this.reportedPlayerQuitMessage = null;
        this.despawnedPlayerIDs.clear();
        this.reportsInFrame.clear();
        this.playerLocations.clear();
        this.reportedPlayerWorldChange = null;
        this.playersSneaking.clear();
        this.playersOnFire.clear();
        this.swingingArms.clear();
        this.damageAnimations.clear();
        this.equipmentChanges.clear();
    }

    /*

    The below methods are for logging player actions which will be later serialized.

     */

    public void logSpawnedPlayer(ReportPlayer spawnedPlayer, Location spawnLocation) {
        this.spawnedPlayers.put(spawnedPlayer, spawnLocation);
    }

    public void logReportedPlayerQuitMessage(String message) {
        this.reportedPlayerQuitMessage = message;
    }

    public void logDespawnedPlayer(int despawnedPlayerID) {
        this.despawnedPlayerIDs.add(despawnedPlayerID);
    }

    public void logReport(Report report) {
        this.reportsInFrame.add(report);
    }

    public void logLook(ReportPlayer reportPlayer, float yaw, float pitch) {
        Location lookLocation = LocationUtils.LOOK_LOCATION_EXAMPLE.clone();
        lookLocation.setYaw(yaw);
        lookLocation.setPitch(pitch);
        this.playerLocations.put(reportPlayer, lookLocation);
    }

    public void logMove(ReportPlayer reportPlayer, double x, double y, double z) {
        Location moveLocation = LocationUtils.MOVE_LOCATION_EXAMPLE.clone();
        moveLocation.setX(x);
        moveLocation.setY(y);
        moveLocation.setZ(z);
        this.playerLocations.put(reportPlayer, moveLocation);
    }

    public void logMoveAndLook(ReportPlayer reportPlayer, Location location) {
        Location locationCopy = location.clone();
        this.playerLocations.put(reportPlayer, locationCopy);
    }

    public void logReportedPlayerWorldChange(Location worldChange) {
        this.reportedPlayerWorldChange = worldChange.clone();
    }

    public void logSneak(ReportPlayer reportPlayer, boolean sneak) {
        this.playersSneaking.put(reportPlayer, sneak);
    }

    public void logOnFire(ReportPlayer reportPlayer, boolean onFire) {
        this.playersOnFire.put(reportPlayer, onFire);
    }

    public void logSwingArm(ReportPlayer reportPlayer) {
        this.swingingArms.add(reportPlayer);
    }

    public void logDamageAnimation(ReportPlayer reportPlayer) {
        this.damageAnimations.add(reportPlayer);
    }

    public void logEquipmentChange(ReportPlayer reportPlayer, int recordedSlotNumber, ItemStack itemStack) {
        this.logEquipmentChange(reportPlayer, new int[]{recordedSlotNumber}, new ItemStack[]{itemStack});
    }

    public void logEquipmentChange(ReportPlayer reportPlayer, int recordedSlotNumber[], ItemStack itemStack[]) {
        HashMap<Integer, ItemStack> equipment = new HashMap<>();
        for (int slot : recordedSlotNumber) {
            if (slot < itemStack.length) {
                equipment.put(slot, itemStack[slot]);
            }
        }
        this.equipmentChanges.put(reportPlayer, equipment);
    }

    /*

    The below methods are for checking whether or not a recordedPlayer needs to be
    updated withing a ReportViewing.

     */

    public boolean hasSpawnedPlayers() {
        return this.spawnedPlayers.size() > 0;
    }

    public boolean hasReportedPlayerQuitMessage() {
        return reportedPlayerQuitMessage != null;
    }

    public boolean hasDespawnedPlayers() {
        return this.despawnedPlayerIDs.size() > 0;
    }

    public boolean hasReportsInFrame() {
        return this.reportsInFrame.size() > 0;
    }

    public boolean hasPlayerLocationChange() {
        return this.playerLocations.size() > 0;
    }

    public boolean hasReportedPlayerWorldChange() {
        return this.reportedPlayerWorldChange != null;
    }

    public boolean hasSneakChange() {
        return this.playersSneaking.size() > 0;
    }

    public boolean hasOnFireChange() {
        return this.playersOnFire.size() > 0;
    }

    public boolean hasSwingingArms() {
        return this.swingingArms.size() > 0;
    }

    public boolean hasDamageAnimations() {
        return this.playerLocations.size() > 0;
    }

    public boolean hasEquipmentChanges() {
        return this.equipmentChanges.size() > 0;
    }

    /*

    The below methods are for retrieving the checked changes within this frame.

     */

    public HashMap<ReportPlayer, Location> getSpawnedPlayers() {
        return spawnedPlayers;
    }

    public String getReportedPlayerQuitMessage() {
        return reportedPlayerQuitMessage;
    }

    public ArrayList<Integer> getDespawnedPlayerIDs() {
        return despawnedPlayerIDs;
    }

    public ArrayList<Report> getReportsInFrame() {
        return reportsInFrame;
    }

    public HashMap<ReportPlayer, Location> getPlayerLocations() {
        return playerLocations;
    }

    public Location getReportedPlayerWorldChange() {
        return reportedPlayerWorldChange;
    }

    public HashMap<ReportPlayer, Boolean> getPlayersSneaking() {
        return playersSneaking;
    }

    public HashMap<ReportPlayer, Boolean> getPlayersOnFire() {
        return playersOnFire;
    }

    public ArrayList<ReportPlayer> getSwingingArms() {
        return swingingArms;
    }

    public ArrayList<ReportPlayer> getDamageAnimations() {
        return damageAnimations;
    }

    public HashMap<ReportPlayer, HashMap<Integer, ItemStack>> getEquipmentChanges() {
        return equipmentChanges;
    }
}
