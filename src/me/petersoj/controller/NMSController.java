package me.petersoj.controller;

import me.petersoj.RecReportsPlugin;
import me.petersoj.nms.NMSHandler;
import me.petersoj.nms.handlers.NMSHandlerv1_12;
import me.petersoj.nms.handlers.NMSHandlerv1_8_8;
import me.petersoj.util.DebugUtils;
import org.bukkit.Bukkit;

public class NMSController {

    public static final String V1_8_8 = "1.8.8";
    public static final String V1_12 = "1.12";
    private static String version = null;

    private RecReportsPlugin plugin;

    private NMSHandler nmsHandler;

    public NMSController(RecReportsPlugin plugin) {
        this.plugin = plugin;

        this.setup();
    }

    private void setup() {
        if (isVersion(V1_8_8)) {
            this.nmsHandler = new NMSHandlerv1_8_8(plugin);
        } else if (isVersion(V1_12)) {
            this.nmsHandler = new NMSHandlerv1_12(plugin);
        }
    }

    /**
     * This sets the version for NMS handling and other NMS related things.
     *
     * @return if the version was allowed
     */
    public boolean setupNMSVersion() {
        String bukkitVersion = Bukkit.getVersion();

        if (bukkitVersion.contains("1.8.8")) {
            version = V1_8_8;
        } else if (bukkitVersion.contains("1.12")) {
            version = V1_12;
        } else {
            DebugUtils.logWarn("YOU MUST USE 1.8.8 OR 1.12");
            DebugUtils.logWarn("Bad version Error", true);
            return false;
        }
        return true;
    }

    public static boolean isVersion(String v) {
        return version == v;
    }

    public NMSHandler getNMSHandler() {
        return nmsHandler;
    }
}
