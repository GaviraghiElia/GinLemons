package it.unimib.ginlemons.data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import it.unimib.ginlemons.ui.RicetteDiscoverFragment;
import it.unimib.ginlemons.utils.Ricetta;

public class FetchData extends Thread{

    private static final String TAG = "Fetch_Data";
    String data = "";

    @Override
    public void run() {
        try {
            URL url = new URL("https://www.thecocktaildb.com/api/json/v1/1/lookup.php?i=11007");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                data += line;
            }

            if(!data.isEmpty())
            {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray ricette = jsonObject.getJSONArray("drinks");

                for (int i = 0; i < ricette.length(); i++)
                {
                    JSONObject recipe = ricette.getJSONObject(i);
                    String nome = recipe.getString("strDrink");
                    String istruzioni = recipe.getString("strInstructionsIT");
                    String[] ingredienti = new String[15];
                    String[] dosi = new String[15];

                    Log.d(TAG, istruzioni);

                    for (int k = 1; k <= 15; k++)
                    {
                        ingredienti[k - 1] = recipe.getString("strIngredient" + k);
                        dosi[k - 1] = recipe.getString("strMeasure" + k);

                        Log.d(TAG, "Ingrediente " + k + ": " + ingredienti[k - 1]);
                        Log.d(TAG, "Dose " + k + ": " + dosi[k - 1]);
                    }

                    Ricetta ricetta = new Ricetta(nome, 10, 2, istruzioni, ingredienti, dosi);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
