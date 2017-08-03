package me.petersoj.util.adapters;

import com.google.gson.*;
import me.petersoj.nms.RecordedPlayer;
import me.petersoj.record.RecordingPlayback;
import me.petersoj.report.ReportPlayer;

import java.lang.reflect.Type;

public class ReportPlayerAdapter implements JsonSerializer<ReportPlayer>, JsonDeserializer<ReportPlayer> {

    private RecordingPlayback deserializerRecordingPlayback;

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
            return context.deserialize(jsonElement, type);
        } else {
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

    /**
     * This method sets whether this instance should deserialize a ReportPlayer from and ID or not.
     *
     * @param deserializerRecordingPlayback null for complete object, object for deserialize from ID
     */
    public void setRecordingPlaybackInstance(RecordingPlayback deserializerRecordingPlayback) {
        this.deserializerRecordingPlayback = deserializerRecordingPlayback;
    }
}
