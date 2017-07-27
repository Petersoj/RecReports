package me.petersoj.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonUtils {

    private static final JsonParser jsonParser = new JsonParser(); // So I don't have to create a new parser every time.
    private static final Gson gson = new Gson(); // So I don't have to create a new Gson instance every time.

    public static JsonParser getJsonParser() {
        return jsonParser;
    }

    public static Gson getGson() {
        return gson;
    }

    public static JsonObject getNewJsonObject(String json) {
        return jsonParser.parse(json).getAsJsonObject();
    }
}
