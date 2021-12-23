package it.unimib.ginlemons.repository;

// Interfaccia con i metodi che si interfacciano con l'API
public interface IRecipeRepository {
    void getRecipeById(String id);
    void fetchRecipes(String type, boolean clear);
}
