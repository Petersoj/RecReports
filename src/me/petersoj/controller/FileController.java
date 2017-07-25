package me.petersoj.controller;

import me.petersoj.RecReportsPlugin;

import java.io.File;

/**
 * This singleton controller class is simply used for reading and writing to files
 * in the data directory for this plugin.
 */

public class FileController {

    private RecReportsPlugin plugin;

    private File dataFolder;

    public FileController(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public void start() {
        plugin.saveDefaultConfig(); // Copies the config file if it doesn't exist

        this.setupDataFolder();
    }

    private void setupDataFolder() {
        this.dataFolder = new File(plugin.getDataFolder(), "/ReportData");

        if (!dataFolder.exists()) {
            dataFolder.mkdir(); // Creates the ReportData directory
        }
    }

    public File getDataFolder() {
        return dataFolder;
    }

}
