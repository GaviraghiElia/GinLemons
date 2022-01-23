package it.unimib.ginlemons.ui.ricette;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.ginlemons.model.FavoritesResponse;
import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.repository.IRecipeRepository;
import it.unimib.ginlemons.repository.RecipeRepository;
import it.unimib.ginlemons.repository.preferiti.FavoritesRepository;
import it.unimib.ginlemons.repository.preferiti.IFavoritesRepository;
import it.unimib.ginlemons.utils.RicetteList;
import it.unimib.ginlemons.utils.SharedPreferencesProvider;

public class RicetteViewModel extends AndroidViewModel {

    private final IRecipeRepository iRecipeRepository;
    private final IFavoritesRepository mFavoritesRepository;

    private MutableLiveData<RicetteList> alcolici;
    private MutableLiveData<RicetteList> analcolici;

    private MutableLiveData<FavoritesResponse> preferitiAlcolici;
    private MutableLiveData<FavoritesResponse> preferitiAnalcolici;

    private MutableLiveData<FirebaseResponse> mAuthenticationResponseLiveData;

    private FirebaseAuth mAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;

    private int type = -1;

    private final SharedPreferencesProvider mSharedPreferencesProvider;

    public RicetteViewModel(@NonNull Application application) {
        super(application);
        mSharedPreferencesProvider = new SharedPreferencesProvider(getApplication());
        iRecipeRepository = new RecipeRepository(application);
        this.mFavoritesRepository = new FavoritesRepository(application);

        mAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        reference = fDB.getReference("favorites").child(mAuth.getCurrentUser().getUid());
    }

    public MutableLiveData<RicetteList> getAlcolici()
    {
        if(alcolici == null)
            alcolici = iRecipeRepository.fetchRecipes("Alcoholic", mSharedPreferencesProvider.getLastUpdate());
        return alcolici;
    }

    public MutableLiveData<RicetteList> getAnalcolici()
    {
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

    public MutableLiveData<FirebaseResponse> addPreferites(RicettaHelper ricettaHelper)
    {
        mAuthenticationResponseLiveData = mFavoritesRepository.addFavorites(ricettaHelper);
        if(ricettaHelper.getType().equals("Alcoholic"))
        {
            preferitiAlcolici.getValue().addFavorites(ricettaHelper);
        }else{
            preferitiAnalcolici.getValue().addFavorites(ricettaHelper);
        }

        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<FirebaseResponse> removeFavorites(RicettaHelper ricettaHelper)
    {
        mAuthenticationResponseLiveData = mFavoritesRepository.removeFavorites(ricettaHelper);
        if(ricettaHelper.getType().equals("Alcoholic"))
        {
            preferitiAlcolici.getValue().removeFavorites(ricettaHelper);
        }else{
            preferitiAnalcolici.getValue().removeFavorites(ricettaHelper);
        }
        return mAuthenticationResponseLiveData;
    }



    public MutableLiveData<FavoritesResponse> getPreferitiAlcolici()
    {
        if(preferitiAlcolici == null) {
            preferitiAlcolici = mFavoritesRepository.getPreferiti("Alcoholic");
        }
        return preferitiAlcolici;
    }

    public MutableLiveData<FavoritesResponse> getPreferitiAnalcolici()
    {
        if(preferitiAnalcolici == null) {
            preferitiAnalcolici = mFavoritesRepository.getPreferiti("Non_Alcoholic");
        }
        return preferitiAnalcolici;
    }

    public void clear() {
        mAuthenticationResponseLiveData.postValue(null);
    }
}
