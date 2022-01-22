package it.unimib.ginlemons.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.ginlemons.utils.Constants;
import it.unimib.ginlemons.utils.Ricetta;

@Database(entities = {Ricetta.class}, version = Constants.DATABASE_VERSION)
public abstract class RecipeRoomDatabase extends RoomDatabase {

    public abstract RecipesDao recipeDao();

    private static volatile RecipeRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriterExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RecipeRoomDatabase getDatabase(final Context context)
    {
        if (INSTANCE == null)
        {
            synchronized (RecipeRoomDatabase.class)
            {
                if (INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), RecipeRoomDatabase.class, Constants.NEWS_DATABASE_NAME).build();
            }

        }

        return INSTANCE;
    }

}
