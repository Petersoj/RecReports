package me.petersoj.report;

import com.google.gson.reflect.TypeToken;
import me.petersoj.controller.FileController;
import me.petersoj.util.JsonUtils;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This class represents the folder in the data directory that holds
 * all the information about reports and recordings on a specific player.
 */
public class ReportsFolder {

    private FileController fileController;

    private File reportsFolder;
    private ReportSummary reportSummary;
    private ArrayList<Report> reports;
    private Type reportsListType;

    public ReportsFolder(FileController fileController, File reportsFolder) {
        this.fileController = fileController;
        this.reportsFolder = reportsFolder;
        this.reportsListType = new TypeToken<ArrayList<Report>>() { // Anonymous for some generic speciality.
        }.getType();
    }

    public void saveMetadata() {
        String summaryJson = JsonUtils.getGson().toJson(reportSummary);
        fileController.saveSmallFile(reportsFolder, "summary.txt", summaryJson);

        String reportsJson = JsonUtils.getGson().toJson(reports, reportsListType);
        fileController.saveSmallFile(reportsFolder, "reports.txt", reportsJson);
    }

    public void readMetadata() {
        String summaryJson = fileController.readFileFully(reportsFolder, "summary.txt");
        this.reportSummary = JsonUtils.getGson().fromJson(summaryJson, ReportSummary.class);

        String reportsJson = fileController.readFileFully(reportsFolder, "reports.txt");
        this.reports = JsonUtils.getGson().fromJson(reportsJson, reportsListType);
    }
}
