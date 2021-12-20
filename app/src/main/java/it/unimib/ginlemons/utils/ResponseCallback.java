package it.unimib.ginlemons.utils;

import java.util.List;

public interface ResponseCallback {
    void onResponse(List<Ricetta> ricette);
    void onFailure(String errorString);
}
