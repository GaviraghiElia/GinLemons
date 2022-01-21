package it.unimib.ginlemons.repository.user;

import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.model.UserHelper;

public interface IUserRepository {
    MutableLiveData<FirebaseResponse> signInWithEmail(String email, String password);
    MutableLiveData<FirebaseResponse> createUserWithEmail(String name, String email, String password);
    MutableLiveData<FirebaseResponse> createUserWithEmailRealTimeDB(String name, String email);
    MutableLiveData<FirebaseResponse> reauthenticateUser(String email, String password);
    MutableLiveData<FirebaseResponse> changeName(UserHelper userHelper);
    MutableLiveData<FirebaseResponse> resetPasswordLink(String email);
}
