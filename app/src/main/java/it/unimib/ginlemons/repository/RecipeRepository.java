package it.unimib.ginlemons.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import it.unimib.ginlemons.service.RecipeApiService;
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
    private MutableLiveData<RicetteList> alcolici;
    private MutableLiveData<RicetteList> analcolici;

    public RecipeRepository(Application application) {
        // Endpoint per richiedere le informazioni di un cocktail
        this.recipeApiService = ServiceLocator.getInstance().getRecipeApiService();
        // Endpoint per richiedere i cocktail Alcolici o Analcolici
        this.fetchApiService = ServiceLocator.getInstance().fetchRecipesApiService();

        this.alcolici = new MutableLiveData<>();
        this.analcolici = new MutableLiveData<>();
    }

    // Metodo che ottiene gli Id dei cocktail Alcolici o Analcolici a seconda del parametro Type
    // Il parametro clear indica se la lista delle ricette va cancellata o no prima di inserire i nuovi drink
    @Override
    public MutableLiveData<RicetteList> fetchRecipes(String type) {
        Call<RicetteList> getRecipeCall = fetchApiService.fetchRecipes(type);

        getRecipeCall.enqueue(new Callback<RicetteList>() {
            @Override
            public void onResponse(Call<RicetteList> call, Response<RicetteList> response) {
                if (response.body() != null && response.isSuccessful())
                {
                    if(type.equals("Alcoholic"))
                        alcolici.postValue(response.body());
                    else
                        analcolici.postValue(response.body());
                }
                else
                {
                    if(type.equals("Alcoholic"))
                        alcolici.getValue().setError("Caricamento Fallito");
                    else
                        analcolici.getValue().setError("Caricamento Fallito");
                }
            }

            @Override
            public void onFailure(Call<RicetteList> call, Throwable t) {
                if(type.equals("Alcoholic"))
                    alcolici.getValue().setError(t.getMessage());
                else
                    analcolici.getValue().setError(t.getMessage());
            }
        });

        if(type.equals("Alcoholic"))
            return alcolici;
        else
            return analcolici;
    }

    // Metodo che ottiene i dati dettagliati del cocktail con l'Id passato come parametro
    @Override
    public void getRecipeById(String id) {
        /*
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
         */
    }
}
