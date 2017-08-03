package me.petersoj.util.adapters;

import com.google.gson.*;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

/**
 * This class exists so that certain data within ItemStack does not get serialized.
 */
public class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

//    Only necessary if serializing the entire ItemStack using itemStack.serialize();
//    private static final Type itemStackSerializedmap = new TypeToken<LinkedHashMap<String, Object>>() {
//    }.getType();

    @Override
    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext context) {
        if (itemStack == null) {
            throw new NullPointerException("ItemStack cannot be null!");
        }

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("type", itemStack.getType().toString()); // Add material enum string.
        jsonObject.addProperty("dura", itemStack.getData().getData()); // The data value can change the look of the itemstack.
        jsonObject.addProperty("dura", itemStack.getDurability()); // In some cases, the durability determines the data.
        jsonObject.addProperty("ench", itemStack.getEnchantments().size() > 0); // If the item has any enchantments, then show them.

        return jsonObject;
    }

    @Override
    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }
}
