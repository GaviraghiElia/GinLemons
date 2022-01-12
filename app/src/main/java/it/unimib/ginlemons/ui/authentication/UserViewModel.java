package it.unimib.ginlemons.ui.authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.model.AuthenticationResponse;
import it.unimib.ginlemons.repository.user.IUserRepository;
import it.unimib.ginlemons.repository.user.UserRepository;

public class UserViewModel extends AndroidViewModel {
    private MutableLiveData<AuthenticationResponse> mAuthenticationResponseLiveData;
    private final IUserRepository mUserRepository;

    public UserViewModel(@NonNull Application application) {
        super(application);
        this.mUserRepository = new UserRepository(application);
    }


    public MutableLiveData<AuthenticationResponse> signInWithEmail(String email, String password) {
        mAuthenticationResponseLiveData = mUserRepository.signInWithEmail(email, password);
        return mAuthenticationResponseLiveData;
    }

    public MutableLiveData<AuthenticationResponse> signUpWithEmail(String name, String email, String password) {
        mAuthenticationResponseLiveData = mUserRepository.createUserWithEmail(name, email, password);
        return mAuthenticationResponseLiveData;
    }

    public void clear() {
        mAuthenticationResponseLiveData.postValue(null);
    }

}
