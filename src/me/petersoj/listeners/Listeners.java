package me.petersoj.listeners;

import me.petersoj.RecReportsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class Listeners implements Listener {

    private RecReportsPlugin plugin;

    public Listeners(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public void listen() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }


}
