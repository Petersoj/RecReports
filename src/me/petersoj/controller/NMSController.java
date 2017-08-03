package me.petersoj.controller;

import me.petersoj.RecReportsPlugin;
import me.petersoj.nms.NMSHandler;
import me.petersoj.nms.NMSVersion;
import me.petersoj.nms.handlers.NMSHandlerv1_12;
import me.petersoj.nms.handlers.NMSHandlerv1_8_8;

public class NMSController {

    private RecReportsPlugin plugin;

    private NMSHandler nmsHandler;

    public NMSController(RecReportsPlugin plugin) {
        this.plugin = plugin;

        this.setupNMS();
    }

    private void setupNMS() {
        if (NMSVersion.isVersion(NMSVersion.V1_8_8)) {
            this.nmsHandler = new NMSHandlerv1_8_8(plugin);
        } else if (NMSVersion.isVersion(NMSVersion.V1_12)) {
            this.nmsHandler = new NMSHandlerv1_12(plugin);
        }
    }

    public NMSHandler getNMSHandler() {
        return nmsHandler;
    }
}
