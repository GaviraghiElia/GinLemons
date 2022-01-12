package it.unimib.ginlemons.ui.settings;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import it.unimib.ginlemons.databinding.CustomPasswordDialogBinding;
import it.unimib.ginlemons.databinding.FragmentUserProfileBinding;
import it.unimib.ginlemons.model.UserHelper;

public class UserProfileFragment extends Fragment {

    private String displayName, displayEmail;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;
    private FragmentUserProfileBinding mBinding;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentUserProfileBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();
        navController = NavHostFragment.findNavController(this);

        setTitleToolbar();
        setHasOptionsMenu(true);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        reference = fDB.getReference("users");

        // Listener per dati Real Time sempre aggiornati
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    // Vogliamo solo i dati dell'utente
                    if(snapshot.getKey().equals(mAuth.getCurrentUser().getUid())) {

                        // particolare: la snapshot è il risultato di una hasMap con <Chiave, Valore>
                        // è compatibile con l'oggetto userHelper creato ad hoc per recuperarne i parametri
                        UserHelper userHelper = snapshot.getValue(UserHelper.class);
                        displayName = userHelper.getName();
                        displayEmail = userHelper.getEmail();
                        mBinding.helloUserProfile.setText("Hello " + displayName + "!");
                        mBinding.userProfileName.setText(displayName);
                        mBinding.userProfileEmail.setText(displayEmail);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), getString(R.string.get_data_fail), Toast.LENGTH_SHORT).show();
            }
        });

        // recupera la password
        mBinding.userForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        // l'utente vuole fare l'update delle informazioni
        mBinding.updateUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeClickButton();
            }
        });

        return view;
    }

    // Inserisce il pulsante per il logout nella toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.user_profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onResume() {
        setTitleToolbar();
        super.onResume();
    }

    public void setTitleToolbar() {
        /*
        ActivityMainBinding mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mainBinding.activityToolbar.setTitle(R.string.user_profile_toolbar_title);
        */
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_toolbar);
        toolbar.setTitle(R.string.user_profile_toolbar_title);
    }

    public void changeClickButton(){
        // Stringhe al momento dell'Onclick
        String onClickName = mBinding.userProfileName.getText().toString();
        String onClickMail = mBinding.userProfileEmail.getText().toString().toLowerCase();

        if(onClickName.isEmpty()){
            mBinding.userProfileName.setError(getString(R.string.name_not_empty));
            mBinding.userProfileName.requestFocus();

        }else if(onClickMail.isEmpty()){
            mBinding.userProfileEmail.setError(getString(R.string.email_not_empty));
            mBinding.userProfileEmail.requestFocus();

        } else if(!displayName.equals(onClickName) && displayEmail.equals(onClickMail)){
            // NON ha cambiato la mail, più easy
            changeOnlyName(new UserHelper(onClickName, onClickMail));

        } else if(!displayEmail.equals(onClickMail)){
            // vuole cambiare mail? Allora si deve riautenticare sto infame
            changeEmailName(onClickName, onClickMail);

        }else {
            Toast.makeText(getContext(), getString(R.string.no_changes), Toast.LENGTH_LONG).show();
        }
    }

    public void changeOnlyName(UserHelper userObj){
        Log.d("Snapshot", "Name changed, not email");
        // change realtime DB
        reference.child(mAuth.getCurrentUser().getUid()).setValue(userObj).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), getString(R.string.name_updated), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), getString(R.string.name_not_updated), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void changeEmailName(String onClickName, String onClickMail){

        CustomPasswordDialogBinding mBindingDialog = CustomPasswordDialogBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(mBindingDialog.getRoot());
        dialog.setCancelable(true);

        // enable button yes if password not empty
        mBindingDialog.resetInputEmailPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mBindingDialog.yesButtonResetEmailPassword.setEnabled(!mBindingDialog.resetInputEmailPassword.getText().toString().isEmpty());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // preme no
        mBindingDialog.noButtonResetEmailPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // preme si
        mBindingDialog.yesButtonResetEmailPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBindingDialog.resetInputEmailPassword.getText().toString().isEmpty()){
                    mBindingDialog.resetInputEmailPasswordLayout.setError(getString(R.string.password_not_empty));
                    mBindingDialog.resetInputEmailPassword.requestFocus();
                } else {
                    UserHelper userObj = new UserHelper(onClickName, onClickMail);
                    String passwordInsert = mBindingDialog.resetInputEmailPassword.getText().toString();

                    // Credenziali
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(displayEmail, passwordInsert); // Current Login Credentials \\

                    //reauthentication
                    FirebaseUser firebaseUser = mAuth.getCurrentUser();
                    firebaseUser.reauthenticate(credential)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    // update mail firebaseUser
                                    firebaseUser.updateEmail(onClickMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {

                                            // update the Real Time DB
                                            reference.child(mAuth.getCurrentUser().getUid()).setValue(userObj).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    // abbiamo finito

                                                    // update display attribute
                                                    displayEmail = onClickMail;
                                                    displayName = onClickName;

                                                    dialog.dismiss();
                                                    Toast.makeText(getContext(), getString(R.string.informations_updated), Toast.LENGTH_SHORT).show();

                                                    mAuth.signOut();
                                                    if(!checkSession()){
                                                        navController.navigate(R.id.action_userProfileFragment_to_authenticationActivity);
                                                        getActivity().finish();
                                                    }
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            dialog.dismiss();
                                            mBinding.userProfileEmail.setError(getString(R.string.bad_email));
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show();
                            mBindingDialog.resetInputEmailPasswordLayout.setError(getString(R.string.incorrect_password));
                            mBindingDialog.resetInputEmailPassword.requestFocus();
                        }
                    });
                }
            }
        });

        dialog.show();

    }

    public void resetPassword(){
        AlertDialog.Builder resetPasswordMailDialog = new AlertDialog.Builder(requireActivity());
        resetPasswordMailDialog.setTitle(getString(R.string.forgot_password));
        resetPasswordMailDialog.setCancelable(true);
        // Preme "si"
        resetPasswordMailDialog.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String emailReset = mAuth.getCurrentUser().getEmail();
                mAuth.sendPasswordResetEmail(emailReset).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getContext(), getString(R.string.password_reset_link), Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        if(!checkSession()){
                            navController.navigate(R.id.action_userProfileFragment_to_authenticationActivity);
                            getActivity().finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), getString(R.string.password_reset_link_error)
                                + e.getMessage(), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        resetPasswordMailDialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = resetPasswordMailDialog.create();
        alertDialog.show();
    }


    private boolean checkSession(){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            return false;
        }
        return true;
    }

}