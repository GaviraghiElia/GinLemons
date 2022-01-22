package it.unimib.ginlemons.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public void setRecipes(List<Ricetta> ricette)
    {
        HashMap<String, Ricetta> recipes = new HashMap<>();

        for(Ricetta r : ricette)
            recipes.put(r.getId(), r);

        this.recipes = recipes;
    }

    public void setError(String error) {
        this.error = error;
    }
}
