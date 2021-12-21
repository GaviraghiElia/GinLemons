package it.unimib.ginlemons.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.utils.UserHelper;

public class UserProfileFragment extends Fragment {

    private TextInputEditText name, email;
    private String displayName, displayEmail;
    private Button updateButton;
    private FirebaseAuth mAuth;
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

        // Istanza sessione user
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        // Istanza real time DB
        fDB = FirebaseDatabase.getInstance("https://ginlemons-6adb3-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = fDB.getReference("users");

        // Listener per dati Real Time sempre aggiornati
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // Vogliamo solo i dati dell'utente
                    if(snapshot.getKey().equals(userID)) {
                        // particolare: la snapshot è il risultato di una hasMap con <Chiave, Valore>
                        // è compatibile con l'oggetto userHelper creato ad hoc per recuperarne i parametri
                        UserHelper userHelper = snapshot.getValue(UserHelper.class);
                        displayName = userHelper.getName();
                        displayEmail = userHelper.getEmail();
                        name.setText(displayName);
                        email.setText(displayEmail);
                        //Log.d("Snapshot", "Chiave " + snapshot.getKey());
                        //Log.d("Snapshot",  "Nome" + snapshot.getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });

        // l'utente vuole fare l'update delle informazioni
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Stringhe al momento dell'Onclick
                String onClickName = name.getText().toString();
                String onClickMail = email.getText().toString().toLowerCase();
                Log.d("Snapshot", "Click update button");

                if(onClickName.isEmpty()){
                    name.setError("Name cannot be empty");
                    name.requestFocus();

                }else if(onClickMail.isEmpty()){
                    email.setError("Email cannot be empty!");
                    email.requestFocus();

                } else if (!displayEmail.equals(onClickMail)){
                    Log.d("Snapshot", "Not empty");
                    // non sono vuoti, andiamo ad aggiornare
                    UserHelper userObj = new UserHelper(onClickName, onClickMail);

                    // vuole cambiare mail? Allora si deve riautenticare sto infame
                    // non c'è sensitive su maiuscole e minuscole

                        Log.d("Snapshot", "Email changed, create dialog");
                        Log.d("Snapshot", "OnclickMail" + onClickMail + " DisplayMail = " + displayEmail);

                        // Dialog per inserimento password - deve autenticarsi prima di fare il change mail
                        /*EditText insertPasswordChangeEmail = new EditText(v.getContext());
                        AlertDialog.Builder insertPasswordUpdateMailDialog = new AlertDialog.Builder(getActivity());
                        insertPasswordUpdateMailDialog.setTitle("Want change your Email?");
                        insertPasswordUpdateMailDialog.setMessage("Enter Your Password");
                        insertPasswordUpdateMailDialog.setView(insertPasswordChangeEmail);
                        */

                        // Preme "si"
                        //insertPasswordUpdateMailDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            //@Override
                            //public void onClick(DialogInterface dialog, int which) {
                                // recupero la password inserita
                                String passwordInsert = "elia123";  //insertPasswordChangeEmail.getText().toString();

                                // recupero l'istanza dell'utente FireBase
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                Log.d("Snapshot", "Get User");
                                // Re-autenticazione dell'utente: è richiesto da Firebase, non si può aggiornare senza.

                                // Credenziali
                                Log.d("Snapshot", "Get credential");
                                AuthCredential credential = EmailAuthProvider
                                        .getCredential(displayEmail, passwordInsert); // Current Login Credentials \\
                                //riautenticazione
                                 Log.d("Snapshot", "Reauthenticate");
                                firebaseUser.reauthenticate(credential)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Log.d("Snapshot", "User re-authenticated success");

                                                //Ora si può fare l'update
                                                firebaseUser.updateEmail(onClickMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        // update the Real Time DB
                                                        reference.child(userID).setValue(userObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                // abbiamo finito
                                                                displayEmail = onClickMail;
                                                                displayName = onClickName;
                                                                Log.d("Snapshot", "Update Real time DB");
                                                                Toast.makeText(getContext(), "Your information updated", Toast.LENGTH_SHORT).show();
                                                                mAuth.signOut();
                                                                if(!checkSession()){
                                                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.d("Snapshot", "User re-authenticated failed");
                                                                Toast.makeText(getContext(), "Information not updated : please login", Toast.LENGTH_LONG).show();
                                                            }
                                                        });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Log.d("Snapshot", "User update mail failed");
                                                        Toast.makeText(getContext(), "Password is not correct", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            //} // fine Onlick positive button
                        //}); // fine setPositiveButton button

                        // Preme No
                        /*insertPasswordUpdateMailDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        insertPasswordUpdateMailDialog.create();
                        */

                    }else if(!displayName.equals(onClickName)){ // NON ha cambiato la mail, più easy
                        Log.d("Snapshot", "Name changed, not email");
                        UserHelper userObj = new UserHelper(onClickName, onClickMail);
                        // change realtime DB
                        reference.child(userID).setValue(userObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getContext(), "Name updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Name not updated", Toast.LENGTH_LONG).show();
                            }
                        });
                    }else {
                        Log.d("Snapshot", "Non è cambiato nulla");
                        Toast.makeText(getContext(), "No changes", Toast.LENGTH_LONG).show();
                }

            } // fine onlick
        });

        return view;
    }

    private boolean checkSession(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            return false;
        }
        return true;
    }

}