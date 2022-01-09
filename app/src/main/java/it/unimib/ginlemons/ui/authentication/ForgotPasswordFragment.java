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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.databinding.FragmentForgotPasswordBinding;

public class ForgotPasswordFragment extends Fragment {

    private FirebaseAuth mAuth;
    private NavController navController;
    private FragmentForgotPasswordBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        mAuth = FirebaseAuth.getInstance();
        navController = NavHostFragment.findNavController(this);


        mBinding.resetMail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBinding.resetButton.setEnabled(!mBinding.resetMail.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mBinding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(mBinding.resetMail.getText().toString());
            }
        });

        mBinding.returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
            }
        });


        return view;
    }

    public void resetPassword(String email){
        if(email.isEmpty()){
            mBinding.resetMail.setError("Email cannot be empty!");
        } else {

            mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getContext(), "Reset link sent to your mail",
                            Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_forgotPasswordFragment_to_loginFragment);
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

}
