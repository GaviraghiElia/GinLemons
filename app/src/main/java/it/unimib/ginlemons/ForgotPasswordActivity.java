package it.unimib.ginlemons;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import it.unimib.ginlemons.ui.LoginActivity;
import it.unimib.ginlemons.ui.MainActivity;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailReset;
    private Button resetButton;
    private Button returnLoginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailReset = findViewById(R.id.resetMail);
        resetButton = findViewById(R.id.resetButton);
        returnLoginButton = findViewById(R.id.returnToLogin);
        mAuth = FirebaseAuth.getInstance();

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
                            Toast.makeText(ForgotPasswordActivity.this, "Reset link sent to your mail", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                            overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_left);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotPasswordActivity.this, "Error ! Reset Link is not sent"
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

        returnLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}