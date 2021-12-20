package it.unimib.ginlemons.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.utils.UserHelper;

public class UserProfileFragment extends Fragment {

    private TextInputEditText name, email;
    private Button updateButton;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private String userID;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_profile, container, false);
        name = view.findViewById(R.id.userProfileName);
        email = view.findViewById(R.id.userProfileEmail);
        updateButton = view.findViewById(R.id.updateUserButton);
        progressBar = view.findViewById(R.id.progressBarUpdate);

        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        fDB = FirebaseDatabase.getInstance("https://ginlemons-6adb3-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = fDB.getReference("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.getKey().equals(userID)) {
                        UserHelper userHelper = snapshot.getValue(UserHelper.class);
                        name.setText(userHelper.getName());
                        email.setText(userHelper.getEmail());
                        Log.d("Snapshot", "Chiave " + snapshot.getKey());
                        Log.d("Snapshot",  "Nome" + snapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sName = name.getText().toString();
                String sEmail = email.getText().toString();
                if(sName.isEmpty()){
                    name.setError("Name cannot be empty");
                    name.requestFocus();
                }else if(sEmail.isEmpty()){
                    email.setError("Email cannot be empty!");
                    email.requestFocus();
                } else {
                    UserHelper user = new UserHelper(sName, sEmail);
                    // set the realtime DB

                    reference.child(userID).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Information updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Information not updated", Toast.LENGTH_LONG).show();
                        }
                    });



                }
            }
        });

        return view;
    }

}