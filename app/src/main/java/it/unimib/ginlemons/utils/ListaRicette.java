package it.unimib.ginlemons.utils;

import java.util.List;

public class ListaRicette {
    private List<Ricetta> recipes;

    public ListaRicette(List<Ricetta> recipes) {
        this.recipes = recipes;
    }

    public List<Ricetta> getRecipes() {
        return recipes;
    }
}
