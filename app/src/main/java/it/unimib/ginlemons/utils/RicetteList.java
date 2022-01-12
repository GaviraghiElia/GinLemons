package it.unimib.ginlemons.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class RicetteList {
    private String error = null;
    private HashMap<String, Ricetta> recipes;

    public RicetteList()
    {
        recipes = new HashMap<>();
    }

    public RicetteList(HashMap<String, Ricetta> repices)
    {
        this.recipes = repices;
    }

    public ArrayList<Ricetta> getRepices()
    {
        return new ArrayList<Ricetta>(recipes.values());
    }

    public Ricetta getRecipe(String id)
    {
        return recipes.get(id);
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
