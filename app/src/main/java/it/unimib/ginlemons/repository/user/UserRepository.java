package it.unimib.ginlemons.repository.user;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.model.AuthenticationResponse;
import it.unimib.ginlemons.model.UserHelper;
import it.unimib.ginlemons.utils.SharedPreferencesProvider;

public class UserRepository implements IUserRepository {

    private final FirebaseAuth mAuth;
    private final Application mApplication;
    private final SharedPreferencesProvider mSharedPreferencesProvider;
    private final MutableLiveData<AuthenticationResponse> mAuthenticationResponseLiveData;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;

    public UserRepository(Application application) {
        mAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        reference = fDB.getReference("users");
        mApplication = application;
        mAuthenticationResponseLiveData = new MutableLiveData<>();
        mSharedPreferencesProvider = new SharedPreferencesProvider(application);
    }


    @Override
    public MutableLiveData<AuthenticationResponse> signInWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(mApplication), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            authenticationResponse.setSuccess(true);
                            mSharedPreferencesProvider.
                                    setAuthenticationToken(user.getIdToken(false).getResult().getToken());
                            mSharedPreferencesProvider.setUserId(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("TAG", "signInWithEmail:failure", task.getException());
                            authenticationResponse.setSuccess(false);
                            authenticationResponse.setMessage(task.getException().getMessage());
                        }
                        mAuthenticationResponseLiveData.postValue(authenticationResponse);
                    }
                });
        return mAuthenticationResponseLiveData;
    }

    @Override
    public MutableLiveData<AuthenticationResponse> createUserWithEmail(String name, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(mApplication), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            authenticationResponse.setSuccess(true);
                            if (user != null) {
                                mSharedPreferencesProvider.
                                        setAuthenticationToken(user.getIdToken(false).getResult().getToken());
                                mSharedPreferencesProvider.setUserId(user.getUid());
                            }

                            UserHelper userHelper = new UserHelper(name, email);
                            reference.child(user.getUid()).setValue(userHelper).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("Real Time DB", "Successo");
                                        authenticationResponse.setSuccess(true);

                                    }else{
                                        Log.d("Firestore", "Errore! DB non scritto");
                                        authenticationResponse.setSuccess(false);
                                        if (task.getException() != null) {
                                            authenticationResponse.setMessage(task.getException().getLocalizedMessage());
                                        } else {
                                            authenticationResponse.setMessage(mApplication.getString(R.string.registration_failure));
                                        }
                                    }
                                }
                            });

                        } else {
                            Log.d("TAG", "createUserWithEmail:failure" + task.getException().getMessage());
                            authenticationResponse.setSuccess(false);
                            if (task.getException() != null) {
                                authenticationResponse.setMessage(task.getException().getLocalizedMessage());
                            } else {
                                authenticationResponse.setMessage(mApplication.getString(R.string.registration_failure));
                            }
                        }
                        mAuthenticationResponseLiveData.postValue(authenticationResponse);
                    }
                });
        return mAuthenticationResponseLiveData;
    }
}

