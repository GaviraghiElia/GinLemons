package it.unimib.ginlemons.utils;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class IdsDeserializer implements JsonDeserializer<IdList> {

    @Override
    public IdList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonArray arrayricetteJSON = jsonObject.get("drinks").getAsJsonArray();
        String[] ids = new String[arrayricetteJSON.size()];
        int i = 0;

        for (JsonElement ricettaJSON : arrayricetteJSON) {
            final JsonObject ricetta = ricettaJSON.getAsJsonObject();

            ids[i] = ricetta.get("idDrink").getAsString();
            i++;
        }

        return new IdList(ids);
    }
}
