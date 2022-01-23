package it.unimib.ginlemons.service;

import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.RicetteList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Interfaccia con le chiamate da fare all'API
public interface RecipeApiService
{
    @GET("filter.php")
    Call<RicetteList> fetchRecipes(@Query("a") String type);

    @GET("lookup.php")
    Call<Ricetta> getRecipeById(@Query("i") String id);
}
