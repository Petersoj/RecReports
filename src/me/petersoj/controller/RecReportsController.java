package me.petersoj.controller;

import me.petersoj.RecReportsPlugin;
import me.petersoj.view.ReportsView;

/**
 * This singleton controller class is used for creating and viewing reports.
 */

public class RecReportsController {

    private RecReportsPlugin plugin;

    private ReportsView reportsView;

    public RecReportsController(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {

    }

}
