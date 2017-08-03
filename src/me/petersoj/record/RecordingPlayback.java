package me.petersoj.record;

import me.petersoj.nms.RecordedPlayer;

import java.util.ArrayList;

/**
 * This class will playback a previously saved recording and will aid
 * in the updating of the ReportViewingController.
 */
public class RecordingPlayback {

    public ArrayList<RecordedPlayer> recordedPlayers;

    public ArrayList<RecordedPlayer> getRecordedPlayers() {
        return recordedPlayers;
    }
}
