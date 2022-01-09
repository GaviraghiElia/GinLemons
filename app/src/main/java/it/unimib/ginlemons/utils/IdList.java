package it.unimib.ginlemons.utils;

// Classe necessaria per l'interazione con l'API
// Incapsula un array di stringhe che rappresentano gli Ids dei cocktail
public class IdList {
    private String[] ids;

    public String[] getIds() {
        return ids;
    }

    public IdList(String[] ids) {
        this.ids = ids;
    }
}
