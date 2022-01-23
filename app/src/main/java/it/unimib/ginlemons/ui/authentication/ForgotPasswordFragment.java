package it.unimib.ginlemons.ui.authentication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.databinding.FragmentForgotPasswordBinding;

public class ForgotPasswordFragment extends Fragment
{
    private FirebaseAuth mAuth;
    private NavController navController;
    private FragmentForgotPasswordBinding mBinding;
    private UserViewModel mUserViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        mAuth = FirebaseAuth.getInstance();
        navController = NavHostFragment.findNavController(this);
        backButtonPressed(view);

        mBinding.resetMail.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String email = mBinding.resetMail.getText().toString();
                mBinding.resetButton.setEnabled(!email.isEmpty() && isValidEmail(email));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mBinding.resetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                resetPassword(mBinding.resetMail.getText().toString());
            }
        });

        mBinding.returnToLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
            }
        });

        return view;
    }

    public void resetPassword(String email)
    {
        if(email.isEmpty())
            mBinding.resetMail.setError(getContext().getString(R.string.email_not_empty));
        else
            if(!isValidEmail(email))
                mBinding.resetMail.setError(getString(R.string.badEmail));
        else
        {
            mUserViewModel.resetPasswordLink(email).observe(getViewLifecycleOwner(), firebaseResponse -> {
                if(firebaseResponse != null)
                {
                    if(firebaseResponse.isSuccess())
                        makeMessage(getResources().getString(R.string.password_reset_link));
                    else
                        makeMessage(firebaseResponse.getMessage());

                    navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
                }
            });
        }
    }

    // check pattern email
    public boolean isValidEmail(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void makeMessage(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        mUserViewModel.clear();
    }

    public void backButtonPressed(View view)
    {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
                    return true;
                }

                return false;
            }
        });
    }
}
