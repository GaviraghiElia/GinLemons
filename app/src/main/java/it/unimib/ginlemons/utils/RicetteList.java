package it.unimib.ginlemons.utils;

import java.util.ArrayList;

public class RicetteList {
    ArrayList<Ricetta> repices;

    public RicetteList(ArrayList<Ricetta> repices) {
        this.repices = repices;
    }

    public ArrayList<Ricetta> getRepices() {
        return repices;
    }
}
