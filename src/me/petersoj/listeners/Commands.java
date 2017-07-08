package me.petersoj.listeners;

import me.petersoj.RecReportsPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Commands {

    private RecReportsPlugin plugin;

    public Commands(RecReportsPlugin plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return true;
    }

}
