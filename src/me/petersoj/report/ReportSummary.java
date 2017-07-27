package me.petersoj.report;

/**
 * This class is just a data holder for Summary.txt and Gson will do the [de]serializing.
 */
public class ReportSummary {

    private String playerName;
    private int totalReports;
    private int totalRecordings;
    private String mostReportedType;
    private long lastReportTime;
    private long firstReportTime;
    private long lastReviewedTime;
    private int timesReviewed;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public long getFirstReportTime() {
        return firstReportTime;
    }

    public void setFirstReportTime(long firstReportTime) {
        this.firstReportTime = firstReportTime;
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
