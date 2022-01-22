package it.unimib.ginlemons.ui.ricette;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.repository.IRecipeRepository;
import it.unimib.ginlemons.repository.RecipeRepository;
import it.unimib.ginlemons.repository.preferiti.FavoritesRepository;
import it.unimib.ginlemons.repository.preferiti.IFavoritesRepository;
import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.RicetteList;
import it.unimib.ginlemons.utils.SharedPreferencesProvider;

public class RicetteViewModel extends AndroidViewModel {

    private final IRecipeRepository iRecipeRepository;
    private final IFavoritesRepository mFavoritesRepository;

    private MutableLiveData<RicetteList> alcolici;
    private MutableLiveData<RicetteList> analcolici;
    private MutableLiveData<FirebaseResponse> mAuthenticationResponseLiveData;

    private final SharedPreferencesProvider mSharedPreferencesProvider;

    public RicetteViewModel(@NonNull Application application) {
        super(application);
        mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        iRecipeRepository = new RecipeRepository(application);
        this.mFavoritesRepository = new FavoritesRepository(application);
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

    public MutableLiveData<FirebaseResponse> addPreferites(RicettaHelper ricettaHelper) {
        mAuthenticationResponseLiveData = mFavoritesRepository.addFavorites(ricettaHelper);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<FirebaseResponse> removeFavorites(RicettaHelper ricettaHelper) {
        mAuthenticationResponseLiveData = mFavoritesRepository.removeFavorites(ricettaHelper);
        return mAuthenticationResponseLiveData;
    }

    public void clear() {
        mAuthenticationResponseLiveData.postValue(null);
    }
}
