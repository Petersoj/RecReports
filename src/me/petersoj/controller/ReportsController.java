package me.petersoj.controller;

import me.petersoj.RecReportsPlugin;

/**
 * This singleton controller class is responsible for holding current ReportFolders
 * and saving the data within them.
 */

public class ReportsController {

    private RecReportsPlugin plugin;

    public ReportsController(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }
}
