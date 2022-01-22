package it.unimib.ginlemons.ui.ricette;

import android.app.Application;
import android.util.Log;

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

    private int type = -1;

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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void changeType() {
        type = (type + 1) % 2;
    }
}
