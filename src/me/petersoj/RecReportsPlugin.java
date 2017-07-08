package me.petersoj;

import me.petersoj.controller.RecReportsController;
import me.petersoj.listeners.Commands;
import me.petersoj.listeners.Listeners;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class RecReportsPlugin extends JavaPlugin {

    private RecReportsController recReportsController;
    private Listeners listeners;
    private Commands commands;


    @Override
    public void onEnable() {
        this.recReportsController = new RecReportsController(this);
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);

        this.listeners.listen();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commands.onCommand(sender, command, label, args);
    }
}
