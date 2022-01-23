package it.unimib.ginlemons.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.ginlemons.ui.MainActivity;

public class EntryActivity extends AppCompatActivity
{
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        if (checkSession())
            startActivity(new Intent(this, MainActivity.class));
        else
            startActivity(new Intent(this, AuthenticationActivity.class));

        finish();
    }

    private boolean checkSession()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
            return true;

        return false;
    }
}