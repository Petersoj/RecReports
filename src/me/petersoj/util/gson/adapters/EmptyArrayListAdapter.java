package me.petersoj.util.gson.adapters;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class EmptyArrayListAdapter implements JsonSerializer<ArrayList<?>> {

    @Override
    public JsonElement serialize(ArrayList<?> arrayList, Type type, JsonSerializationContext context) {
        if (!arrayList.isEmpty()) {
            return context.serialize(arrayList, type);
        }
        return null;
    }
}
