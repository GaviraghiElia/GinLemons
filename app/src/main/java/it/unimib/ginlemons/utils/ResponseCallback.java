package it.unimib.ginlemons.utils;

import java.util.List;

// Interfaccia con i metodi di risposta ad una chiamata all'API
public interface ResponseCallback {
    void onResponse(Ricetta ricetta);
    void onResponse(List<Ricetta> ricette, boolean clear);
    void onFailure(String errorString);
}
