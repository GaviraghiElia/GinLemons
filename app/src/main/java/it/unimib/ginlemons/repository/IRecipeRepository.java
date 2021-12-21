package it.unimib.ginlemons.repository;

public interface IRecipeRepository {

    void getRecipeById(String id);
    void fetchRecipes(String type);
}
