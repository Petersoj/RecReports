package me.petersoj.record;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import me.petersoj.controller.FileController;
import me.petersoj.nms.RecordedPlayer;
import me.petersoj.report.ReportsFolder;
import me.petersoj.util.DebugUtils;
import me.petersoj.util.JsonUtils;

import java.util.ArrayList;

/**
 * This class will playback a previously saved recording.
 */
public class RecordingPlayback {

    private FileController fileController;

    private ReportsFolder reportsFolder;
    private JsonArray recordingArray;
    private Frame currentFrame;
    private int frameIndex;
    private ArrayList<RecordedPlayer> recordedPlayers;

    public RecordingPlayback(FileController fileController, ReportsFolder reportsFolder) {
        this.fileController = fileController;

        this.reportsFolder = reportsFolder;
        this.currentFrame = new Frame();
        this.recordedPlayers = new ArrayList<>();
    }

    /**
     * This method attempts to read the recording and parse it into a JsonArray.
     *
     * @param recordingIndex the recording index from ReportInfo's list of recording names.
     * @return if the recording was successfully read.
     */
    public boolean readRecording(int recordingIndex) {
        if (recordingArray == null) {
            String recordingName = reportsFolder.getReportInfo().getRecordingFileNames().get(recordingIndex);
            String fullRecording = fileController.readFileFully(reportsFolder.getFolder(), recordingName);

            if (fullRecording != null) {
                try {
                    this.recordingArray = JsonUtils.getJsonParser().parse(fullRecording).getAsJsonArray();

                    if (recordingArray != null) {
                        return true;
                    }
                } catch (JsonParseException e) {
                    DebugUtils.handleError(e, false);
                }
            }
        }
        return false;
    }

    /**
     * This method will read the next frame from frame index.
     */
    public void readNextFrame() {
        this.deserializeCurrentFrame();
        this.frameIndex++;
    }

    /**
     * This method will deserialize a json object within recording array into currentFrame.
     */
    private void deserializeCurrentFrame() {
        JsonObject frameObject = recordingArray.get(frameIndex).getAsJsonObject();

        JsonUtils.DESERIALIZATION_EXCLUSION_STRATEGY.setExclusionChecking(true);

        // Set currentFrame to deserialized frame from Json.
        this.currentFrame = JsonUtils.getGson().fromJson(frameObject, Frame.class);
    }

    public ArrayList<RecordedPlayer> getRecordedPlayers() {
        return recordedPlayers;
    }

    public void incrementFrameIndex() {
        this.frameIndex++;
    }

    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
    }

    public int getFrameIndex() {
        return frameIndex;
    }
}
