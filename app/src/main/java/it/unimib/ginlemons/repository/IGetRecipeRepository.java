package it.unimib.ginlemons.repository;

import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.RicetteList;

public interface IGetRecipeRepository {
    Ricetta getRecipeById(String id);
}
