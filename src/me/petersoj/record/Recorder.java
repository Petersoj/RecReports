package me.petersoj.record;

import com.google.gson.Gson;
import me.petersoj.controller.FileController;
import me.petersoj.report.ReportPlayer;
import me.petersoj.report.ReportsFolder;
import me.petersoj.util.JsonUtils;
import me.petersoj.util.LogUtils;
import me.petersoj.util.adapters.LocationAdapter;
import me.petersoj.util.adapters.ReportAdapter;
import me.petersoj.util.adapters.ReportPlayerAdapter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is responsible for capturing/recording and writing captured/recorded to the disk.
 */
public class Recorder {

    private static int MAX_FRAME_COUNT; // Maximum frame count aka recording time limit.

    private FileController fileController;

    private ReportsFolder reportsFolder;
    private int frameCount;
    private int emptyFrameCount;
    private Frame currentFrame;
    private Player reportedPlayer;
    private ArrayList<ReportPlayer> otherRecordedPlayers;
    private BufferedWriter writer;

    public Recorder(FileController fileController, Player reportedPlayer, ReportsFolder reportsFolder) {
        this.fileController = fileController;
        this.reportedPlayer = reportedPlayer;
        this.reportsFolder = reportsFolder;
        this.otherRecordedPlayers = new ArrayList<>();
    }

    public static void setMaxFrameCount(int frameCount) {
        MAX_FRAME_COUNT = frameCount;
    }

    /**
     * Attempts to start the recording by creating the recording file stream.
     *
     * @return true if the recording file could be created.
     */
    public boolean startRecording() {
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
    public boolean stopRecording() {
        if (writer == null) {
            return false;
        }
        try {
            this.write("]"); // Finish off the recording array.

            writer.flush(); // Writes out all bytes in the buffer.
            writer.close(); // Closes the writing stream.
            return true;
        } catch (IOException e) {
            LogUtils.handleError(e, false);
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
    public void serializeFrame() {
        StringBuilder jsonString = new StringBuilder();
        jsonString.append("["); // Don't add comma before because of first frame.

        Gson gson = JsonUtils.getGson();

        LocationAdapter locationAdapter = JsonUtils.LOCATION_ADAPTER;
        ReportPlayerAdapter reportPlayerAdapter = JsonUtils.REPORT_PLAYER_ADAPTER;
        ReportAdapter reportAdapter = JsonUtils.REPORT_ADAPTER;

        // Set adapter serializing techniques
        locationAdapter.setMinimizeLocation(true);
        locationAdapter.setIncludeWorlds(true);
        reportPlayerAdapter.setSerializeFullReportPlayer(true); // Serialize entire ReportPlayer
        reportAdapter.setSerializeFullReport(false);

        // If statements below check frame data and serialize accordingly.

        if (currentFrame.hasSpawnedPlayers()) {
            gson.toJson(currentFrame.getSpawnedPlayers(), JsonUtils.MAP_REPORT_PLAYER_LOCATION, jsonString);
        }
        if (currentFrame.hasReportedPlayerQuitMessage()) {
            gson.toJson(currentFrame.getReportedPlayerQuitMessage(), String.class, jsonString);
        }
        if (currentFrame.hasDespawnedPlayers()) {
            gson.toJson(currentFrame.getDespawnedPlayerIDs(), JsonUtils.ARRAYLIST_INTEGER, jsonString);
        }
        if (currentFrame.hasReportsInFrame()) {
            gson.toJson(currentFrame.getReportsInFrame(), JsonUtils.ARRAYLIST_REPORT, jsonString);
        }

        reportPlayerAdapter.setSerializeFullReportPlayer(false); // ReportPlayer within HashMap should not be serialized for below.
        locationAdapter.setIncludeWorlds(false); // Worlds should not be serialized for below.
        if (currentFrame.hasPlayerLocationChange()) {
            gson.toJson(currentFrame.getPlayerLocations(), JsonUtils.MAP_REPORT_PLAYER_LOCATION, jsonString);
        }

        locationAdapter.setIncludeWorlds(true); // Worlds should be serialized for below.
        if (currentFrame.hasReportedPlayerWorldChange()) {
            gson.toJson(currentFrame.getReportedPlayerWorldChange(), Location.class, jsonString);
        }
        if (currentFrame.hasSneakChange()) {
            gson.toJson(currentFrame.getPlayersSneaking(), JsonUtils.MAP_REPORT_PLAYER_BOOLEAN, jsonString);
        }
        if (currentFrame.hasOnFireChange()) {
            gson.toJson(currentFrame.getPlayersOnFire(), JsonUtils.MAP_REPORT_PLAYER_BOOLEAN, jsonString);
        }
        if (currentFrame.hasSwingingArms()) {
            gson.toJson(currentFrame.getSwingingArms(), JsonUtils.ARRAYLIST_REPORT_PLAYER, jsonString);
        }
        if (currentFrame.hasDamageAnimations()) {
            gson.toJson(currentFrame.getDamageAnimations(), JsonUtils.ARRAYLIST_REPORT_PLAYER, jsonString);
        }
        if (currentFrame.hasEquipmentChanges()) {
            gson.toJson(currentFrame.getEquipmentChanges(), JsonUtils.MAP_EQUIPMENT_TYPE, jsonString);
        }

        // Handle empty frames.
        if (jsonString.length() == 1) { // Only character in Appendable is '['
            this.emptyFrameCount++;
        } else if (emptyFrameCount > 0 && frameCount == MAX_FRAME_COUNT) { // Add empty if end of recording or there is empty frames.
            jsonString.append("{\"empty\":").append(emptyFrameCount); // Append empty frame count so the playback knows.
            this.emptyFrameCount = 0;
        }

        jsonString.append("]"); // Finish off the array of objects
        this.write(jsonString.toString());
    }

    /**
     * End the current frame/JsonObject and start on with the next frame/JsonObject.
     */
    public void nextFrame() {
        this.write(","); // Write comma to separate arrays
        this.currentFrame.resetFrame(); // Reset the frame constant.
    }

    private void write(String data) {
        try {
            writer.write(data);
        } catch (IOException e) {
            LogUtils.handleError(e, false);
        }
    }

    public int getFrameCount() {
        return frameCount;
    }

    public Frame getCurrentFrame() {
        return currentFrame;
    }
}
