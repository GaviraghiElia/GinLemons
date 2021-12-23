package it.unimib.ginlemons.utils;

import java.util.List;

public interface ResponseCallback {
    void onResponse(Ricetta ricetta);
    void onResponse(String[] ids, boolean clear);
    void onFailure(String errorString);
}
