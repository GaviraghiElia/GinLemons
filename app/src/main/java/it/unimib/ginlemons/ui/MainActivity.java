package it.unimib.ginlemons.ui;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.databinding.ActivityMainBinding;
import it.unimib.ginlemons.ui.authentication.EntryActivity;
import it.unimib.ginlemons.utils.SharedPreferencesProvider;


public class MainActivity extends AppCompatActivity {

    private NavHostFragment navHostFragment;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private AppBarConfiguration appBarConfiguration;
    private Toolbar myToolbar;
    private FirebaseAuth mAuth;
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        // Autentication
        mAuth = FirebaseAuth.getInstance();
        // Toolbar
        myToolbar = mBinding.activityToolbar;
        setSupportActionBar(myToolbar);

        // Navigation Component
        navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentListeRicetteContainerView);
        navController = navHostFragment.getNavController();

        // Controllo della bottom bar attraverso il fragment manager
        bottomNavigationView = mBinding.bottomNavigationView;
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.listeFragment, R.id.ricetteFragment, R.id.userProfileFragment).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!checkSession()){
            startActivity(new Intent(MainActivity.this, EntryActivity.class));
            finish();
        }
    }

    private boolean checkSession(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            return false;
        }
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            mAuth.signOut();
            SharedPreferencesProvider sharedPreferencesProvider =
                    new SharedPreferencesProvider(getApplication());
            sharedPreferencesProvider.deleteAll();
            startActivity(new Intent(MainActivity.this, EntryActivity.class));
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}