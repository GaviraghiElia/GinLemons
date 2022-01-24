package it.unimib.ginlemons.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.model.UserHelper;

public interface IUserRepository
{
    MutableLiveData<FirebaseResponse> signInWithEmail(String email, String password);
    MutableLiveData<FirebaseResponse> createUserWithEmail(String email, String password);
    MutableLiveData<FirebaseResponse> reauthenticateUser(UserHelper userHelper, String email, String password);
    MutableLiveData<FirebaseResponse> updateEmail(String email);
    MutableLiveData<FirebaseResponse> resetPasswordLink(String email);
}