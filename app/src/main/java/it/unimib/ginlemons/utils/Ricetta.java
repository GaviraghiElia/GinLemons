package it.unimib.ginlemons.utils;

import java.util.Arrays;
import java.util.Comparator;

public class Ricetta {
    private String id;
    private String name;
    private String istruzioni;
    private String[] ingredienti;
    private String[] dosi;

    private String error = null;


    public Ricetta(){}

    public Ricetta(String id, String name)
    {
        this.id = id;
        this.name = name;
        this.istruzioni = null;
    }

    public Ricetta(String id, String name, String istruzioni, String[] ingredienti, String[] dosi){
        this.id = id;
        this.name = name;
        this.istruzioni = istruzioni;
        this.ingredienti = ingredienti;
        this.dosi = dosi;
    }

    public String getId() {
        return id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getIstruzioni() {
        return istruzioni;
    }

    @Override
    public String toString() {
        return "Ricetta{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", istruzioni='" + istruzioni + '\'' +
                '}';
    }

    // Metodi per il sort delle ricette nelle sezioni Esplora e Preferiti

    // Ordine alfabetico crescente
    public static Comparator<Ricetta> OrdinaRicetteAlfabeticoAZ = new Comparator<Ricetta>() {
        @Override
        public int compare(Ricetta r1, Ricetta r2) {
            return r1.getName().compareToIgnoreCase(r2.getName());
        }
    };

    // Ordine alfabetico decrescente
    public static Comparator<Ricetta> OrdinaRicetteAlfabeticoZA = new Comparator<Ricetta>() {
        @Override
        public int compare(Ricetta r1, Ricetta r2) {
            return r2.getName().compareToIgnoreCase(r1.getName());
        }
    };


    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
