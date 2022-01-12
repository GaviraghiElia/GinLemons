package it.unimib.ginlemons.utils;

import java.util.List;

// Interfaccia con i metodi di risposta ad una chiamata all'API
public interface ResponseCallback {
    void onResponse(Ricetta ricetta);
    void onFailure(String errorString);
}