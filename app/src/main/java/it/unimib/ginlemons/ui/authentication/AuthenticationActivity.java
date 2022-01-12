package it.unimib.ginlemons.ui.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import it.unimib.ginlemons.databinding.ActivityAuthenticationBinding;

public class AuthenticationActivity extends AppCompatActivity {

    private NavHostFragment navHostFragment;
    private NavController navController;
    private ActivityAuthenticationBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityAuthenticationBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);

        navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(mBinding.fragmentAuthContainerView.getId());
        navController = navHostFragment.getNavController();
    }

}