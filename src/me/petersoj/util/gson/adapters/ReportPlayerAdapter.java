package me.petersoj.util.gson.adapters;

import com.google.gson.*;
import me.petersoj.nms.RecordedPlayer;
import me.petersoj.record.RecordingPlayback;
import me.petersoj.report.ReportPlayer;

import java.lang.reflect.Type;

public class ReportPlayerAdapter implements JsonSerializer<ReportPlayer>, JsonDeserializer<ReportPlayer> {

    private boolean serializeFullReportPlayer;
    private RecordingPlayback deserializerRecordingPlayback;

    @Override
    public JsonElement serialize(ReportPlayer reportPlayer, Type type, JsonSerializationContext context) {
        if (reportPlayer == null) {
            throw new NullPointerException("ReportPlayer cannot be null!");
        }

        if (serializeFullReportPlayer) {
            return context.serialize(reportPlayer, type);
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("pid", reportPlayer.getPlayerID());
            return jsonObject;
        }
    }

    @Override
    public ReportPlayer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(jsonElement instanceof JsonObject)) {
            throw new JsonParseException("Element must be an object!");
        }
        if (deserializerRecordingPlayback == null) {
            return context.deserialize(jsonElement, type);
        } else {
            JsonObject object = jsonElement.getAsJsonObject();
            int pid = object.getAsJsonPrimitive("pid").getAsInt();

            for (RecordedPlayer recordedPlayer : deserializerRecordingPlayback.getRecordedPlayers()) {
                if (recordedPlayer.getReportPlayer().getPlayerID() == pid) {
                    return recordedPlayer.getReportPlayer();
                }
            }
            return null;
        }
    }

    /**
     * This sets whether to serialize the entire reportplayer object or to just serialize the ID.
     *
     * @param serializeFullReportPlayer whether to serialize the entire reportplayer object
     */
    // synchronized just in case multiple threads need to change this when using Gson
    public synchronized void setSerializeFullReportPlayer(boolean serializeFullReportPlayer) {
        this.serializeFullReportPlayer = serializeFullReportPlayer;
    }

    /**
     * This method sets whether this instance should deserialize a ReportPlayer from an ID or not.
     *
     * @param deserializerRecordingPlayback null for complete object, object for deserialize from ID
     */
    // synchronized just in case multiple threads need to change this when using Gson
    public synchronized void setDeserializeFromID(RecordingPlayback deserializerRecordingPlayback) {
        this.deserializerRecordingPlayback = deserializerRecordingPlayback;
    }
}
