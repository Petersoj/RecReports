package me.petersoj.report;

import me.petersoj.controller.FileController;
import me.petersoj.util.JsonUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

/**
 * This class represents the folder in the data directory that holds
 * all the information about reports and recordings on a specific player.
 */
public class ReportsFolder {

    private FileController fileController;

    private UUID playerUUID;
    private File folder; // A reference to this ReportsFolder in the file system.
    private ReportInfo reportInfo; // Info data stored in info.txt
    private ArrayList<Report> reports; // Reports stored in reports.txt

    public ReportsFolder(FileController fileController, UUID playerUUID, File folder) {
        this.fileController = fileController;
        this.playerUUID = playerUUID;
        this.folder = folder;
        this.reportInfo = new ReportInfo();
        this.reports = new ArrayList<>();
    }

    public void saveData() {
        JsonUtils.SERIALIZATION_EXCLUSION_STRATEGY.setExclusionChecking(false); // No need to check recording fields for this data.

        JsonUtils.REPORT_PLAYER_ADAPTER.setSerializeFullReportPlayer(true); // Serialize complete object within ReportInfo.
        String infoJson = JsonUtils.getGson().toJson(reportInfo);
        fileController.saveSmallFile(folder, "info.txt", infoJson);

        JsonUtils.REPORT_ADAPTER.setSerializeFullReport(true); // Serialize complete object.
        String reportsJson = JsonUtils.getGson().toJson(reports, JsonUtils.ARRAYLIST_REPORT);
        fileController.saveSmallFile(folder, "reports.txt", reportsJson);
    }

    public void readData() {
        JsonUtils.DESERIALIZATION_EXCLUSION_STRATEGY.setExclusionChecking(false); // No need to check recording fields for this data.

        JsonUtils.REPORT_PLAYER_ADAPTER.setDeserializeFromID(null); // Deserialize complete object within ReportInfo.
        String infoJson = fileController.readFileFully(folder, "info.txt");
        this.reportInfo = JsonUtils.getGson().fromJson(infoJson, ReportInfo.class);

        JsonUtils.REPORT_ADAPTER.setDeserializeFromID(null); // Deserialize complete object.
        String reportsJson = fileController.readFileFully(folder, "reports.txt");
        this.reports = JsonUtils.getGson().fromJson(reportsJson, JsonUtils.ARRAYLIST_REPORT);
    }

    public Report getReportByID(int reportID) {
        for (Report report : reports) {
            if (report.getReportID() == reportID) {
                return report;
            }
        }
        return null;
    }

    public Report createReport(String reporterName, String reportReason) {
        Report report = new Report(reports.size(), reporterName, reportReason);
        this.reports.add(report);
        return report;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public File getFolder() {
        return folder;
    }

    public ReportInfo getReportInfo() {
        return reportInfo;
    }
}
