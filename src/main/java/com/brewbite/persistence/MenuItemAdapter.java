package com.brewbite.persistence;

import com.brewbite.model.Beverage;
import com.brewbite.model.MenuItem;
import com.brewbite.model.Pastry;

import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Custom Gson adapter for the abstract MenuItem class.
 *
 * Without this, Gson can't decide whether a serialized MenuItem should
 * deserialize as Beverage or Pastry. We add a "type" discriminator field
 * during serialization and use it to pick the concrete class on read.
 */
public class MenuItemAdapter
        implements JsonSerializer<MenuItem>, JsonDeserializer<MenuItem> {

    private static final String TYPE_FIELD = "_type";

    @Override
    public JsonElement serialize(MenuItem src, Type typeOfSrc,
                                  JsonSerializationContext context) {
        // Serialize the concrete class, then add a discriminator field.
        JsonElement element = context.serialize(src, src.getClass());
        if (element.isJsonObject()) {
            element.getAsJsonObject().addProperty(TYPE_FIELD, src.getClass().getSimpleName());
        }
        return element;
    }

    @Override
    public MenuItem deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context)
            throws JsonParseException {

        if (!json.isJsonObject()) {
            throw new JsonParseException("Expected MenuItem JSON object");
        }

        JsonObject obj = json.getAsJsonObject();
        if (!obj.has(TYPE_FIELD)) {
            throw new JsonParseException(
                    "Missing '" + TYPE_FIELD + "' field on MenuItem JSON");
        }

        String type = obj.get(TYPE_FIELD).getAsString();
        return switch (type) {
            case "Beverage" -> context.deserialize(obj, Beverage.class);
            case "Pastry"   -> context.deserialize(obj, Pastry.class);
            default ->
                    throw new JsonParseException("Unknown MenuItem subtype: " + type);
        };
    }
}
