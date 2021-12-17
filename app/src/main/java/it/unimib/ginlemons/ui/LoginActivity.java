package it.unimib.ginlemons.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import it.unimib.ginlemons.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private Button login;
    private Button signUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passwordLogin);
        login = findViewById(R.id.buttonLogin);
        signUp = findViewById(R.id.buttonSignUp);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(view -> {
            loginUser();
        });

        signUp.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            this.finish();
         });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void loginUser(){
        String e = email.getText().toString();
        String pwd = password.getText().toString();

        if(TextUtils.isEmpty(e)){
            email.setError("Email cannot be empty");
            email.requestFocus();
        }else if (TextUtils.isEmpty(pwd)){
            password.setError("Passowrd cannot be empty");
            password.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(e, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "User login is successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }else{
                        String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                        Toast.makeText(LoginActivity.this, "Login Error :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        /*if (errorCode.equals("ERROR_WRONG_PASSWORD") | errorCode.equals("ERROR_WEAK_PASSWORD")) {
                            /*Snackbar.make(findViewById(android.R.id.content), task.getException().getMessage(), Snackbar.LENGTH_LONG).setAction("Reset Password", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //EditText resetMail = new EditText(v.getContext());
                                    //AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                                    //passwordResetDialog.setTitle("Reset your password");

                                }
                            }).show();
                        }*/
                    }
                }
            });
        }

    }

}