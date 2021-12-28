package it.unimib.ginlemons.ui.authentication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.ginlemons.R;

public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;
    private Button login, signUp, forgetPassword;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        email = view.findViewById(R.id.loginEmail);
        password = view.findViewById(R.id.loginPassword);
        login = view.findViewById(R.id.buttonLogin);
        signUp = view.findViewById(R.id.buttonSignUp);
        forgetPassword = view.findViewById(R.id.forgetPassword);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });


        // button forget passowrd
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
            }
        });
        return view;
    }


    private void loginUser(View view) {
        String e = email.getText().toString();
        String pwd = password.getText().toString();

        if (TextUtils.isEmpty(e)) {
            email.setError("Email cannot be empty");
            email.requestFocus();
        } else if (TextUtils.isEmpty(pwd)) {
            password.setError("Passowrd cannot be empty");
            password.requestFocus();
        }else if (pwd.isEmpty()){
            password.setError("Password must be >= 6 characters");
            password.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(e, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), "User login is successfully", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_mainActivity);
                        requireActivity().finish();
                    }else{
                        Toast.makeText(getContext(), "Login Error :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}