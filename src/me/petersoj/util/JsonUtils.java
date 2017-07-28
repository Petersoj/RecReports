package me.petersoj.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.petersoj.util.adapters.LocationAdapter;
import org.bukkit.Location;

public class JsonUtils {

    private static final JsonParser jsonParser = new JsonParser(); // So I don't have to create a new parser every time.
    private static Gson gson; // So I don't have to create a new Gson instance every time.


    public static JsonParser getJsonParser() {
        return jsonParser;
    }

    public static Gson getGson() { // Not thread safe! But eh.
        if (gson == null) {
            gson = configureGson(new GsonBuilder());
        }
        return gson;
    }

    public static JsonObject getNewJsonObject(String json) {
        return jsonParser.parse(json).getAsJsonObject();
    }

    private static Gson configureGson(GsonBuilder gsonBuilder) {

        // Location adapter so org.bukkit.World doesn't get serialized :)
        gsonBuilder.registerTypeAdapter(Location.class, new LocationAdapter());

        return gsonBuilder.create();
    }
}
