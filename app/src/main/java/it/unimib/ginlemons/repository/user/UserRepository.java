package it.unimib.ginlemons.repository.user;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.model.UserHelper;
import it.unimib.ginlemons.utils.SharedPreferencesProvider;

public class UserRepository implements IUserRepository {

    private final FirebaseAuth mAuth;
    private final Application mApplication;
    private final SharedPreferencesProvider mSharedPreferencesProvider;
    private final MutableLiveData<FirebaseResponse> mAuthenticationResponseLiveData;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;

    public UserRepository(Application application)
    {
        mAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        reference = fDB.getReference("users");
        mApplication = application;
        mAuthenticationResponseLiveData = new MutableLiveData<>();
        mSharedPreferencesProvider = new SharedPreferencesProvider(application);
    }

    @Override
    public MutableLiveData<FirebaseResponse> signInWithEmail(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(mApplication), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        FirebaseResponse firebaseResponse = new FirebaseResponse();
                        if (task.isSuccessful())
                        {
                            FirebaseUser user = mAuth.getCurrentUser();
                            firebaseResponse.setSuccess(true);
                        }
                        else
                        {
                            // If sign in fails, display a message to the user.
                            Log.d("TAG", "signInWithEmail:failure", task.getException());

                            firebaseResponse.setSuccess(false);
                            firebaseResponse.setMessage(task.getException().getMessage());
                        }

                        mAuthenticationResponseLiveData.postValue(firebaseResponse);
                    }
                });

        return mAuthenticationResponseLiveData;
    }

    @Override
    public MutableLiveData<FirebaseResponse> createUserWithEmail(String email, String password)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseResponse firebaseResponse = new FirebaseResponse();
                        if (task.isComplete()) {
                            if (task.isSuccessful())
                                firebaseResponse.setSuccess(true);
                            else {
                                firebaseResponse.setSuccess(false);
                                if (task.getException() != null)
                                    firebaseResponse.setMessage(task.getException().getLocalizedMessage());
                                else
                                    firebaseResponse.setMessage(mApplication.getString(R.string.registration_failure));
                            }
                        }
                        mAuthenticationResponseLiveData.postValue(firebaseResponse);
                    }
                });

        return mAuthenticationResponseLiveData;
    }

    @Override
    public MutableLiveData<FirebaseResponse> resetPasswordLink(String email)
    {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        firebaseResponse.setMessage(mApplication.getString(R.string.password_reset_link));
                    else
                        firebaseResponse.setMessage(task.getException().getMessage());
                }
                mAuthenticationResponseLiveData.postValue(firebaseResponse);
            }
        });

        return mAuthenticationResponseLiveData;
    }


    @Override
    public MutableLiveData<FirebaseResponse> reauthenticateUser(UserHelper userHelper, String email, String password)
    {
        AuthCredential credential = EmailAuthProvider.getCredential(email, password); // Current Login Credentials \\

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null)
        {
            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>()
            {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    FirebaseResponse firebaseResponse = new FirebaseResponse();

                    if (task.isSuccessful())
                    {
                        firebaseUser.updateEmail(userHelper.getEmail());
                        reference.child(firebaseUser.getUid()).setValue(userHelper);
                        firebaseResponse.setSuccess(true);
                    }
                    else
                    {
                        firebaseResponse.setSuccess(false);

                        if (task.getException() == null)
                            firebaseResponse.setMessage("Error reauthentication");
                        else
                            firebaseResponse.setMessage(task.getException().getMessage());
                    }

                    mAuthenticationResponseLiveData.postValue(firebaseResponse);
                }
            });
        }
        return mAuthenticationResponseLiveData;
    }

    @Override
    public MutableLiveData<FirebaseResponse> updateEmail(String email)
    {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task)
                {
                    FirebaseResponse firebaseResponse = new FirebaseResponse();

                    if(task.isSuccessful())
                        firebaseResponse.setSuccess(true);
                    else
                    {
                        firebaseResponse.setSuccess(false);

                        if (task.getException() == null)
                            firebaseResponse.setMessage("Error update Email");
                        else
                            firebaseResponse.setMessage(task.getException().getMessage());
                    }

                    mAuthenticationResponseLiveData.postValue(firebaseResponse);
                }
            });
        }
        return mAuthenticationResponseLiveData;
    }

}
