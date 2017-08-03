package me.petersoj.record;

import me.petersoj.controller.FileController;
import me.petersoj.report.ReportPlayer;
import me.petersoj.report.ReportsFolder;
import me.petersoj.util.LogUtils;
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

        if (currentFrame.hasSpawnedPlayers()) {

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
}
