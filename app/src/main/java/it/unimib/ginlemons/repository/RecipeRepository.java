package it.unimib.ginlemons.repository;

import android.app.Application;
import android.util.Log;

import java.util.List;

import it.unimib.ginlemons.service.RecipeApiService;
import it.unimib.ginlemons.utils.ListaRicette;
import it.unimib.ginlemons.utils.ResponseCallback;
import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository implements IRecipeRepository{

    private static final String TAG = "NewsRepository";
    private final RecipeApiService recipeApiService;
    private final ResponseCallback responseCallback;

    public RecipeRepository(Application application, ResponseCallback responseCallback) {
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
        this.responseCallback = responseCallback;
    }

    @Override
    public void fetchRecipes() {

        Call<ListaRicette> getRecipesCall = recipeApiService.getRecipes("margarita");


        getRecipesCall.enqueue(new Callback<ListaRicette>() {
            @Override
            public void onResponse(Call<ListaRicette> call, Response<ListaRicette> response) {
                if (response.body() != null && response.isSuccessful()) {
                    List<Ricetta> recipes = response.body().getRecipes();

                    responseCallback.onResponse(recipes);
                } else {
                    responseCallback.onFailure("Caricamento Fallito");
                }
            }

            @Override
            public void onFailure(Call<ListaRicette> call, Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }
}
