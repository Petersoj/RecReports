package me.petersoj.record.adapters;

import com.google.gson.*;
import me.petersoj.nms.RecordedPlayer;
import me.petersoj.record.RecordingPlayback;
import me.petersoj.report.ReportPlayer;

import java.lang.reflect.Type;

public class ReportPlayerIDAdapter implements JsonSerializer<ReportPlayer>, JsonDeserializer<ReportPlayer> {

    private RecordingPlayback deserializerRecordingPlayback;

    /**
     * This constructor is used for Gson when serializing ReportPlayer ID's
     */
    public ReportPlayerIDAdapter() {
    }

    /**
     * This constructor is used for Gson when deserializing ReportPlayers from ID's
     *
     * @param deserializerRecordingPlayback the current RecordingPlayback object
     */
    public ReportPlayerIDAdapter(RecordingPlayback deserializerRecordingPlayback) {
        this.deserializerRecordingPlayback = deserializerRecordingPlayback;
    }

    @Override
    public JsonElement serialize(ReportPlayer reportPlayer, Type type, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", reportPlayer.getPlayerID());
        return jsonObject;
    }

    @Override
    public ReportPlayer deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(jsonElement instanceof JsonObject)) {
            throw new JsonParseException("Element must be an object!");
        }
        if (deserializerRecordingPlayback == null) {
            throw new NullPointerException("The RecordingPlayback cannot be null!");
        }

        JsonObject object = (JsonObject) jsonElement;
        int id = object.getAsJsonPrimitive("id").getAsInt();

        for (RecordedPlayer recordedPlayer : deserializerRecordingPlayback.getRecordedPlayers()) {
            if (recordedPlayer.getReportPlayer().getPlayerID() == id) {
                return recordedPlayer.getReportPlayer();
            }
        }
        return null;
    }
}
