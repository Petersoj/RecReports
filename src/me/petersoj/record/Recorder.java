package me.petersoj.record;

import com.google.gson.Gson;
import me.petersoj.controller.FileController;
import me.petersoj.report.ReportPlayer;
import me.petersoj.report.ReportsFolder;
import me.petersoj.util.DebugUtils;
import me.petersoj.util.JsonUtils;
import me.petersoj.util.adapters.LocationAdapter;
import me.petersoj.util.adapters.ReportAdapter;
import me.petersoj.util.adapters.ReportPlayerAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class is responsible for capturing/recording and writing captured/recorded data to the disk.
 */
public class Recorder {

    private FileController fileController;

    private ReportsFolder reportsFolder;
    private int frameCount;
    private Frame currentFrame;
    private Gson gson = JsonUtils.getGson();
    private LocationAdapter locationAdapter = JsonUtils.LOCATION_ADAPTER;
    private ReportPlayerAdapter reportPlayerAdapter = JsonUtils.REPORT_PLAYER_ADAPTER;
    private ReportAdapter reportAdapter = JsonUtils.REPORT_ADAPTER;
    private BufferedWriter writer;
    private Player reportedPlayer;
    private ArrayList<ReportPlayer> otherRecordedPlayers;

    public Recorder(FileController fileController, Player reportedPlayer, ReportsFolder reportsFolder) {
        this.fileController = fileController;

        this.reportedPlayer = reportedPlayer;
        this.reportsFolder = reportsFolder;
        this.currentFrame = new Frame();
        this.otherRecordedPlayers = new ArrayList<>();
    }

    /**
     * Attempts to start the recording by creating the recording file stream.
     *
     * @return true if the recording file could be created.
     */
    public synchronized boolean startRecording() {
        if (writer == null) {
            // Create a new BufferedWriter to a file with currentTimeMillis as the name.
            String recordingName = String.valueOf(System.currentTimeMillis());
            this.writer = fileController.getNewBufferedWriter(reportsFolder.getFolder(), recordingName);

            if (writer == null) {
                return false;
            }

            this.write("["); // Write out the first part of a json array.
            return true;
        }
        return false;
    }

    /**
     * Attempts to stop the recording file stream.
     *
     * @return true if the recording was stopped successfully.
     */
    public synchronized boolean stopRecording() {
        if (writer == null) {
            return false;
        }
        try {
            this.write("]"); // Finish off the recording array.

            writer.flush(); // Writes out all bytes in the buffer.
            writer.close(); // Closes the writing stream.
            return true;
        } catch (IOException e) {
            DebugUtils.handleError(e, false);
        }
        return false;
    }

    /**
     * This method captures a players movements as well as surrounding players' movements
     * and inserts them into the Frame.
     */
    public void captureFrame() {

    }

    /**
     * This method will convert the currentFrame object into a json string
     * and write it to the disk.
     */
    public void serializeCurrentFrame() {
        StringBuilder jsonString = new StringBuilder(40); // Initial capacity of 40 characters.
        jsonString.append("["); // Don't add comma before because of first frame.

        // Set adapter serializing techniques.
        locationAdapter.setMinimizeLocation(true);
        locationAdapter.setIncludeWorlds(true);
        locationAdapter.setCheckMovementType(false);
        reportPlayerAdapter.setSerializeFullReportPlayer(true); // Serialize entire ReportPlayer.
        reportAdapter.setSerializeFullReport(false);

        // If statements below check frame data and serialize accordingly.

        if (currentFrame.hasSpawnedPlayers()) {
            serializeObject(currentFrame.getSpawnedPlayers(), JsonUtils.MAP_REPORT_PLAYER_LOCATION, jsonString);
        }
        if (currentFrame.hasReportedPlayerQuitMessage()) {
            serializeObject(currentFrame.getReportedPlayerQuitMessage(), String.class, jsonString);
        }
        if (currentFrame.hasDespawnedPlayers()) {
            serializeObject(currentFrame.getDespawnedPlayerIDs(), JsonUtils.ARRAYLIST_INTEGER, jsonString);
        }
        if (currentFrame.hasReportsInFrame()) {
            serializeObject(currentFrame.getReportsInFrame(), JsonUtils.ARRAYLIST_REPORT, jsonString);
        }

        reportPlayerAdapter.setSerializeFullReportPlayer(false); // ReportPlayer within HashMap should not be serialized for all of below.

        locationAdapter.setIncludeWorlds(false); // Worlds should not be serialized for below.
        locationAdapter.setCheckMovementType(true); // The Location within playerLocations will have various movement types encoded in the Location.
        if (currentFrame.hasPlayerLocationChange()) {
            serializeObject(currentFrame.getPlayerLocations(), JsonUtils.MAP_REPORT_PLAYER_LOCATION, jsonString);
        }

        locationAdapter.setIncludeWorlds(true); // Worlds should be serialized for below.
        locationAdapter.setCheckMovementType(false); // We need the full location for the new world.
        if (currentFrame.hasReportedPlayerWorldChange()) {
            serializeObject(currentFrame.getReportedPlayerWorldChange(), Location.class, jsonString);
        }
        if (currentFrame.hasSneakChange()) {
            serializeObject(currentFrame.getPlayersSneaking(), JsonUtils.MAP_REPORT_PLAYER_BOOLEAN, jsonString);
        }
        if (currentFrame.hasOnFireChange()) {
            serializeObject(currentFrame.getPlayersOnFire(), JsonUtils.MAP_REPORT_PLAYER_BOOLEAN, jsonString);
        }
        if (currentFrame.hasSwingingArms()) {
            serializeObject(currentFrame.getSwingingArms(), JsonUtils.ARRAYLIST_REPORT_PLAYER, jsonString);
        }
        if (currentFrame.hasDamageAnimations()) {
            serializeObject(currentFrame.getDamageAnimations(), JsonUtils.ARRAYLIST_REPORT_PLAYER, jsonString);
        }
        if (currentFrame.hasEquipmentChanges()) {
            serializeObject(currentFrame.getEquipmentChanges(), JsonUtils.MAP_EQUIPMENT_TYPE, jsonString);
        }

        // Remove the last objects comma to prevent data parsing errors.
        int lastIndex = jsonString.length() - 1;
        if (jsonString.charAt(lastIndex) == ',') {
            jsonString.deleteCharAt(lastIndex);
        }

        jsonString.append("]"); // End off this frame object array.

        // Finally write out to the bufferedWriter.
        this.write(jsonString.toString());
    }

    private void serializeObject(Object object, Type type, StringBuilder stringBuilder) {
        stringBuilder.append(gson.toJson(object, type)).append(","); // Appends toJson and comma for next object.
    }

    /**
     * End the current frame/JsonObject and start on with the next frame/JsonObject.
     */
    public void setupNextFrame() {
        this.write(","); // Write comma to separate arrays
        this.currentFrame.resetFrame(); // Reset the frame constant.
    }

    private void write(String data) {
        try {
            if (writer != null) {
                writer.write(data);
            }
        } catch (IOException e) {
            DebugUtils.handleError(e, false);
        }
    }

    public int getFrameCount() {
        return frameCount;
    }

    public Frame getCurrentFrame() {
        return currentFrame;
    }
}
