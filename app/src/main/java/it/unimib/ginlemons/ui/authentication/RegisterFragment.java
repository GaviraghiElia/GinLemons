package it.unimib.ginlemons.ui.authentication;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.ui.LoginActivity;
import it.unimib.ginlemons.ui.RegisterActivity;
import it.unimib.ginlemons.utils.UserHelper;

public class RegisterFragment extends Fragment {

    private TextInputEditText name;
    private TextInputEditText email;
    private TextInputEditText password;
    private Button registerButton, signIn;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;
    private String userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        name = view.findViewById(R.id.registerName);
        email = view.findViewById(R.id.registerEmail);
        password = view.findViewById(R.id.registerPassword);
        registerButton = view.findViewById(R.id.buttonRegister);
        signIn = view.findViewById(R.id.buttonSignIn);

        firebaseAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance("https://ginlemons-6adb3-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = fDB.getReference("users");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        return view;
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

                        Toast.makeText(getContext(), "User registered is successfully", Toast.LENGTH_LONG).show();
                        Navigation.findNavController(requireView()).navigate(R.id.action_registerFragment_to_loginFragment);
                        firebaseAuth.signOut();

                    }else{
                        Toast.makeText(getContext(), "Registration Error :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}