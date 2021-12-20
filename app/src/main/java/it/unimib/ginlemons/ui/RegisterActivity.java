package it.unimib.ginlemons.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.utils.UserHelper;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button registerButton, signIn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.registerName);
        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        registerButton = findViewById(R.id.buttonRegister);
        signIn = findViewById(R.id.buttonSignIn);

        firebaseAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance("https://ginlemons-6adb3-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = fDB.getReference("users");


        registerButton.setOnClickListener(view ->{
            createUser();
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void createUser() {
        String fullName = name.getText().toString();
        String sMail = email.getText().toString();
        String pwd = password.getText().toString();

        if(fullName.isEmpty()){
            name.setError("Name cannot be empty");
            name.requestFocus();

        }else if(sMail.isEmpty()){
            email.setError("Email cannot be empty");
            email.requestFocus();

        }else if (pwd.isEmpty()){
            password.setError("Passowrd cannot be empty");
            password.requestFocus();

        }else if(pwd.length() < 7) {
            password.setError("Passowrd must be more than 6 characters long");
            password.requestFocus();
        }else{


            firebaseAuth.createUserWithEmailAndPassword(sMail, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        userID = firebaseAuth.getCurrentUser().getUid();
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

                        Toast.makeText(RegisterActivity.this, "User registered is successfully", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        firebaseAuth.signOut();

                    }else{
                        Toast.makeText(RegisterActivity.this, "Registration Error :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}