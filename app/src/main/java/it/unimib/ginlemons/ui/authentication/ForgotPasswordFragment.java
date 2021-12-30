package it.unimib.ginlemons.ui.authentication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.ginlemons.R;

public class ForgotPasswordFragment extends Fragment {

    private TextInputEditText emailReset;
    private Button resetButton;
    private Button returnLoginButton;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        emailReset = view.findViewById(R.id.resetMail);
        resetButton = view.findViewById(R.id.resetButton);
        returnLoginButton = view.findViewById(R.id.returnToLogin);
        mAuth = FirebaseAuth.getInstance();

        emailReset.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                resetButton.setEnabled(!emailReset.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailReset.getText().toString();
                if(email.isEmpty()){
                    emailReset.setError("Email cannot be empty!");
                }else{
                    mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Reset link sent to your mail",
                                    Toast.LENGTH_SHORT).show();
                            NavController navController = NavHostFragment.findNavController(ForgotPasswordFragment.this);
                            navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
                            //Navigation.findNavController(view).navigate(R.id.action_forgotPasswordFragment_to_loginFragment);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Error ! Reset Link is not sent"
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

        returnLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = NavHostFragment.findNavController(ForgotPasswordFragment.this);
                navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
            }
        });


        return view;
    }
}