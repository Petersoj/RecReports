package me.petersoj.report;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class ReportPlayer {

    private UUID uuid;
    private String playerName;
    private String skinProfileValue;
    private String skinProfileSignature;

    public ReportPlayer(UUID uuid, String playerName) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.skinProfileValue = "";
        this.skinProfileSignature = "";
    }

    public ReportPlayer(UUID uuid, String playerName, String skinProfileValue, String skinProfileSignature) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.skinProfileValue = skinProfileValue;
        this.skinProfileSignature = skinProfileSignature;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
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

    public String getSkinProfileValue() {
        return skinProfileValue;
    }

    public void setSkinProfileValue(String skinProfileValue) {
        this.skinProfileValue = skinProfileValue;
    }

    public String getSkinProfileSignature() {
        return skinProfileSignature;
    }

    public void setSkinProfileSignature(String skinProfileSignature) {
        this.skinProfileSignature = skinProfileSignature;
    }
}
