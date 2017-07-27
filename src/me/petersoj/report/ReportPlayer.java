package me.petersoj.report;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class ReportPlayer {

    private UUID uuid;
    private String playerName;

    public ReportPlayer(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
    }

    public OfflinePlayer getOfflinePlayer() {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(uuid);
        if (offlinePlayer.hasPlayedBefore()) {
            return offlinePlayer;
        }
        return null;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
