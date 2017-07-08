package me.petersoj.controller;

import me.petersoj.RecReportsPlugin;

/**
 * This singleton controller class is used for creating and viewing reports.
 */

public class RecReportsController {

    private RecReportsPlugin plugin;

    public RecReportsController(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }

}
