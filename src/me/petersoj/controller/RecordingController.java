package me.petersoj.controller;

import me.petersoj.RecReportsPlugin;
import me.petersoj.record.Frame;

/**
 * This singleton controller class
 */

public class RecordingController {

    private RecReportsPlugin plugin;

    public RecordingController(RecReportsPlugin plugin) {
        this.plugin = plugin;

        this.setupRecorder();
    }

    private void setupRecorder() {
        int time = plugin.getFileController().getRecordingTime() * 60 * 20; // minutes * seconds in minute * ticks in second
        Frame.MAX_FRAME_COUNT = time; // set the max frame count for a recording.
    }

    public void start() {

    }
}
