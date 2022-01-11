package it.unimib.ginlemons.ui.ricette;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.repository.IRecipeRepository;
import it.unimib.ginlemons.repository.RecipeRepository;
import it.unimib.ginlemons.utils.RicetteList;

public class RicetteViewModel extends AndroidViewModel {

    private final IRecipeRepository iRecipeRepository;
    private MutableLiveData<RicetteList> alcolici;
    private MutableLiveData<RicetteList> analcolici;

    public RicetteViewModel(@NonNull Application application) {
        super(application);

        iRecipeRepository = new RecipeRepository(application);
    }

    public MutableLiveData<RicetteList> getAlcolici()
    {
        if(alcolici == null)
            alcolici = iRecipeRepository.fetchRecipes("Alcoholic");


        return alcolici;
    }

    public MutableLiveData<RicetteList> getAnalcolici()
    {
        if(analcolici == null)
            analcolici = iRecipeRepository.fetchRecipes("Non_Alcoholic");

        return analcolici;
    }
}
