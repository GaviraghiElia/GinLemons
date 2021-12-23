package it.unimib.ginlemons.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.unimib.ginlemons.service.RecipeApiService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Classe che inizializza il client Retrofit con cui si effettuano le chiamate all'API
public class ServiceLocator {
    private static ServiceLocator instance = null;

    private ServiceLocator(){}

    public static ServiceLocator getInstance() {
        if (instance == null) {
            synchronized(ServiceLocator.class) {
                instance = new ServiceLocator();
            }
        }
        return instance;
    }

    // Inizializzazione per la chiamata che riceve le informazioni dettagliate di un cocktail
    public RecipeApiService getRecipeApiService() {
        Gson builder = new GsonBuilder().registerTypeAdapter(Ricetta.class, new RicetteDeserializer()).create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.thecocktaildb.com/api/json/v1/1/").addConverterFactory(GsonConverterFactory.create(builder)).build();
        return retrofit.create(RecipeApiService.class);
    }

    // Inizializzazione per la chiamata che riceve gli Ids dei cocktails Alcolici o Analcolici
    public RecipeApiService fetchRecipesApiService() {
        Gson builder = new GsonBuilder().registerTypeAdapter(IdList.class, new IdsDeserializer()).create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.thecocktaildb.com/api/json/v1/1/").addConverterFactory(GsonConverterFactory.create(builder)).build();
        return retrofit.create(RecipeApiService.class);
    }
}
