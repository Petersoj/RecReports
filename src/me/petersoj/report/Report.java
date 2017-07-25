package me.petersoj.report;

public class Report {

    private RecReportsPlayer reporter;
    private RecReportsPlayer victim;
    private String reportReason;

    public Report(RecReportsPlayer reporter, RecReportsPlayer victim, String reportReason) {
        this.reporter = reporter;
        this.victim = victim;
        this.reportReason = reportReason;
    }

    public RecReportsPlayer getReporter() {
        return reporter;
    }

    public RecReportsPlayer getVictim() {
        return victim;
    }

    public String getReportReason() {
        return reportReason;
    }

}
