package me.petersoj.nms;

import me.petersoj.util.DebugUtils;
import org.bukkit.Bukkit;

public class NMSVersion {

    public static final String V1_8_8 = "1.8.8";
    public static final String V1_12 = "1.12";

    private static String version = null;

    /**
     * This sets the version for NMS handling.
     *
     * @return if the version was allowed
     */
    public static boolean setupVersion() {
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

    public static String getVersion() {
        return version;
    }
}
