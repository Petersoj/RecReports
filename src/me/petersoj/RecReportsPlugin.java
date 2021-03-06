package me.petersoj;

import me.petersoj.controller.FileController;
import me.petersoj.controller.NMSController;
import me.petersoj.controller.RecordingController;
import me.petersoj.controller.ReportsController;
import me.petersoj.listeners.Commands;
import me.petersoj.listeners.Listeners;
import me.petersoj.util.DebugUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class RecReportsPlugin extends JavaPlugin implements Listener {

    private FileController fileController;
    private NMSController nmsController;
    private ReportsController reportsController;
    private RecordingController recordingController;

    private Listeners listeners;
    private Commands commands;

    @Override
    public void onEnable() {
        // Set Utils instances up
        DebugUtils.setPluginInstance(this);

        // Create NMSController for version check
        this.nmsController = new NMSController(this);

        if (!nmsController.setupNMSVersion()) {
            return; // Don't do the rest of onEnable()
        }

        Bukkit.getPluginManager().registerEvents(this, this);

        // Init other Controllers
        this.fileController = new FileController(this);
        this.reportsController = new ReportsController(this);
        this.recordingController = new RecordingController(this);

        // Init Listening
        this.listeners = new Listeners(this);
        this.commands = new Commands(this);

        // Start/setup controllers and listeners
        this.fileController.start();
        this.listeners.listen();
    }

    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {
        if (e.getMessage().equals("/testSign")) {
//            nmsController.getNMSHandler().openSignInterface(e.getPlayer(), new String[]{"hey", "hey", "hey", "hey"}, new String[]{"hey3", "hey", "hey", "hey"}, 20);
        }
//        if (e.getMessage().equals("/money")) {
//            String sig = "ItTvKVTrxs7j8HX4BY8ulIQ+y1Y4Aud22XM4TJhbzNEcBLR9gnnBzqs9RC5zZIKHbtQH9HnsFvVwCgQJeLpnyfZlewlkNJB8qrni0Ck3fEbQTgixzSifJSLMuxRJrvl6wxhCtCs3maptEeQMgsNTp9pLbjxlw/vZMKv0qkzcyTygvE4qO49GURW1Up8u3qCsAvvRwPpMqJdqio66yj7jM8sjiDY47F2h/wZfWDTYyuiV0b/DXxOEIXbkabnGiJNH2Y5qM94QYgEjpFXn5H7pLqUnvUkb3C5T83rfvVBrif4JawMccL/ZOAZKtSbK5NHTG9GAqIzB8kZdZ5qNsJINPuP58zEGVGA61qZxlB/uDmF3Wqj3oo93j1HQMQ9mJIMNsCf76RJiJCvPeoPQg16CtJvaJPNmzYdXAz4D7l2ha6z27Ktqx6mCiX6PniLm+32Rxk/fubHTMhSC1PIOT0Gs585Obim8QO3Cz93i7jLCRLZX6QIkxTfhhAl3SFj+4lt+xMPAEFFkFCQPgXFfV2pIBWkABRx4f/Ni24Wdxl8KpyVorxI2cNG4kXiJ5bKBfMGdV/EHLxweyDwwNq0g88EISSOapmgJbUrKRJ1ZA45QSMIgpgRBc4n0CdBqEo2JFv8jVNI6OKwo9zXS/GZJvLEztpK4JeLcGoV8aoiVFQmRESA=";
//            String value = "eyJ0aW1lc3RhbXAiOjE1MDEyNzQ3NzYyMDgsInByb2ZpbGVJZCI6IjRkYmZlZmM4NjJkMDRhNDA4Mzk5YmQyODM3MjUyNjgyIiwicHJvZmlsZU5hbWUiOiJQZXRlcnNv" +
//                    "aiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTllMz"
//                    + "M4ZWZlY2Q4NjlmNmVlMjIzMDVjNTk1MDc1YzFkNTI4NTZhODE3OGE5YjdkNDJmNTNlODk4NzI5NWQifX19";
//            ReportPlayer reportPlayer = new ReportPlayer(0, e.getPlayer().getUniqueId(), "Petersoj", value, sig);
//            RecordedPlayerv1_8_8 recordedPlayer = new RecordedPlayerv1_8_8(this, reportPlayer, e.getPlayer());
//            recordedPlayer.spawn(e.getPlayer().getLocation());
//
//            new BukkitRunnable() {
//                int index = 0;
//
//                @Override
//                public void run() {
//                    recordedPlayer.moveTo(e.getPlayer().getLocation().add(1, 0, 0));
//                    if (index % 20 == 0) {
//                        recordedPlayer.teleport(e.getPlayer().getLocation().add(1, 0, 0));
//                    }
//                    index++;
//
//                    if (index % 60 == 0) {
//                        recordedPlayer.setSneaking(true);
//                    } else if (index % 90 == 0) {
//                        recordedPlayer.setSneaking(false);
//                    }
//                }
//            }.runTaskTimer(this, 0, 0);
//        }
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

    public ReportsController getReportsController() {
        return reportsController;
    }

    public NMSController getNMSController() {
        return nmsController;
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
