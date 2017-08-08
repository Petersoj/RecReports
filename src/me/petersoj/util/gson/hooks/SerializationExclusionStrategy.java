package me.petersoj.util.gson.hooks;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import me.petersoj.util.JsonUtils;
import me.petersoj.util.gson.adapters.LocationAdapter;
import me.petersoj.util.gson.adapters.ReportAdapter;
import me.petersoj.util.gson.adapters.ReportPlayerAdapter;

/**
 * This class is an ExclusionStrategy that acts as a hook for Gson so that we can track whether
 * or not we need to change the adapters serialization techniques based on the field.
 */
public class SerializationExclusionStrategy implements ExclusionStrategy {

    private boolean exclusionChecking;
    private LocationAdapter locationAdapter = JsonUtils.LOCATION_ADAPTER;
    private ReportPlayerAdapter reportPlayerAdapter = JsonUtils.REPORT_PLAYER_ADAPTER;
    private ReportAdapter reportAdapter = JsonUtils.REPORT_ADAPTER;

    // This acts as our hook for every field Gson encounters.
    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        if (exclusionChecking) { // Only do below if we have to.
            String fieldName = fieldAttributes.getName();

            if (fieldName.equals("spawnedPlayers")) {
                locationAdapter.setIncludeWorlds(true);
                locationAdapter.setCheckMovementType(false);
                reportPlayerAdapter.setSerializeFullReportPlayer(true);
            } else if (fieldName.equals("reportsInFrame")) {
                reportAdapter.setSerializeFullReport(false);
            } else if (fieldName.equals("playerLocations")) {
                reportPlayerAdapter.setSerializeFullReportPlayer(false);
                locationAdapter.setIncludeWorlds(false);
                locationAdapter.setCheckMovementType(true);
            } else if (fieldName.equals("reportedPlayerWorldChange")) {
                locationAdapter.setIncludeWorlds(true);
                locationAdapter.setCheckMovementType(false);
            } else {
                reportPlayerAdapter.setSerializeFullReportPlayer(false); // Only spawnedPlayers needs this.
            }
        }

        return false; // Never skip any fields in our case.
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
}
