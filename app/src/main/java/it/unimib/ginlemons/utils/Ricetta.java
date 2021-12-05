package it.unimib.ginlemons.utils;

public class Ricetta {

    private String name;
    private int alcool;
    private String level;
    private boolean preferito;

    public Ricetta(String name, int alcool, String level){
        this.name = name;
        this.alcool = alcool;
        this.level = level;
        preferito = false;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addPreferito(){
        preferito = true;
    }

    public void removePreferito(){
        preferito = false;
    }
}
