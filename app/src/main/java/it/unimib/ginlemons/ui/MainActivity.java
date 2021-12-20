package it.unimib.ginlemons.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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


public class MainActivity extends AppCompatActivity {

    private NavHostFragment navHostFragment;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private AppBarConfiguration appBarConfiguration;
    private Toolbar myToolbar;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Autentication
        mAuth = FirebaseAuth.getInstance();

        // Toolbar
        myToolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(myToolbar);


        // Navigation Component
        navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentListeRicetteContainerView);
        navController = navHostFragment.getNavController();

        // Controllo della bottom bar attraverso il fragment manager
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        appBarConfiguration = new AppBarConfiguration.Builder(R.id.listeFragment, R.id.ricetteFragment, R.id.userProfileFragment).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!checkSession()){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private boolean checkSession(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            return false;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_logout){
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}