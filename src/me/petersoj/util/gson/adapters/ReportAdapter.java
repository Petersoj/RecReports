package me.petersoj.util.gson.adapters;

import com.google.gson.*;
import me.petersoj.report.Report;
import me.petersoj.report.ReportsFolder;

import java.lang.reflect.Type;

public class ReportAdapter implements JsonSerializer<Report>, JsonDeserializer<Report> {

    private boolean serializeFullReport;
    private ReportsFolder reportsFolder;

    @Override
    public JsonElement serialize(Report report, Type type, JsonSerializationContext context) {
        if (report == null) {
            throw new NullPointerException("Report cannot be null!");
        }

        if (serializeFullReport) {
            return context.serialize(report, type);
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("rid", report.getReportID());
            return jsonObject;
        }
    }

    @Override
    public Report deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        if (!(jsonElement instanceof JsonObject)) {
            throw new JsonParseException("Element must be an object!");
        }

        if (reportsFolder == null) {
            return context.deserialize(jsonElement, type);
        } else {
            JsonObject object = jsonElement.getAsJsonObject();
            int rid = object.getAsJsonPrimitive("rid").getAsInt();

            return reportsFolder.getReportByID(rid);
        }
    }

    /**
     * This sets whether to serialize the entire Report object or to just serialize the ID.
     *
     * @param serializeFullReport whether to serialize the entire Report object
     */
    // synchronized just in case multiple threads need to change this when using Gson
    public synchronized void setSerializeFullReport(boolean serializeFullReport) {
        this.serializeFullReport = serializeFullReport;
    }

    /**
     * This method sets whether this instance should deserialize a Report from an ID or not.
     *
     * @param reportsFolder null for complete object, object for deserialize from ID
     */
    // synchronized just in case multiple threads need to change this when using Gson
    public synchronized void setDeserializeFromID(ReportsFolder reportsFolder) {
        this.reportsFolder = reportsFolder;
    }
}
