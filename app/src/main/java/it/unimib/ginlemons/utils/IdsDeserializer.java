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

// Custom deserializer per convertire il JSON ricevuto dall'API
// Estraggo le informazioni dettagliate dei cocktails dagli oggetti JSON ricevuti
public class IdsDeserializer implements JsonDeserializer<RicetteList> {

    @Override
    public RicetteList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        final JsonObject jsonObject = json.getAsJsonObject();
        final JsonArray arrayricetteJSON = jsonObject.get("drinks").getAsJsonArray();

        ArrayList<Ricetta> recipes = new ArrayList<>();
        String id;
        String nome;

        for (JsonElement ricettaJSON : arrayricetteJSON) {
            final JsonObject ricetta = ricettaJSON.getAsJsonObject();

            id = ricetta.get("idDrink").getAsString();
            nome = ricetta.get("strDrink").getAsString();

            recipes.add(new Ricetta(id, nome));
        }

        return new RicetteList(recipes);
    }
}
