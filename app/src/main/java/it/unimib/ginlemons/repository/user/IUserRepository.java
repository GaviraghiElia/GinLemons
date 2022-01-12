package it.unimib.ginlemons.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.model.AuthenticationResponse;

public interface IUserRepository {
    MutableLiveData<AuthenticationResponse> signInWithEmail(String email, String password);
    MutableLiveData<AuthenticationResponse> createUserWithEmail(String name, String email, String password);


}
