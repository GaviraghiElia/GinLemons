package it.unimib.ginlemons.service;

import java.util.List;

import it.unimib.ginlemons.utils.ListaRicette;
import it.unimib.ginlemons.utils.Ricetta;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApiService {
    @GET("search.php")
    Call<ListaRicette> getRecipes(@Query("s") String name);
}
