package it.unimib.ginlemons.ui.authentication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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
import it.unimib.ginlemons.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment
{
    private FirebaseAuth mAuth;
    private NavController navController;
    private FragmentLoginBinding mBinding;
    private UserViewModel mUserViewModel;
    private String email;
    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mBinding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        navController = NavHostFragment.findNavController(this);
        mBinding.loginEmail.addTextChangedListener(loginTextWatcher);
        mBinding.loginPassword.addTextChangedListener(loginTextWatcher);
        mAuth = FirebaseAuth.getInstance();

        mBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                email = mBinding.loginEmail.getText().toString();
                password = mBinding.loginPassword.getText().toString();

                if (email.isEmpty())
                {
                    mBinding.loginEmail.setError(getString(R.string.email_not_empty));
                    mBinding.loginEmail.requestFocus();
                }
                else
                    if(!isValidEmail(email))
                    {
                        mBinding.loginEmail.setError(getString(R.string.bad_email));
                        mBinding.loginEmail.requestFocus();
                    }
                    else
                        if(password.isEmpty())
                        {
                            mBinding.loginPassword.setError(getString(R.string.password_not_empty));
                            mBinding.loginPassword.requestFocus();
                        }
                        else
                        {
                            mUserViewModel.signInWithEmail(email, password).observe(getViewLifecycleOwner(), firebaseResponse -> {
                                if (firebaseResponse != null)
                                {
                                    if (firebaseResponse.isSuccess())
                                    {
                                        NavHostFragment.findNavController(LoginFragment.this).navigate(R.id.action_loginFragment_to_mainActivity);
                                        getActivity().finish();
                                    }
                                    else
                                        makeMessage(firebaseResponse.getMessage());
                                }
                            });
                        }
                    }
        });

        mBinding.buttonSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        // Button forget passowrd
        mBinding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });

        return view;
    }

    // Watcher of text change
    private TextWatcher loginTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            String emailInput = mBinding.loginEmail.getText().toString();
            String passwordInput = mBinding.loginPassword.getText().toString();
            mBinding.buttonLogin.setEnabled(!emailInput.isEmpty() && !passwordInput.isEmpty() && isValidEmail(emailInput));
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    public boolean isValidEmail(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void makeMessage(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        mUserViewModel.clear();
    }
}