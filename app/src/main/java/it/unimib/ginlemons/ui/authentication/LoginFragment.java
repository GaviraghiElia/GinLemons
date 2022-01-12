package it.unimib.ginlemons.ui.authentication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private NavController navController;
    private FragmentLoginBinding mBinding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentLoginBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        navController = NavHostFragment.findNavController(this);

        mBinding.loginEmail.addTextChangedListener(loginTextWatcher);
        mBinding.loginPassword.addTextChangedListener(loginTextWatcher);

        mAuth = FirebaseAuth.getInstance();

        mBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(v);
            }
        });

        mBinding.buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_registerFragment);
            }
        });

        // button forget passowrd
        mBinding.forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_loginFragment_to_forgotPasswordFragment);
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
            String usernameInput = mBinding.loginEmail.getText().toString();
            String passwordInput = mBinding.loginPassword.getText().toString();
            mBinding.buttonLogin.setEnabled(!usernameInput.isEmpty() && !passwordInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    private void loginUser(View view) {
        String e = mBinding.loginEmail.getText().toString();
        String pwd = mBinding.loginPassword.getText().toString();

        if (TextUtils.isEmpty(e)) {
            mBinding.loginEmail.setError(getContext().getString(R.string.email_not_empty));
            mBinding.loginEmail.requestFocus();
        } else if (TextUtils.isEmpty(pwd)) {
            mBinding.loginPassword.setError(getContext().getString(R.string.password_not_empty));
            mBinding.loginPassword.requestFocus();
        }else if (pwd.isEmpty()){
            mBinding.loginPassword.setError(getContext().getString(R.string.password_too_short));
            mBinding.loginPassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(e, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getContext(), getContext().getString(R.string.successfull_login), Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_loginFragment_to_mainActivity);
                        requireActivity().finish();
                    }else{
                        Toast.makeText(getContext(), getContext().getString(R.string.login_error) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

}