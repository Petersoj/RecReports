package me.petersoj.util.gson.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.HashMap;

public class EmptyHashMapAdapter implements JsonSerializer<HashMap<?, ?>> {

    @Override
    public JsonElement serialize(HashMap<?, ?> map, Type type, JsonSerializationContext context) {
        if (!map.isEmpty()) {
            return context.serialize(map, type);
        }
        return null;
    }
}
