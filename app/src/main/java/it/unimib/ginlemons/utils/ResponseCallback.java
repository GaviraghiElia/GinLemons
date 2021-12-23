package it.unimib.ginlemons.utils;

// Interfaccia con i metodi di risposta ad una chiamata all'API
public interface ResponseCallback {
    void onResponse(Ricetta ricetta);
    void onResponse(String[] ids, boolean clear);
    void onFailure(String errorString);
}
