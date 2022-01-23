package it.unimib.ginlemons.repository;

import android.app.Application;
import android.content.res.Resources;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.service.RecipeApiService;
import it.unimib.ginlemons.utils.ResponseCallback;
import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetRecipeRepository implements IGetRecipeRepository{

    private final RecipeApiService recipeApiService;
    private final ResponseCallback responseCallback;
    Ricetta recipe;

    public GetRecipeRepository(Application application, ResponseCallback responseCallback)
    {
        // Endpoint per richiedere le informazioni di un cocktail
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();

        this.responseCallback = responseCallback;
    }


    // Metodo che ottiene i dati dettagliati del cocktail con l'Id passato come parametro
    @Override
    public Ricetta getRecipeById(String id)
    {
        Call<Ricetta> getRecipesCall = recipeApiService.getRecipeById(id);
        getRecipesCall.enqueue(new Callback<Ricetta>()
        {
            @Override
            public void onResponse(Call<Ricetta> call, Response<Ricetta> response)
            {
                if (response.body() != null && response.isSuccessful())
                {
                    Ricetta recipe = response.body();
                    responseCallback.onResponse(recipe);
                }
                else
                    responseCallback.onFailure(Resources.getSystem().getString(R.string.load_fail));
            }

            @Override
            public void onFailure(Call<Ricetta> call, Throwable t)
            {
                responseCallback.onFailure(t.getMessage());
            }
        });

        return recipe;
    }
}
