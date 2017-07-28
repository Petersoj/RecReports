package me.petersoj;

import me.petersoj.controller.FileController;
import me.petersoj.controller.RecReportsController;
import me.petersoj.controller.RecordingController;
import me.petersoj.controller.ReportsController;
import me.petersoj.listeners.Commands;
import me.petersoj.listeners.Listeners;
import me.petersoj.util.LogUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class RecReportsPlugin extends JavaPlugin {

    private RecReportsController recReportsController;
    private ReportsController reportsController;
    private RecordingController recordingController;
    private FileController fileController;

    private Listeners listeners;
    private Commands commands;

    @Override
    public void onEnable() {
        // Set Utils instances up
        LogUtils.setPluginInstance(this);

        // Init Controllers
        this.recReportsController = new RecReportsController(this);
        this.reportsController = new ReportsController();
        this.recordingController = new RecordingController();
        this.fileController = new FileController(this);

        // Init Listening
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);

        // Start controllers and listeners
        this.recReportsController.start();
        this.fileController.start();
        this.listeners.listen();
    }

    @Override
    public void onDisable() {
        // Close current Recordings
    }

    // So I don't have to set every commands' executor
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return commands.onCommand(sender, command, label, args);
    }

    public RecReportsController getRecReportsController() {
        return recReportsController;
    }

    public ReportsController getReportsController() {
        return reportsController;
    }

    public RecordingController getRecordingController() {
        return recordingController;
    }

    public FileController getFileController() {
        return fileController;
    }

    public Listeners getListeners() {
        return listeners;
    }
}
