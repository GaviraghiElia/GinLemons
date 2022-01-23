package it.unimib.ginlemons.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unimib.ginlemons.ui.ricette.RicettaHelper;

public class FavoritesResponse {
    private boolean success;
    private String message;
    private HashMap<String, RicettaHelper> recipes;

    public FavoritesResponse()
    {
        recipes = new HashMap<>();
    }
    public FavoritesResponse(HashMap<String, RicettaHelper> repices)
    {
        this.recipes = repices;
    }

    public void addFavorites(RicettaHelper ricettaHelper){
        recipes.put(ricettaHelper.getId(), ricettaHelper);
    }

    public void removeFavorites(RicettaHelper ricettaHelper){
        recipes.remove(ricettaHelper.getId());
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RicettaHelper getRecipe(String id)
    {
        return recipes.get(id);
    }

    public ArrayList<RicettaHelper> getRepices()
    {
        return new ArrayList<RicettaHelper>(recipes.values());
    }

    public void setRecipes(List<RicettaHelper> ricette)
    {
        HashMap<String, RicettaHelper> recipes = new HashMap<>();

        for(RicettaHelper r : ricette)
            recipes.put(r.getId(), r);

        this.recipes = recipes;
    }


    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                '}';
    }
}
