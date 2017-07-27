package me.petersoj.util;

import org.bukkit.Bukkit;

public class ExceptionUtils {

    public static void handleError(Exception e, boolean fatal) {
        e.printStackTrace();

        // Disable the plugin if error is fatal.
        if (fatal) {
            Bukkit.getPluginManager().disablePlugin(Bukkit.getPluginManager().getPlugin("RecReports"));
        }
//        Do this better later
//        String errorMessage = "An Error has occurred: ";
//        Bukkit.getLogger().log(Level.WARNING, errorMessage);
    }

}
