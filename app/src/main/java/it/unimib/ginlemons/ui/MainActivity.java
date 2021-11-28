package it.unimib.ginlemons.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.ginlemons.R;


public class MainActivity extends AppCompatActivity {

    private NavHostFragment navHostFragment;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private AppBarConfiguration appBarConfiguration;
    private Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        myToolbar = findViewById(R.id.activity_toolbar);
        setSupportActionBar(myToolbar);

        // Navigation Component
        navHostFragment = (NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragmentListeRicetteContainerView);
        navController = navHostFragment.getNavController();

        // controllo bottom bar attraverso il fragment manager
        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        appBarConfiguration =
                new AppBarConfiguration.Builder(R.id.listeFragment, R.id.ricetteFragment).build();

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    }

}