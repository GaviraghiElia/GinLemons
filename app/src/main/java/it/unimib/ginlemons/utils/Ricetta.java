package it.unimib.ginlemons.utils;

import java.util.Comparator;

public class Ricetta {
    private String id;
    private String name;
    private String istruzioni;
    private String[] ingredienti;
    private String[] dosi;


    public Ricetta(){
    }

    public Ricetta(String id, String name){
        this.id = id;
        this.name = name;
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


    public static Comparator<Ricetta> OrdinaRicetteAlfabeticoAZ = new Comparator<Ricetta>() {
        @Override
        public int compare(Ricetta r1, Ricetta r2) {
            return r1.getName().compareToIgnoreCase(r2.getName());
        }
    };

    public static Comparator<Ricetta> OrdinaRicetteAlfabeticoZA = new Comparator<Ricetta>() {
        @Override
        public int compare(Ricetta r1, Ricetta r2) {
            return r2.getName().compareToIgnoreCase(r1.getName());
        }
    };

}
