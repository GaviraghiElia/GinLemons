package it.unimib.ginlemons.ui.authentication;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;
import static it.unimib.ginlemons.utils.Constants.PASSWORD_PATTERN;

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
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment
{
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;
    private NavController navController;
    private FragmentRegisterBinding mBinding;
    private UserViewModel mUserViewModel;

    public RegisterFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        navController = NavHostFragment.findNavController(this);
        backButtonPressed(view);

        // Text Watcher per abilitare il bottone di registrazione
        mBinding.registerName.addTextChangedListener(loginTextWatcher);
        mBinding.registerEmail.addTextChangedListener(loginTextWatcher);
        mBinding.registerPassword.addTextChangedListener(loginTextWatcher);

        firebaseAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        reference = fDB.getReference("users");

        mBinding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String name = mBinding.registerName.getText().toString();
                String email = mBinding.registerEmail.getText().toString();
                String password = mBinding.registerPassword.getText().toString();

                if(name.isEmpty())
                {
                    mBinding.registerName.setError(getString(R.string.name_not_empty));
                    mBinding.registerName.requestFocus();
                }
                else
                    if(email.isEmpty())
                    {
                        mBinding.registerEmail.setError(getString(R.string.email_not_empty));
                        mBinding.registerEmail.requestFocus();

                    }
                    else
                        if(!isValidEmail(email))
                        {
                            mBinding.registerEmail.setError(getString(R.string.bad_email));
                            mBinding.registerEmail.requestFocus();

                        }
                        else
                            if(password.isEmpty())
                            {
                                mBinding.registerPassword.setError(getString(R.string.password_not_empty));
                                mBinding.registerPassword.requestFocus();
                            }
                            else
                                if(!isValidPassword(password))
                                {
                                    mBinding.registerPassword.setError(getString(R.string.incorrect_password));
                                    Toast.makeText(getContext(), getString(R.string.password_requirements), Toast.LENGTH_LONG).show();
                                    mBinding.registerPassword.requestFocus();

                                }
                                else
                                {

                                    mUserViewModel.clear();
                                    mUserViewModel.signUpWithEmail(email, password).observe(getViewLifecycleOwner(), firebaseResponse -> {
                                        if (firebaseResponse != null)
                                        {
                                            if (firebaseResponse.isSuccess())
                                            {
                                                mUserViewModel.signUpWithEmailRealTimeDB(email, name).observe(getViewLifecycleOwner(), realTimeDBResponse -> {
                                                    if(realTimeDBResponse != null)
                                                    {
                                                        if(realTimeDBResponse.isSuccess())
                                                        {
                                                            makeMessage(getString(R.string.successfull_registration));
                                                            firebaseAuth = FirebaseAuth.getInstance();
                                                            firebaseAuth.signOut();
                                                        }
                                                        else
                                                            makeMessage(realTimeDBResponse.getMessage());

                                                        navController.navigate(R.id.action_registerFragment_to_loginFragment);
                                                    }
                                                });
                                            }
                                            else
                                            {
                                                makeMessage(firebaseResponse.getMessage());
                                                navController.navigate(R.id.action_registerFragment_to_loginFragment);
                                            }
                                        }
                                    });
                                }
            }
        });

        mBinding.buttonSignIn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
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
            String nameInput = mBinding.registerName.getText().toString();
            String emailInput = mBinding.registerEmail.getText().toString();
            String passwordInput = mBinding.registerPassword.getText().toString();
            mBinding.buttonRegister.setEnabled((!nameInput.isEmpty()) && (!emailInput.isEmpty()) && (!passwordInput.isEmpty()) && isValidEmail(emailInput));
        }

        @Override
        public void afterTextChanged(Editable s) {}
    };

    // Check pattern password
    public boolean isValidPassword(String password)
    {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    // Check pattern email
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
        view.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_BACK)
                {
                    navController.navigate(R.id.action_registerFragment_to_loginFragment);

                    return true;
                }

                return false;
            }
        });
    }
}