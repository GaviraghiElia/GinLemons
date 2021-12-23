package it.unimib.ginlemons.service;

import it.unimib.ginlemons.utils.IdList;
import it.unimib.ginlemons.utils.Ricetta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Interfaccia con le chiamate da fare all'API
public interface RecipeApiService {
    @GET("filter.php")
    Call<IdList> fetchRecipe(@Query("a") String type);

    @GET("lookup.php")
    Call<Ricetta> getRecipeById(@Query("i") String id);
}
