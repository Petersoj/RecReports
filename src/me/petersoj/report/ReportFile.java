package me.petersoj.report;

import me.petersoj.controller.FileController;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

public class ReportFile {

    private File reportFile;
    private File[] recordingFiles; // This will be set to null on a new recording save.

    public ReportFile(FileController fileController, UUID playerUUID) {
        try {
            this.setupFile(fileController, playerUUID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupFile(FileController fileController, UUID playerUUID) throws IOException {
        this.reportFile = new File(fileController.getDataFolder(), playerUUID.toString());
        if (!reportFile.mkdir() && !reportFile.exists()) { // if the directory wasn't made at all.
            throw new IOException("The report file could not be created!");
        }
    }

    private void readRecordingFiles() {
        this.recordingFiles = reportFile.listFiles();
        if (recordingFiles != null) {
            Arrays.sort(recordingFiles); // Sort the array alphabetically/numbered
        }
    }

    public void saveRecording(Recording recording) {

    }

    public Recording readRecording(int index) {
        if (recordingFiles == null) {
            this.readRecordingFiles();
        }
        return null;
    }

    public File getReportFile() {
        return reportFile;
    }
}
