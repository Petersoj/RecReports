package me.petersoj.record;

import me.petersoj.controller.FileController;
import me.petersoj.nms.RecordedPlayer;
import me.petersoj.report.Report;
import me.petersoj.report.ReportsFolder;
import me.petersoj.util.LogUtils;
import org.bukkit.Location;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is responsible for writing captured/recorded to the disk.
 */
public class Recorder {

    private FileController fileController;

    private ReportsFolder reportsFolder;
    private RecordedPlayer reportedPlayer;
    private ArrayList<RecordedPlayer> recordedPlayers;
    private BufferedWriter writer;

    public Recorder(FileController fileController, RecordedPlayer reportedPlayer, ReportsFolder reportsFolder) {
        this.fileController = fileController;
        this.reportedPlayer = reportedPlayer;
        this.reportsFolder = reportsFolder;
        this.recordedPlayers = new ArrayList<>();
    }

    /**
     * Attempts to create the recording file stream.
     *
     * @return true if the recording file could be created.
     */
    public boolean startRecordingStream() {
        if (writer == null) {
            // Create a new BufferedWriter to a file with currentTimeMillis as the name.
            String recordingName = String.valueOf(System.currentTimeMillis());
            this.writer = fileController.getNewBufferedWriter(reportsFolder.getFolder(), recordingName);

            return this.writer != null;
        } else {
            return false;
        }
    }

    /**
     * Attempts to stop the recording file stream.
     *
     * @return true if the recording was stopped successfully.
     */
    public boolean stopRecordingStream() {
        try {
            writer.flush(); // Writes out all bytes in the buffer.
            writer.close(); // Closes the writing stream.
            return true;
        } catch (IOException e) {
            LogUtils.handleError(e, false);
        }
        return false;
    }

    /**
     * End the current frame/JsonObject and start on with the next frame/JsonObject.
     */
    public void nextFrame() {

    }

    public void logReport(Report report) {

    }

    public void logSpawn(RecordedPlayer recordedPlayer, Location spawnLocation) {

    }

    public void logLook(RecordedPlayer recordedPlayer, float yaw, float pitch) {

    }

    /**
     * Logs the any type of movement (teleport or regular) because distance check
     * will be using on RecordingPlayback.
     */
    public void logMovement(RecordedPlayer recordedPlayer, Location newLocation) {

    }

    public void logFire(RecordedPlayer recordedPlayer, boolean fire) {

    }
}
