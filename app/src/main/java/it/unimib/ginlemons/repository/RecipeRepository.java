package it.unimib.ginlemons.repository;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

import it.unimib.ginlemons.service.RecipeApiService;
import it.unimib.ginlemons.utils.IdList;
import it.unimib.ginlemons.utils.ResponseCallback;
import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.RicetteList;
import it.unimib.ginlemons.utils.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Classe che si occupa dell'interazione con l'API
public class RecipeRepository implements IRecipeRepository{

    private static final String TAG = "NewsRepository";
    private final RecipeApiService recipeApiService;
    private final RecipeApiService fetchApiService;
    private final ResponseCallback responseCallback;

    public RecipeRepository(Application application, ResponseCallback responseCallback) {
        // Endpoint per richiedere le informazioni di un cocktail
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
        // Endpoint per richiedere i cocktail Alcolici o Analcolici
        this.fetchApiService = ServiceLocator.getInstance().fetchRecipesApiService();
        this.responseCallback = responseCallback;
    }

    // Metodo che ottiene i dati dettagliati del cocktail con l'Id passato come parametro
    @Override
    public void getRecipeById(String id) {
        Call<Ricetta> getRecipesCall = recipeApiService.getRecipeById(id);

        getRecipesCall.enqueue(new Callback<Ricetta>() {
            @Override
            public void onResponse(Call<Ricetta> call, Response<Ricetta> response) {
                if (response.body() != null && response.isSuccessful()) {
                    Ricetta recipe = response.body();

                    responseCallback.onResponse(recipe);
                } else {
                    responseCallback.onFailure("Caricamento Fallito");
                }
            }

            @Override
            public void onFailure(Call<Ricetta> call, Throwable t) {
                    responseCallback.onFailure(t.getMessage());
            }
        });
    }

    // Metodo che ottiene gli Id dei cocktail Alcolici o Analcolici a seconda del parametro Type
    // Il parametro clear indica se la lista delle ricette va cancellata o no prima di inserire i nuovi drink
    @Override
    public void fetchRecipes(String type, boolean clear) {
        Call<RicetteList> getRecipeCall = fetchApiService.fetchRecipes(type);

        getRecipeCall.enqueue(new Callback<RicetteList>() {
            @Override
            public void onResponse(Call<RicetteList> call, Response<RicetteList> response) {
                if (response.body() != null && response.isSuccessful()) {

                    ArrayList<Ricetta> ricette = response.body().getRepices();
                    responseCallback.onResponse(ricette, clear);
                } else {
                    responseCallback.onFailure("Caricamento Fallito");
                }
            }

            @Override
            public void onFailure(Call<RicetteList> call, Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }
}
