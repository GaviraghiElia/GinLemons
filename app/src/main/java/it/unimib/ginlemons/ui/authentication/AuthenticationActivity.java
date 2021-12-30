package it.unimib.ginlemons.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;

import it.unimib.ginlemons.R;

public class AuthenticationActivity extends AppCompatActivity {

    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        Log.d("Auth Activity", "Capitano siamo dentro");

        navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentAuthContainerView);
        navController = navHostFragment.getNavController();
    }

}