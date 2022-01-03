package it.unimib.ginlemons.utils;

import java.util.Comparator;

public class Ricetta {

    private String name;
    private int alcool;
    private int level;
    private boolean preferito;

    public Ricetta(){

    }

    public Ricetta(String name, int alcool, int level){
        this.name = name;
        this.alcool = alcool;
        this.level = level;
        preferito = false;
    }

    public Ricetta(String name, int alcool, int level, boolean preferito){
        this.name = name;
        this.alcool = alcool;
        this.level = level;
        this.preferito = preferito;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getAlcool(){
        return alcool;
    }

    public void setAlcool(int alcool){
        this.alcool = alcool;
    }

    public int getLevel(){
        return level;
    }

    public void setLevel(int level){
        this.level = level;
    }

    public boolean isPreferito(){
        return preferito;
    }

    public void addPreferito(){
        preferito = true;
    }

    public void removePreferito(){
        preferito = false;
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

    public static Comparator<Ricetta> OrdinaRicetteAlcoolCrescente = new Comparator<Ricetta>() {
        @Override
        public int compare(Ricetta r1, Ricetta r2) {
            return r1.getAlcool() - r2.getAlcool();
        }
    };

    public static Comparator<Ricetta> OrdinaRicetteAlcoolDecrescente = new Comparator<Ricetta>() {
        @Override
        public int compare(Ricetta r1, Ricetta r2) {
            return r2.getAlcool() - r1.getAlcool();
        }
    };
}
