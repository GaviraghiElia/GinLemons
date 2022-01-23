package it.unimib.ginlemons.repository;

import android.app.Application;
import android.content.res.Resources;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.database.RecipeRoomDatabase;
import it.unimib.ginlemons.database.RecipesDao;
import it.unimib.ginlemons.service.RecipeApiService;
import it.unimib.ginlemons.utils.Constants;
import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.RicetteList;
import it.unimib.ginlemons.utils.ServiceLocator;
import it.unimib.ginlemons.utils.SharedPreferencesProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Classe che si occupa dell'interazione con l'API
public class RecipeRepository implements IRecipeRepository
{
    private static final String TAG = "NewsRepository";

    private final RecipesDao recipesDao;

    private final RecipeApiService fetchApiService;
    private MutableLiveData<RicetteList> alcolici;
    private MutableLiveData<RicetteList> analcolici;

    private final SharedPreferencesProvider mSharedPreferencesProvider;

    public RecipeRepository(Application application)
    {
        // Endpoint per richiedere i cocktail Alcolici o Analcolici
        this.fetchApiService = ServiceLocator.getInstance().fetchRecipesApiService();

        RecipeRoomDatabase database = ServiceLocator.getInstance().getRecipesDao(application);
        this.recipesDao = database.recipeDao();

        this.alcolici = new MutableLiveData<>();
        this.analcolici = new MutableLiveData<>();

        mSharedPreferencesProvider = new SharedPreferencesProvider(application);
    }

    // Metodo che ottiene gli Id dei cocktail Alcolici o Analcolici a seconda del parametro Type
    // Il parametro clear indica se la lista delle ricette va cancellata o no prima di inserire i nuovi drink
    @Override
    public MutableLiveData<RicetteList> fetchRecipes(String type)
    {
        ExecutorService executor = Executors.newSingleThreadExecutor();

        Future<Integer> result = executor.submit(new Callable<Integer>() {
            public Integer call()
            {
                return recipesDao.isEmpty(type);
            }
        });

        try {
            if (result.get() == 0)
                getRecipesFromAPI(type);
            else
                getRecipesFromDatabase(type);

        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (type.equals("Alcoholic"))
                return alcolici;
            else
                return analcolici;
        }
    }

    private void saveDataInDatabase(List<Ricetta> recipeList, String type)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                recipesDao.deleteAll(type);

                recipesDao.insertRecipes(recipeList);
            }
        };

        new Thread(runnable).start();
    }

    public void getRecipesFromAPI(String type)
    {
        Call<RicetteList> getRecipeCall = fetchApiService.fetchRecipes(type);

        getRecipeCall.enqueue(new Callback<RicetteList>() {
            @Override
            public void onResponse(Call<RicetteList> call, Response<RicetteList> response)
            {
                if (response.body() != null && response.isSuccessful())
                {
                    RicetteList appo = response.body();

                    appo = setType(appo, type);

                    saveDataInDatabase(appo.getRepices(), type);

                    mSharedPreferencesProvider.setLastUpdate(System.currentTimeMillis());

                    if(type.equals("Alcoholic"))
                        alcolici.postValue(appo);
                    else
                        analcolici.postValue(appo);
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
            public void onFailure(Call<RicetteList> call, Throwable t)
            {
                if(type.equals("Alcoholic"))
                    alcolici.getValue().setError(t.getMessage());
                else
                    analcolici.getValue().setError(t.getMessage());
            }
        });
    }

    private void getRecipesFromDatabase(String type)
    {
        Runnable runnable = new Runnable() {
            @Override
            public void run()
            {
                RicetteList recipes = null;

                if(type.equals("Alcoholic"))
                    recipes = alcolici.getValue();
                else
                    analcolici.getValue();

                if (recipes == null)
                    recipes = new RicetteList();

                recipes.setRecipes(recipesDao.getRicette(type));

                if(type.equals("Alcoholic"))
                    alcolici.postValue(recipes);
                else
                    analcolici.postValue(recipes);
            }
        };

        new Thread(runnable).start();
    }

    public RicetteList setType(RicetteList appo, String type)
    {
        for (Ricetta r : appo.getRepices())
            r.setType(type);

        return appo;
    }
}
