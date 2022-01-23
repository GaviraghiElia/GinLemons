package it.unimib.ginlemons.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.utils.RicetteList;

// Interfaccia con i metodi che si interfacciano con l'API
public interface IRecipeRepository
{
    MutableLiveData<RicetteList> fetchRecipes(String type, long lastUpdate);
}
