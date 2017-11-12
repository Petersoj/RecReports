package me.petersoj.util.gson.hooks;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import me.petersoj.record.RecordingPlayback;
import me.petersoj.report.ReportsFolder;
import me.petersoj.util.JsonUtils;
import me.petersoj.util.gson.adapters.LocationAdapter;
import me.petersoj.util.gson.adapters.ReportAdapter;
import me.petersoj.util.gson.adapters.ReportPlayerAdapter;

/**
 * This class is an ExclusionStrategy that acts as a hook for Gson so that we can track whether
 * or not we need to change the adapters deserialization techniques based on the field.
 */
public class DeserializationExclusionStrategy implements ExclusionStrategy {

    private boolean exclusionChecking;
    private LocationAdapter locationAdapter = JsonUtils.LOCATION_ADAPTER;
    private ReportPlayerAdapter reportPlayerAdapter = JsonUtils.REPORT_PLAYER_ADAPTER;
    private ReportAdapter reportAdapter = JsonUtils.REPORT_ADAPTER;

    // This acts as our hook for every field Gson encounters.
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        if (exclusionChecking) {

        }
        return false;
    }

    // We aren't skipping any classes.
    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }

    // Frame field names below for reference.
    /*
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
     */

    public synchronized void setExclusionChecking(boolean check) {
        this.exclusionChecking = check;
    }

    public synchronized void setDeserializationInstances(RecordingPlayback recordingPlayback, ReportsFolder reportsFolder) {

    }
}
