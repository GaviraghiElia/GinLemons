package it.unimib.ginlemons.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RicetteDeserializer implements JsonDeserializer<Ricetta> {

    @Override
    public Ricetta deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Ricetta recipe = null;

        Gson gson = new Gson();
        Ricetta response = gson.fromJson(json, Ricetta.class);

        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonArray arrayricetteJSON = jsonObject.get("drinks").getAsJsonArray();

        for (JsonElement ricettaJSON : arrayricetteJSON) {
            final JsonObject ricetta = ricettaJSON.getAsJsonObject();

            Log.d("Test", ricetta.get("idDrink").getAsString());

            String nome = ricetta.get("strDrink").getAsString();
            String istruzioni = ricetta.get("strInstructionsIT").getAsString();
            String[] ingredienti = new String[15];
            String[] dosi = new String[15];

            for (int k = 1; k <= 15; k++)
            {
                if(ricetta.get("strIngredient" + k).isJsonNull())
                    ingredienti[k - 1] = null;
                else
                    ingredienti[k - 1] = ricetta.get("strIngredient" + k).getAsString();

                if(ricetta.get("strMeasure" + k).isJsonNull())
                    dosi[k - 1] = null;
                else
                    dosi[k - 1] = ricetta.get("strMeasure" + k).getAsString();
            }

            recipe = new Ricetta(nome, 10, 2, istruzioni, ingredienti, dosi);
        }

        return recipe;
    }
}
