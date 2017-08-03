package me.petersoj.util;

import me.petersoj.RecReportsPlugin;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class LogUtils {

    private static RecReportsPlugin pluginInstance;

    public static void setPluginInstance(RecReportsPlugin plugin) {
        pluginInstance = plugin;
    }

    public static void handleError(Exception e, boolean fatal) {
        e.printStackTrace();

        // Disable the plugin if error is fatal.
        if (fatal) {
            Bukkit.getPluginManager().disablePlugin(pluginInstance);
        }

//        Do this better later
//        String errorMessage = "An Error has occurred: ";
//        Bukkit.getLogger().log(Level.WARNING, errorMessage);
    }

    public static void logWarn(String message) {
        logWarn(message, false);
    }

    public static void logWarn(String message, boolean fatal) {
        if (pluginInstance.getFileController().isLoggingEnabled()) {
            Bukkit.getLogger().log(Level.WARNING, message);
        }

        if (fatal) {
            Bukkit.getLogger().log(Level.WARNING, "WARNING WAS FATAL! DISABLING PLUGIN");
            Bukkit.getPluginManager().disablePlugin(pluginInstance);
        }
    }

    public static void logInfo(String message) {
        if (pluginInstance.getFileController().isLoggingEnabled()) {
            Bukkit.getLogger().log(Level.INFO, message);
        }
    }

}
