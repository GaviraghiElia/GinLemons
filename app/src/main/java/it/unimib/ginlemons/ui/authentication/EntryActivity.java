package it.unimib.ginlemons.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.ginlemons.databinding.ActivityEntryBinding;
import it.unimib.ginlemons.ui.MainActivity;

public class EntryActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityEntryBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityEntryBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        if(checkSession()){
            startActivity(new Intent(EntryActivity.this, MainActivity.class));
        } else {
            startActivity(new Intent(EntryActivity.this, AuthenticationActivity.class));
        }
        finish();
    }

    private boolean checkSession(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            return false;
        }
        return true;
    }
}