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
        if (pluginInstance.getFileController().isLoggingEnabled()) {
            Bukkit.getLogger().log(Level.WARNING, message);
        }
    }

    public static void logInfo(String message) {
        if (pluginInstance.getFileController().isLoggingEnabled()) {
            Bukkit.getLogger().log(Level.INFO, message);
        }
    }

}