package it.unimib.ginlemons.ui.authentication;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;
import static it.unimib.ginlemons.utils.Constants.PASSWORD_PATTERN;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.databinding.FragmentRegisterBinding;
import it.unimib.ginlemons.model.UserHelper;

public class RegisterFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;
    private NavController navController;
    private FragmentRegisterBinding mBinding;
    private UserViewModel mUserViewModel;

    public RegisterFragment(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
            public void onClick(View v) {
                //createUser();
                String name = mBinding.registerName.getText().toString();
                String email = mBinding.registerEmail.getText().toString();
                String password = mBinding.registerPassword.getText().toString();

                if(name.isEmpty()){
                    mBinding.registerName.setError("Name cannot be empty");
                    mBinding.registerName.requestFocus();

                }else if(email.isEmpty()){
                    mBinding.registerEmail.setError("Email cannot be empty");
                    mBinding.registerEmail.requestFocus();

                }else if (password.isEmpty()){
                    mBinding.registerPassword.setError("Passowrd cannot be empty");
                    mBinding.registerPassword.requestFocus();

                }else if(!isValidPassword(password)) {
                    mBinding.registerPassword.setError("Passowrd not valid");
                    Toast.makeText(getContext(), "Password must contain 6 to 15 characters, 1 special character and 1 number", Toast.LENGTH_LONG).show();
                    mBinding.registerPassword.requestFocus();

                }else{
                    mUserViewModel.signUpWithEmail(name, email, password).observe(getViewLifecycleOwner(), firebaseResponse -> {
                        if (firebaseResponse != null) {
                            if (firebaseResponse.isSuccess()) {
                                mUserViewModel.clear();
                                mUserViewModel.signUpWithEmailRealTimeDB(email, name).observe(getViewLifecycleOwner(), firebaseResponse1 -> {
                                    if(firebaseResponse1 != null){
                                        if(firebaseResponse1.isSuccess()){
                                            makeMessage("Registrazione andata a buon fine");
                                            firebaseAuth.signOut();
                                            navController.navigate(R.id.action_registerFragment_to_loginFragment);
                                        } else {
                                            makeMessage(firebaseResponse1.getMessage());
                                        }
                                    }
                                });
                            } else {
                                makeMessage(firebaseResponse.getMessage());
                            }
                        }
                    });
                }
            }
        });

        mBinding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        return view;
    }

    // watcher of text change
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String nameInput = mBinding.registerName.getText().toString();
            String usernameInput = mBinding.registerEmail.getText().toString();
            String passwordInput = mBinding.registerPassword.getText().toString();
            mBinding.buttonRegister.setEnabled((!nameInput.isEmpty()) && (!usernameInput.isEmpty()) && (!passwordInput.isEmpty()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    // check pattern password
    public boolean isValidPassword(String password){
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private void makeMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        mUserViewModel.clear();
    }

    public void backButtonPressed(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    navController.navigate(R.id.action_registerFragment_to_loginFragment);
                    return true;
                }
                return false;
            }
        });
    }

}