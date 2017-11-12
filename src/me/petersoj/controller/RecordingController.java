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

        this.setup();
    }

    private void setup() {
        Frame.MAX_FRAME_COUNT = plugin.getFileController().getRecordingTime() * 60 * 20; // set the max frame count for a recording.
    }

    public void start() {

    }
}
