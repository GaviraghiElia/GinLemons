package it.unimib.ginlemons.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import it.unimib.ginlemons.utils.Ricetta;

@Dao
public interface RecipesDao {
    @Query("SELECT * FROM Ricetta WHERE type = :type")
    List<Ricetta> getRicette(String type);

    @Insert
    void insertRecipes(List<Ricetta> recipes);

    @Query("DELETE FROM Ricetta WHERE type = :type")
    void deleteAll(String type);

    @Query("SELECT COUNT(*) FROM Ricetta WHERE type = :type")
    int isEmpty(String type);

    @Query("DELETE FROM Ricetta")
    void viatutto();
}
