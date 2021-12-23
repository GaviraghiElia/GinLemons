package it.unimib.ginlemons.repository;

import android.app.Application;

import it.unimib.ginlemons.service.RecipeApiService;
import it.unimib.ginlemons.utils.IdList;
import it.unimib.ginlemons.utils.ResponseCallback;
import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRepository implements IRecipeRepository{

    private static final String TAG = "NewsRepository";
    private final RecipeApiService recipeApiService;
    private final RecipeApiService fetchApiService;
    private final ResponseCallback responseCallback;

    public RecipeRepository(Application application, ResponseCallback responseCallback) {
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
        this.fetchApiService = ServiceLocator.getInstance().fetchRecipesApiService();
        this.responseCallback = responseCallback;
    }

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

    @Override
    public void fetchRecipes(String type, boolean clear) {
        Call<IdList> getRecipeCall = fetchApiService.fetchRecipe(type);

        getRecipeCall.enqueue(new Callback<IdList>() {
            @Override
            public void onResponse(Call<IdList> call, Response<IdList> response) {
                if (response.body() != null && response.isSuccessful()) {
                    String[] ids = response.body().getIds();

                    responseCallback.onResponse(ids, clear);
                } else {
                    responseCallback.onFailure("Caricamento Fallito");
                }
            }

            @Override
            public void onFailure(Call<IdList> call, Throwable t) {
                responseCallback.onFailure(t.getMessage());
            }
        });
    }
}
