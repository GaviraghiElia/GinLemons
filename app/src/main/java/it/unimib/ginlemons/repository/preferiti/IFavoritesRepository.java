package it.unimib.ginlemons.repository.preferiti;

import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.model.FavoritesResponse;
import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.ui.ricette.RicettaHelper;

public interface IFavoritesRepository
{
    MutableLiveData<FirebaseResponse> addFavorites(RicettaHelper ricettaHelper);
    MutableLiveData<FirebaseResponse> removeFavorites(RicettaHelper ricettaHelper);
    MutableLiveData<FavoritesResponse> getPreferiti(String type);
}