package me.petersoj.record;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import me.petersoj.record.adapters.ReportPlayerIDAdapter;
import me.petersoj.report.Report;
import me.petersoj.report.ReportPlayer;
import me.petersoj.report.ReportsFolder;
import me.petersoj.util.JsonUtils;
import me.petersoj.util.adapters.LocationAdapter;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class represents a frame in a recording.
 */
public class Frame {

    // Below are the variables for Gson generics to use during [de]serialization
    private static Type mapReportPlayerLocationType;
    private static Type arrayListIntegerType;
    private static Type arrayListReportType;
    private static Type mapReportPlayerBooleanType;
    private static Type arrayListReportPlayerType;
    private static Type mapReportPlayerMapIntegerItemStackType;

    static {
        // TypeToken has to be anonymous for some speciality.
        mapReportPlayerLocationType = new TypeToken<HashMap<ReportPlayer, Location>>() {
        }.getType();
        arrayListIntegerType = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        arrayListReportType = new TypeToken<ArrayList<Report>>() {
        }.getType();
        mapReportPlayerBooleanType = new TypeToken<HashMap<ReportPlayer, Boolean>>() {
        }.getType();
        arrayListReportPlayerType = new TypeToken<ArrayList<ReportPlayer>>() {
        }.getType();
        mapReportPlayerMapIntegerItemStackType = new TypeToken<HashMap<ReportPlayer, HashMap<Integer, ItemStack>>>() {
        }.getType();
    }

    private ReportsFolder reportsFolder;

    private Gson reportPlayerIDGson;

    private HashMap<ReportPlayer, Location> spawnedPlayers;
    private ArrayList<Integer> despawnedPlayerIDs;
    private ArrayList<Report> reportsInFrame;
    private HashMap<ReportPlayer, Location> playerLocations;
    private HashMap<ReportPlayer, Location> playerWorldChanges;
    private HashMap<ReportPlayer, Boolean> playersSneaking;
    private HashMap<ReportPlayer, Boolean> playersOnFire;
    private ArrayList<ReportPlayer> swingingArms;
    private ArrayList<ReportPlayer> damageAnimations;
    private HashMap<ReportPlayer, HashMap<Integer, ItemStack>> equipmentChanges;

    public Frame(ReportsFolder reportsFolder) {
        this(reportsFolder, true);
    }

    public Frame(ReportsFolder reportsFolder, RecordingPlayback recordingPlayback) {
        this(reportsFolder, false);

        // Create a new Gson for the times when we need only to deserialize the reportPlayerID
        this.reportPlayerIDGson = new GsonBuilder()
                .registerTypeAdapter(ReportPlayer.class, new ReportPlayerIDAdapter(recordingPlayback))
                .registerTypeAdapter(Location.class, new LocationAdapter())
                .create();
    }

    private Frame(ReportsFolder reportsFolder, boolean createGson) {
        this.reportsFolder = reportsFolder;

        if (createGson) {
            // Create a new Gson for the times when we need only to serialize the reportPlayerID
            this.reportPlayerIDGson = new GsonBuilder()
                    .registerTypeAdapter(ReportPlayer.class, new ReportPlayerIDAdapter())
                    .registerTypeAdapter(Location.class, new LocationAdapter())
                    .create();
        }

        this.spawnedPlayers = new HashMap<>();
        this.despawnedPlayerIDs = new ArrayList<>();
        this.reportsInFrame = new ArrayList<>();
        this.playerLocations = new HashMap<>();
        this.playerWorldChanges = new HashMap<>();
        this.playersSneaking = new HashMap<>();
        this.playersOnFire = new HashMap<>();
        this.swingingArms = new ArrayList<>();
        this.damageAnimations = new ArrayList<>();
        this.equipmentChanges = new HashMap<>();
    }

    /**
     * This method will serialize this frame data into a json string.
     *
     * @return a json object string representing this frame
     */
    public String serialize() {
        Gson regularGson = JsonUtils.getGson(); // Used to fully serialize the ReportPlayer Object

        StringBuilder jsonString = new StringBuilder();
        jsonString.append("["); // Initial frame object array

        if (hasSpawnedPlayers()) {
            regularGson.toJson(spawnedPlayers, mapReportPlayerLocationType, jsonString);
        } else if (hasDespawnedPlayers()) {
            reportPlayerIDGson.toJson(despawnedPlayerIDs, arrayListIntegerType, jsonString);
        } else if (hasReportsInFrame()) {
            regularGson.toJson(despawnedPlayerIDs, arrayListIntegerType, jsonString);
        } else if (hasPlayerLocationChange()) {
            reportPlayerIDGson.toJson(playerLocations, mapReportPlayerLocationType, jsonString);
        } else if (hasPlayerWorldChange()) {
            reportPlayerIDGson.toJson(playerWorldChanges, mapReportPlayerLocationType, jsonString);
        } else if (hasSneakChange()) {

        } else if (hasOnFireChange()) {

        } else if (hasSwingingArms()) {

        } else if (hasDamageAnimations()) {

        } else if (hasEquipmentChanges()) {

        }

        jsonString.append("]"); // Finish off the array frame object.
        return "";
    }

    /**
     * This method will populate the data members of this frame from
     * the given JsonObject.
     *
     * @param frame the JsonObject representation of a recorded frame.
     */
    public void deserialize(JsonObject frame) {

    }

    /**
     * This method is used if the frame object be used for multiple frames
     * rather than being re-instantiated for each frame in a recording.
     */
    public void resetFrame() {
        this.spawnedPlayers.clear();
        this.despawnedPlayerIDs.clear();
        this.reportsInFrame.clear();
        this.playerLocations.clear();
        this.playerWorldChanges.clear();
        this.playersSneaking.clear();
        this.playersOnFire.clear();
        this.swingingArms.clear();
        this.damageAnimations.clear();
        this.equipmentChanges.clear();
    }

    /*

    The below methods are for logging player actions which will be later serialized
    for writing to the disk.

     */

    public void logSpawnedPlayer(ReportPlayer spawnedPlayer, Location spawnLocation) {
        this.spawnedPlayers.put(spawnedPlayer, spawnLocation);
    }

    public void logDespawnedPlayer(int despawnedPlayerID) {
        this.despawnedPlayerIDs.add(despawnedPlayerID);
    }

    public void logReport(Report report) {
        this.reportsInFrame.add(report);
    }

    public void logLook(ReportPlayer reportPlayer, float yaw, float pitch) {
        this.playerLocations.put(reportPlayer, createLookLocation(yaw, pitch));
    }

    public void logMove(ReportPlayer reportPlayer, double x, double y, double z) {
        this.playerLocations.put(reportPlayer, createMoveLocaiton(x, y, z));
    }

    public void logMoveAndLook(ReportPlayer reportPlayer, Location location) {
        Location locationCopy = new Location(null, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        this.playerLocations.put(reportPlayer, locationCopy);
    }

    public void logChangeWorld(ReportPlayer reportPlayer, Location world) {
        Location locationCopy = new Location(world.getWorld(), world.getX(), world.getY(), world.getZ(), world.getYaw(), world.getPitch());
        this.playerWorldChanges.put(reportPlayer, locationCopy);
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

    public boolean hasDespawnedPlayers() {
        return this.despawnedPlayerIDs.size() > 0;
    }

    public boolean hasReportsInFrame() {
        return this.reportsInFrame.size() > 0;
    }

    public boolean hasPlayerLocationChange() {
        return this.playerLocations.size() > 0;
    }

    public boolean hasPlayerWorldChange() {
        return this.playerWorldChanges.size() > 0;
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

    public ArrayList<Integer> getDespawnedPlayerIDs() {
        return despawnedPlayerIDs;
    }

    public ArrayList<Report> getReportsInFrame() {
        return reportsInFrame;
    }

    public HashMap<ReportPlayer, Location> getPlayerLocations() {
        return playerLocations;
    }

    public HashMap<ReportPlayer, Location> getPlayerWorldChanges() {
        return playerWorldChanges;
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

    /**
     * This method gets
     *
     * @param location the location to check.
     * @return the type of movement
     */
    public MovementType getMovementType(Location location) {
        if (location == null) {
            throw new NullPointerException("The Location cannot be null!");
        }

        if ((int) location.getX() == Integer.MIN_VALUE && location.getY() == 0 && location.getZ() == 0) {
            return MovementType.LOOK;
        } else if (location.getYaw() == 360f && location.getPitch() == 360f) {
            return MovementType.MOVE;
        } else {
            return MovementType.MOVE_AND_LOOK;
        }
    }

    private Location createLookLocation(float yaw, float pitch) {
        return new Location(null, Integer.MIN_VALUE, 0, 0, yaw, pitch);
    }

    private Location createMoveLocaiton(double x, double y, double z) {
        return new Location(null, x, y, z, 360f, 360f);
    }
}
