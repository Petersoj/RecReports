package me.petersoj.record;

import me.petersoj.controller.FileController;
import me.petersoj.report.ReportPlayer;
import me.petersoj.report.ReportsFolder;
import me.petersoj.util.DebugUtils;
import me.petersoj.util.JsonUtils;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is responsible for capturing/recording and writing captured/recorded data to the disk.
 */
public class Recorder {

    private FileController fileController;

    private ReportsFolder reportsFolder;
    private int frameCount;
    private Frame currentFrame;
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
        JsonUtils.SERIALIZATION_EXCLUSION_STRATEGY.setExclusionChecking(true); // make sure to check fields before we serialize.

        // Write out the currentFrame object using Gson :)  SO EASY!
        this.write(JsonUtils.getGson().toJson(currentFrame, Frame.class));
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
