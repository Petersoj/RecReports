package me.petersoj.report;

import java.util.ArrayList;

/**
 * This class is just a data holder for info.txt and Gson will do the [de]serializing.
 */
public class ReportInfo {

    private final ReportPlayer reportedPlayer = new ReportPlayer(0, null, ""); // Default ReportPlayer, ID is always 0 for reportedPlayer.
    private final ArrayList<String> recordingFileNames = new ArrayList<>();
    private int totalReports;
    private int totalRecordings;
    private String mostReportedType = "N/A"; // N/A for default
    private long lastReportTime;
    private long lastReviewedTime;
    private int timesReviewed;

    public ReportPlayer getReportedPlayer() {
        return reportedPlayer;
    }

    public ArrayList<String> getRecordingFileNames() {
        return recordingFileNames;
    }

    public void addRecordingFileName(String name) {
        this.recordingFileNames.add(name);
    }

    public int getTotalReports() {
        return totalReports;
    }

    public void incrementTotalReports(int increment) {
        this.totalReports += increment;
    }

    public int getTotalRecordings() {
        return totalRecordings;
    }

    public void incrementTotalRecordings(int increment) {
        this.totalRecordings += increment;
    }

    public String getMostReportedType() {
        return mostReportedType;
    }

    public void setMostReportedType(String mostReportedType) {
        this.mostReportedType = mostReportedType;
    }

    public long getLastReportTime() {
        return lastReportTime;
    }

    public void setLastReportTime(long lastReportTime) {
        this.lastReportTime = lastReportTime;
    }

    public long getLastReviewedTime() {
        return lastReviewedTime;
    }

    public void setLastReviewedTime(long lastReviewedTime) {
        this.lastReviewedTime = lastReviewedTime;
    }

    public int getTimesReviewed() {
        return timesReviewed;
    }

    public void incrementTimesReviewed(int increment) {
        this.timesReviewed += increment;
    }
}
