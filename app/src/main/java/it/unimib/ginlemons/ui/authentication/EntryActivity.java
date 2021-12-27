package it.unimib.ginlemons.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.ui.LoginActivity;
import it.unimib.ginlemons.ui.MainActivity;

public class EntryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);


        mAuth = FirebaseAuth.getInstance();
        if(checkSession()){
            startActivity(new Intent(EntryActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(EntryActivity.this, AuthenticationActivity.class));
        }
    }

    private boolean checkSession(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            return false;
        }
        return true;
    }
}