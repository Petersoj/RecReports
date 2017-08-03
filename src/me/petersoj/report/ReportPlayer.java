package me.petersoj.report;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

/**
 * This class represents a player's UUID, name, and skin data, as well as the ID for recordings.
 */
public class ReportPlayer {

    private int playerID; // The ID used for referencing this player in a recording.
    private UUID uuid;
    private String playerName;
    private String skinProfileValue;
    private String skinProfileSignature;

    public ReportPlayer(int playerID, UUID uuid, String playerName) {
        this.playerID = playerID;
        this.uuid = uuid;
        this.playerName = playerName;
        this.skinProfileValue = "";
        this.skinProfileSignature = "";
    }

    public ReportPlayer(int playerID, UUID uuid, String playerName, String skinProfileValue, String skinProfileSignature) {
        this.playerID = playerID;
        this.uuid = uuid;
        this.playerName = playerName;
        this.skinProfileValue = skinProfileValue;
        this.skinProfileSignature = skinProfileSignature;
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(uuid);
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public UUID getUUID() {
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
