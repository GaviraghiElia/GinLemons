package it.unimib.ginlemons.repository.preferiti;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.model.FavoritesResponse;
import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.ui.ricette.RicettaHelper;
import it.unimib.ginlemons.utils.SharedPreferencesProvider;

public class FavoritesRepository implements IFavoritesRepository
{
    private final FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private final Application mApplication;
    private final SharedPreferencesProvider mSharedPreferencesProvider;
    private final MutableLiveData<FirebaseResponse> mAuthenticationResponseLiveData;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;

    private MutableLiveData<FavoritesResponse> alcolici;
    private MutableLiveData<FavoritesResponse> analcolici;

    public FavoritesRepository(Application application)
    {
        mAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        firebaseUser = mAuth.getCurrentUser();
        reference = fDB.getReference("favorites").child(firebaseUser.getUid());
        mApplication = application;
        mAuthenticationResponseLiveData = new MutableLiveData<>();
        mSharedPreferencesProvider = new SharedPreferencesProvider(application);

        alcolici = new MutableLiveData<>();
        analcolici = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<FirebaseResponse> addFavorites(RicettaHelper ricettaHelper)
    {
        reference.child(ricettaHelper.getId()).setValue(ricettaHelper)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        FirebaseResponse firebaseResponse = new FirebaseResponse();

                        if(task.isSuccessful())
                            firebaseResponse.setSuccess(true);
                        else
                        {
                            if(task.getException() == null)
                                firebaseResponse.setMessage(Resources.getSystem().getString(R.string.add_favorites));
                            else
                                firebaseResponse.setMessage(task.getException().getMessage());
                        }

                        mAuthenticationResponseLiveData.postValue(firebaseResponse);
                    }
                });

        return mAuthenticationResponseLiveData;
    }

    @Override
    public MutableLiveData<FirebaseResponse> removeFavorites(RicettaHelper ricettaHelper)
    {
        reference.child(ricettaHelper.getId()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>()
                {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        FirebaseResponse firebaseResponse = new FirebaseResponse();

                        if(task.isSuccessful())
                            firebaseResponse.setSuccess(true);
                        else
                            if(task.getException() == null)
                                firebaseResponse.setMessage("Error to add favorites");
                            else
                                firebaseResponse.setMessage(task.getException().getMessage());

                        mAuthenticationResponseLiveData.postValue(firebaseResponse);
                    }
                });

        return mAuthenticationResponseLiveData;
    }

    @Override
    public MutableLiveData<FavoritesResponse> getPreferiti(String type)
    {
        List<RicettaHelper> favoritesList = new ArrayList<>();

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                FavoritesResponse favoritesResponse = new FavoritesResponse();

                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    RicettaHelper ricetta = dataSnapshot.getValue(RicettaHelper.class);

                    if(ricetta.getType().equals(type))
                    {
                        Log.d("SIZEOF", "REPOSITORY - Siamo dentro all'IF con " + ricetta.getName());
                        favoritesList.add(ricetta);
                    }
                }

                Log.d("SIZEOF", "REPOSITORY - fine for con array.size = " + favoritesList.size());

                favoritesResponse.setRecipes(favoritesList);
                favoritesResponse.setSuccess(true);

                Log.d("SIZEOF", "REPOSITORY -  favoritesResponse.setRecipes" + favoritesResponse.getRepices().size());

                if(type.equals("Alcoholic"))
                {
                    Log.d("SIZEOF", "REPOSITORY - Alcoholic");
                    alcolici.postValue(favoritesResponse);
                }else
                {
                    Log.d("SIZEOF", "REPOSITORY - Non_Alcoholic");
                    analcolici.postValue(favoritesResponse);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        if(type.equals("Alcoholic"))
            return alcolici;
        else
            return analcolici;
    }
}