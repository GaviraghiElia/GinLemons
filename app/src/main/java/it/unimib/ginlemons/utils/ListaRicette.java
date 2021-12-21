package it.unimib.ginlemons.utils;

import java.util.HashMap;
import java.util.List;

public class ListaRicette {
    //private HashMap<String, String> drinks;
    private List<Ricetta> recipes;

    public ListaRicette(List<Ricetta> recipes) {
        this.recipes = recipes;
    }

    public List<Ricetta> getRecipes() {
        return recipes;
    }
}
