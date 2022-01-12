package it.unimib.ginlemons.ui.authentication;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;
import static it.unimib.ginlemons.utils.Constants.PASSWORD_PATTERN;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        navController = NavHostFragment.findNavController(this);

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
                createUser();
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

    private void createUser() {
        String fullName = mBinding.registerName.getText().toString();
        String sMail = mBinding.registerEmail.getText().toString();
        String pwd = mBinding.registerPassword.getText().toString();

        if(fullName.isEmpty()){
            mBinding.registerName.setError(getContext().getString(R.string.name_not_empty));
            mBinding.registerName.requestFocus();

        }else if(sMail.isEmpty()){
            mBinding.registerEmail.setError(getContext().getString(R.string.email_not_empty));
            mBinding.registerEmail.requestFocus();

        }else if (pwd.isEmpty()){
            mBinding.registerPassword.setError(getContext().getString(R.string.password_not_empty));
            mBinding.registerPassword.requestFocus();

        }else if(!isValidPassword(pwd)) {
            mBinding.registerPassword.setError(getContext().getString(R.string.invalid_password));
            Toast.makeText(getContext(), getContext().getString(R.string.password_requirements), Toast.LENGTH_LONG).show();
            mBinding.registerPassword.requestFocus();
        }else{

            firebaseAuth.createUserWithEmailAndPassword(sMail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String userID = firebaseAuth.getCurrentUser().getUid();
                        UserHelper user = new UserHelper(fullName, sMail);

                        // set the realtime DB
                        reference.child(userID).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Firestore", "Successo");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Firestore", "Errore! DB non scritto");
                            }
                        });

                        Toast.makeText(getContext(), getContext().getString(R.string.successfull_registration), Toast.LENGTH_LONG).show();
                        navController.navigate(R.id.action_registerFragment_to_loginFragment);
                        firebaseAuth.signOut();

                    }else{
                        Toast.makeText(getContext(), getContext().getString(R.string.registration_error) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public boolean isValidPassword(String password){
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}