package it.unimib.ginlemons.ui.authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthCredential;

import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.model.UserHelper;
import it.unimib.ginlemons.repository.user.IUserRepository;
import it.unimib.ginlemons.repository.user.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private MutableLiveData<FirebaseResponse> mAuthenticationResponseLiveData;
    private final IUserRepository mUserRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.mUserRepository = new UserRepository(application);
    }


    public MutableLiveData<FirebaseResponse> signInWithEmail(String email, String password) {
        mAuthenticationResponseLiveData = mUserRepository.signInWithEmail(email, password);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<FirebaseResponse> signUpWithEmail(String name, String email, String password) {
        mAuthenticationResponseLiveData = mUserRepository.createUserWithEmail(name, email, password);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<FirebaseResponse> signUpWithEmailRealTimeDB(String email, String name){
        mAuthenticationResponseLiveData = mUserRepository.createUserWithEmailRealTimeDB(email, name);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<FirebaseResponse> resetPasswordLink(String email){
        mAuthenticationResponseLiveData = mUserRepository.resetPasswordLink(email);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<FirebaseResponse> reauthenticateUser(UserHelper userHelper, String email, String password){
        mAuthenticationResponseLiveData = mUserRepository.reauthenticateUser(userHelper, email, password);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<FirebaseResponse> updateEmail(String email){
        mAuthenticationResponseLiveData = mUserRepository.updateEmail(email);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<FirebaseResponse> updateEmailRealTimeDB(UserHelper userHelper){
        mAuthenticationResponseLiveData = mUserRepository.updateEmailRealTimeDB(userHelper);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<FirebaseResponse> changeName(UserHelper userHelper){
        mAuthenticationResponseLiveData = mUserRepository.changeName(userHelper);
        return mAuthenticationResponseLiveData;
    }

    public void clear() {
        mAuthenticationResponseLiveData.postValue(null);
    }

}
