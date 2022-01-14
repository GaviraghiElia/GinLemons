package it.unimib.ginlemons.repository;

import android.app.Application;
import android.content.res.Resources;

import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.service.RecipeApiService;
import it.unimib.ginlemons.utils.RicetteList;
import it.unimib.ginlemons.utils.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Classe che si occupa dell'interazione con l'API
public class RecipeRepository implements IRecipeRepository{

    private static final String TAG = "NewsRepository";
    private final RecipeApiService fetchApiService;
    private MutableLiveData<RicetteList> alcolici;
    private MutableLiveData<RicetteList> analcolici;

    public RecipeRepository(Application application) {
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
                        alcolici.getValue().setError(Resources.getSystem().getString(R.string.load_fail));
                    else
                        analcolici.getValue().setError(Resources.getSystem().getString(R.string.load_fail));
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
}
