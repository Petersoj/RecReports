package me.petersoj.controller;

import me.petersoj.RecReportsPlugin;
import me.petersoj.report.ReportsFolder;
import me.petersoj.util.DebugUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

/**
 * This singleton controller class is simply used for reading and writing to files
 * in the data directory and holding config values.
 */
public class FileController {

    private RecReportsPlugin plugin;
    private File dataFolder;
    private int recordThreshold;
    private int banThreshold;
    private int recordingTime;
    private List<String> reportTypes;
    private int reportCommandCooldown;
    private boolean loggingEnabled;

    public FileController(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.saveDefaultConfig(); // Copies the config file if it doesn't exist

        this.readConfig();
        this.setupDataFolder();
    }

    @SuppressWarnings("unchecked")
    private void readConfig() {
        FileConfiguration config = plugin.getConfig();
        this.recordThreshold = config.getInt("record-threshold");
        this.banThreshold = config.getInt("ban-threshold");
        this.recordingTime = config.getInt("recording-time");
        this.reportTypes = (List<String>) config.getList("report-type-list");
        this.reportCommandCooldown = config.getInt("report-command-cooldown");
        this.loggingEnabled = config.getBoolean("logging-enabled");
    }

    private void setupDataFolder() {
        this.dataFolder = new File(plugin.getDataFolder(), "/ReportData");

        if (!dataFolder.mkdir() && !dataFolder.exists()) { // If the directory wasn't made at all.
            DebugUtils.handleError(new IOException("The Report Data folder could not be created!"), true);
        }
    }

    public ReportsFolder getReportsFolder(UUID playerUUID) {
        File reportFile = new File(dataFolder, playerUUID.toString());
        if (!reportFile.mkdir() && !reportFile.exists()) { // If the directory wasn't made at all.
            DebugUtils.handleError(new IOException("The Report File could not be created!"), false);
        }
        return new ReportsFolder(this, playerUUID, reportFile);
    }

    public void saveSmallFile(File path, String fileName, String contents) {
        this.saveSmallFile(new File(path, fileName), contents);
    }

    public void saveSmallFile(File pathToFile, String contents) {
        try {
            Files.write(pathToFile.toPath(), contents.getBytes("UTF-8")); // Default OpenOptions are perfect here
        } catch (IOException e) {
            DebugUtils.handleError(e, false);
        }
    }

    public String readFileFully(File path, String fileName) {
        return this.readFileFully(new File(path, fileName));
    }

    public String readFileFully(File pathToFile) {
        String contents = null;
        try {
            byte[] fileBytes = Files.readAllBytes(pathToFile.toPath());
            contents = new String(fileBytes);
        } catch (IOException e) {
            DebugUtils.handleError(e, false);
        }
        return contents;
    }

    public BufferedWriter getNewBufferedWriter(File path, String fileName) {
        return getNewBufferedWriter(new File(path, fileName));
    }

    public BufferedWriter getNewBufferedWriter(File pathToFile) {
        try {
            return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathToFile), "UTF-8")); // default buffer size if good.
        } catch (IOException e) {
            DebugUtils.handleError(e, false);
        }
        return null;
    }

    public File getDataFolder() {
        return dataFolder;
    }

    public int getRecordThreshold() {
        return recordThreshold;
    }

    public int getBanThreshold() {
        return banThreshold;
    }

    public int getRecordingTime() {
        return recordingTime;
    }

    public List<String> getReportTypes() {
        return reportTypes;
    }

    public int getReportCommandCooldown() {
        return reportCommandCooldown;
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }
}
