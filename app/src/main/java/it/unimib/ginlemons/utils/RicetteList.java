package it.unimib.ginlemons.utils;

import java.util.ArrayList;

public class RicetteList {
    private String error = null;
    private ArrayList<Ricetta> repices;

    public RicetteList(ArrayList<Ricetta> repices) {
        this.repices = repices;
    }

    public ArrayList<Ricetta> getRepices() {
        return repices;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
