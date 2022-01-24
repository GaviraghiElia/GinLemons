package it.unimib.ginlemons.repository.user;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.model.UserHelper;

public class UserDBRepository implements IUserDBRepository{

    private final MutableLiveData<FirebaseResponse> mDBResponseLiveData;
    private final FirebaseAuth mAuth;
    private final Application mApplication;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;


    public UserDBRepository(Application application)
    {
        mAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        reference = fDB.getReference("users");
        mApplication = application;
        mDBResponseLiveData = new MutableLiveData<>();
    }

    @Override
    public MutableLiveData<FirebaseResponse> createUserWithEmailRealTimeDB(String email, String name)
    {
        UserHelper userHelper = new UserHelper(name, email);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null) {
            reference.child(firebaseUser.getUid()).setValue(userHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    FirebaseResponse firebaseResponse = new FirebaseResponse();
                    if (task.isSuccessful()) {
                        Log.d("Real Time DB", "Successo");
                        firebaseResponse.setSuccess(true);
                    } else {
                        Log.d("Firestore", "Errore! DB non scritto");

                        firebaseResponse.setSuccess(false);

                        if (task.getException() != null)
                            firebaseResponse.setMessage(task.getException().getLocalizedMessage());
                        else
                            firebaseResponse.setMessage(mApplication.getString(R.string.registration_failure));
                    }
                    mDBResponseLiveData.postValue(firebaseResponse);
                }
            });
        }

        return mDBResponseLiveData;
    }

    @Override
    public MutableLiveData<FirebaseResponse> updateEmailRealTimeDB(UserHelper userHelper)
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null) {


            reference.child(firebaseUser.getUid()).setValue(userHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    FirebaseResponse firebaseResponse = new FirebaseResponse();

                    if (task.isSuccessful())
                        firebaseResponse.setSuccess(true);
                    else {
                        firebaseResponse.setSuccess(false);

                        if (task.getException() == null)
                            firebaseResponse.setMessage("Error update Email");
                        else
                            firebaseResponse.setMessage(task.getException().getMessage());
                    }

                    mDBResponseLiveData.postValue(firebaseResponse);
                }
            });
        }
        return mDBResponseLiveData;
    }

    @Override
    public MutableLiveData<FirebaseResponse> changeName(UserHelper userHelper)
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            reference.child(firebaseUser.getUid())
                    .setValue(userHelper)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            FirebaseResponse firebaseResponse = new FirebaseResponse();
                            if(task.isSuccessful())
                                firebaseResponse.setSuccess(true);
                            else
                            {
                                firebaseResponse.setSuccess(false);

                                if(task.getException() == null)
                                    firebaseResponse.setMessage(mApplication.getString(R.string.name_not_updated));
                                else
                                    firebaseResponse.setMessage(task.getException().getMessage());
                            }

                            mDBResponseLiveData.postValue(firebaseResponse);
                        }
                    });
        }
        return mDBResponseLiveData;
    }
}
