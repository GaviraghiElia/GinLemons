package it.unimib.ginlemons.ui.ricette;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.repository.IRecipeRepository;
import it.unimib.ginlemons.repository.RecipeRepository;
import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.RicetteList;
import it.unimib.ginlemons.utils.SharedPreferencesProvider;

public class RicetteViewModel extends AndroidViewModel {

    private final IRecipeRepository iRecipeRepository;
    private MutableLiveData<RicetteList> alcolici;
    private MutableLiveData<RicetteList> analcolici;

    private final SharedPreferencesProvider mSharedPreferencesProvider;

    public RicetteViewModel(@NonNull Application application) {
        super(application);

        mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        iRecipeRepository = new RecipeRepository(application);
    }

    public MutableLiveData<RicetteList> getAlcolici()
    {
        if(alcolici == null)
            alcolici = iRecipeRepository.fetchRecipes("Alcoholic", mSharedPreferencesProvider.getLastUpdate());

        return alcolici;
    }

    public MutableLiveData<RicetteList> getAnalcolici() {
        if (analcolici == null)
            analcolici = iRecipeRepository.fetchRecipes("Non_Alcoholic", mSharedPreferencesProvider.getLastUpdate());

        return analcolici;
    }

    public void setType(String type)
    {
        MutableLiveData<RicetteList> appo = alcolici;

        if(type.equalsIgnoreCase( "Non_Alcoholic"))
            appo = analcolici;

        for (Ricetta r : appo.getValue().getRepices())
            r.setType(type);
    }
}
