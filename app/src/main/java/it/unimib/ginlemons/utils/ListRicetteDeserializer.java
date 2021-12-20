package it.unimib.ginlemons.utils;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListRicetteDeserializer implements JsonDeserializer<List<Ricetta>> {
    @Override
    public List<Ricetta> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        List<Ricetta> recipes = new ArrayList<Ricetta>();

        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonArray arrayricetteJSON = jsonObject.get("drinks").getAsJsonArray();

        for (JsonElement ricettaJSON : arrayricetteJSON) {
            final JsonObject ricetta = ricettaJSON.getAsJsonObject();

            String nome = ricetta.get("strDrink").getAsString();
            String istruzioni = ricetta.get("strInstructionsIT").getAsString();
            String[] ingredienti = new String[15];
            String[] dosi = new String[15];

            Log.d("Deserializer", istruzioni);

            for (int k = 1; k <= 15; k++)
            {
                ingredienti[k - 1] = ricetta.get("strIngredient" + k).getAsString();
                dosi[k - 1] = ricetta.get("strMeasure" + k).getAsString();

                Log.d("Deserializer", "Ingrediente " + k + ": " + ingredienti[k - 1]);
                Log.d("Deserializer", "Dose " + k + ": " + dosi[k - 1]);
            }

            recipes.add(new Ricetta(nome, 10, 2, istruzioni, ingredienti, dosi));
        }

        return recipes;
    }
}
