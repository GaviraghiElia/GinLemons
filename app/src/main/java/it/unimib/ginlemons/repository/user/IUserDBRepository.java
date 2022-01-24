package it.unimib.ginlemons.repository.user;
import androidx.lifecycle.MutableLiveData;

import it.unimib.ginlemons.model.FirebaseResponse;
import it.unimib.ginlemons.model.UserHelper;

public interface IUserDBRepository {
    MutableLiveData<FirebaseResponse> createUserWithEmailRealTimeDB(String name, String email);
    MutableLiveData<FirebaseResponse> updateEmailRealTimeDB(UserHelper userHelper);
    MutableLiveData<FirebaseResponse> changeName(UserHelper userHelper);

}
