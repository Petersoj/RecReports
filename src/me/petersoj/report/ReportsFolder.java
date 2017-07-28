package me.petersoj.report;

import com.google.gson.reflect.TypeToken;
import me.petersoj.controller.FileController;
import me.petersoj.util.JsonUtils;

import java.io.File;
import java.lang.reflect.Type;
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
    private Type reportsListType; // Needed for Gson [de]serializing.

    public ReportsFolder(FileController fileController, UUID playerUUID, File folder) {
        this.fileController = fileController;
        this.playerUUID = playerUUID;
        this.folder = folder;

        this.reportsListType = new TypeToken<ArrayList<Report>>() {
        }.getType(); // Anonymous for some speciality.
    }

    public void saveData() {
        String infoJson = JsonUtils.getGson().toJson(reportInfo);
        fileController.saveSmallFile(folder, "info.txt", infoJson);

        String reportsJson = JsonUtils.getGson().toJson(reports, reportsListType);
        fileController.saveSmallFile(folder, "reports.txt", reportsJson);
    }

    public void readData() {
        String infoJson = fileController.readFileFully(folder, "info.txt");
        this.reportInfo = JsonUtils.getGson().fromJson(infoJson, ReportInfo.class);

        String reportsJson = fileController.readFileFully(folder, "reports.txt");
        this.reports = JsonUtils.getGson().fromJson(reportsJson, reportsListType);
    }

    public Report getReportByID(int reportID) {
        for (Report report : reports) {
            if (report.getReportID() == reportID) {
                return report;
            }
        }
        return null;
    }

    public void addReport(Report report) {
        reports.add(report);
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
