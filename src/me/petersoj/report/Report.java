package me.petersoj.report;

/**
 * The Report class represents a report generated by a user
 * using the "/report" command.
 */
public class Report {

    private int reportID; // ReportID is used for referencing this report within a recording.
    private String reporterName;
    private String reportReason;

    public Report(int reportID, String reporterName, String reportReason) {
        this.reportID = reportID;
        this.reporterName = reporterName;
        this.reportReason = reportReason;
    }

    public int getReportID() {
        return reportID;
    }

    public String getReporterName() {
        return reporterName;
    }

    public String getReportReason() {
        return reportReason;
    }
}
