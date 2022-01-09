package it.unimib.ginlemons.utils;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

public class Ricetta {
    private String id;
    private String name;
    private int alcool;
    private int level;
    private String istruzioni;
    private String[] ingredienti;
    private String[] dosi;
    private boolean preferito;

    public Ricetta(String id, String name){
        this.id = id;
        this.name = name;
    }

    public Ricetta(String id, String name, int alcool, int level, String istruzioni, String[] ingredienti, String[] dosi){
        this.id = id;
        this.name = name;
        this.alcool = alcool;
        this.level = level;
        this.istruzioni = istruzioni;
        this.ingredienti = ingredienti;
        this.dosi = dosi;
        preferito = false;
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

    // Ordine di gradazione alcolica crescente
    public static Comparator<Ricetta> OrdinaRicetteAlcoolCrescente = new Comparator<Ricetta>() {
        @Override
        public int compare(Ricetta r1, Ricetta r2) {
            return r1.getAlcool() - r2.getAlcool();
        }
    };

    // Ordine di gradazione alcolica decrescente
    public static Comparator<Ricetta> OrdinaRicetteAlcoolDecrescente = new Comparator<Ricetta>() {
        @Override
        public int compare(Ricetta r1, Ricetta r2) {
            return r2.getAlcool() - r1.getAlcool();
        }
    };
}
