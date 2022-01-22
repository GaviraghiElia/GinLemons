package it.unimib.ginlemons.repository.preferiti;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.ui.ricette.RicettaHelper;
import it.unimib.ginlemons.utils.SharedPreferencesProvider;

public class FavoritesRepository implements IFavoritesRepository{
    private final FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private final Application mApplication;
    private final SharedPreferencesProvider mSharedPreferencesProvider;
    private final MutableLiveData<FirebaseResponse> mAuthenticationResponseLiveData;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;

    public FavoritesRepository(Application application) {
        mAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        firebaseUser = mAuth.getCurrentUser();
        reference = fDB.getReference("favorites").child(firebaseUser.getUid());
        mApplication = application;
        mAuthenticationResponseLiveData = new MutableLiveData<>();
        mSharedPreferencesProvider = new SharedPreferencesProvider(application);
    }

    @Override
    public MutableLiveData<FirebaseResponse> addFavorites(RicettaHelper ricettaHelper) {
        reference.child(ricettaHelper.getId()).setValue(ricettaHelper)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
             FirebaseResponse firebaseResponse = new FirebaseResponse();
                if(task.isSuccessful()){
                    firebaseResponse.setSuccess(true);
                }else{
                    if(task.getException() == null){
                        firebaseResponse.setMessage("Error to add favorites");
                    }else{
                        firebaseResponse.setMessage(task.getException().getMessage());
                    }
                }
             mAuthenticationResponseLiveData.postValue(firebaseResponse);
            }
        });
        return mAuthenticationResponseLiveData;
    }

    @Override
    public MutableLiveData<FirebaseResponse> removeFavorites(RicettaHelper ricettaHelper) {
        reference.child(ricettaHelper.getId()).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseResponse firebaseResponse = new FirebaseResponse();
                        if(task.isSuccessful()){
                            firebaseResponse.setSuccess(true);
                        }else{
                            if(task.getException() == null){
                                firebaseResponse.setMessage("Error to add favorites");
                            }else{
                                firebaseResponse.setMessage(task.getException().getMessage());
                            }
                        }
                        mAuthenticationResponseLiveData.postValue(firebaseResponse);
                    }
                });
        return mAuthenticationResponseLiveData;
    }
}
