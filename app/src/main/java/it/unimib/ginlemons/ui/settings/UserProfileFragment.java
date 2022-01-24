package it.unimib.ginlemons.ui.settings;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.databinding.CustomEmailDialogBinding;
import it.unimib.ginlemons.databinding.CustomPasswordDialogBinding;
import it.unimib.ginlemons.databinding.FragmentUserProfileBinding;
import it.unimib.ginlemons.model.UserHelper;
import it.unimib.ginlemons.ui.authentication.UserViewModel;

public class UserProfileFragment extends Fragment
{
    private String displayName, displayEmail;
    private FirebaseAuth mAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;
    private FragmentUserProfileBinding mBinding;
    private NavController navController;
    private UserViewModel mUserViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mUserViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    // Vogliamo solo i dati dell'utente
                    if(mAuth.getCurrentUser() != null)
                    {
                        if (Objects.equals(snapshot.getKey(), mAuth.getCurrentUser().getUid()))
                        {
                            // particolare: la snapshot è il risultato di una hasMap con <Chiave, Valore>
                            // è compatibile con l'oggetto userHelper creato ad hoc per recuperarne i parametri
                            UserHelper userHelper = snapshot.getValue(UserHelper.class);
                            if(userHelper != null) {
                                displayName = userHelper.getName();
                                displayEmail = userHelper.getEmail();
                                mBinding.helloUserProfile.setText("Hello " + displayName + "!");
                                mBinding.userProfileName.setText(displayName);
                                mBinding.userProfileEmail.setText(displayEmail);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {
                Toast.makeText(getContext(), "Fail to get data", Toast.LENGTH_SHORT).show();
            }
        });

        // Recupera la password
        mBinding.userForgetPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                resetPassword();
            }
        });

        // l'utente vuole fare l'update delle informazioni
        mBinding.updateUserButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                changeClickButton();
            }
        });

        return view;
    }

    // Inserisce il pulsante per il logout nella toolbar
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.user_profile_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onResume()
    {
        setTitleToolbar();
        super.onResume();
    }

    public void setTitleToolbar()
    {
        if(getActivity() != null) {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_toolbar);
            if(toolbar != null)
            {
                toolbar.setTitle(R.string.user_profile_toolbar_title);
            }
        }
    }

    public void changeClickButton()
    {
        // Stringhe al momento dell'Onclick
        String onClickName = mBinding.userProfileName.getText().toString();
        String onClickMail = mBinding.userProfileEmail.getText().toString().toLowerCase();

        if(onClickName.isEmpty())
        {
            mBinding.userProfileName.setError(getString(R.string.name_not_empty));
            mBinding.userProfileName.requestFocus();
        }
        else
            if(onClickMail.isEmpty())
            {
                mBinding.userProfileEmail.setError(getContext().getString(R.string.email_not_empty));
                mBinding.userProfileEmail.requestFocus();
            }
            else
                if(!isValidEmail(onClickMail))
                {
                    mBinding.userProfileEmail.setError(getContext().getString(R.string.bad_email));
                    mBinding.userProfileEmail.requestFocus();
                }
                else
                    if(!displayName.equals(onClickName) && displayEmail.equals(onClickMail))
                    {
                        // NON ha cambiato la mail, più easy
                        changeOnlyName(new UserHelper(onClickName, displayEmail));
                    }
                    else
                        if(!displayEmail.equals(onClickMail))
                        {
                            // vuole cambiare mail? Allora si deve riautenticare
                            changeEmailName(onClickName, onClickMail);
                        }
                        else
                            Toast.makeText(getContext(), getString(R.string.no_changes), Toast.LENGTH_LONG).show();
    }

    // check pattern email
    public boolean isValidEmail(String email)
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void resetPassword()
    {
        CustomEmailDialogBinding mBindingDialog = CustomEmailDialogBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(mBindingDialog.getRoot());
        dialog.setCancelable(true);
        // enable button yes if password not empty
        mBindingDialog.resetInputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String email = mBindingDialog.resetInputEmail.getText().toString();
                mBindingDialog.yesButtonResetEmail.setEnabled(!email.isEmpty() && isValidEmail(email));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mBindingDialog.noButtonResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        mBindingDialog.yesButtonResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String emailReset = mAuth.getCurrentUser().getEmail();
                String emailInput = mBindingDialog.resetInputEmail.getText().toString();

                if(!emailInput.equals(emailReset))
                    mBindingDialog.resetInputEmailLayout.setError(getString(R.string.incorrect_email));
                else
                {
                    mUserViewModel.resetPasswordLink(emailInput).observe(getViewLifecycleOwner(), firebaseResponse -> {
                        if(firebaseResponse != null)
                        {
                            if(firebaseResponse.isSuccess())
                            {
                                makeMessage(getResources().getString(R.string.password_reset_link));
                                mAuth.signOut();
                                if(!checkSession())
                                {
                                    dialog.dismiss();
                                    navController.navigate(R.id.action_userProfileFragment_to_authenticationActivity);
                                    requireActivity().finish();
                                }
                            }
                            else
                            {
                                makeMessage(firebaseResponse.getMessage());
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });

        dialog.show();
    }

    public void changeOnlyName(UserHelper userObj)
    {
        Log.d("Snapshot", "Name changed, not email");

        // change realtime DB
        mUserViewModel.changeName(userObj).observe(getViewLifecycleOwner(), firebaseResponse -> {
            if(firebaseResponse != null)
            {
                if(firebaseResponse.isSuccess())
                    makeMessage(getString(R.string.informations_updated));
                else
                    makeMessage(firebaseResponse.getMessage());
            }
        });
    }

    public void changeEmailName(String onClickName, String onClickMail)
    {
        CustomPasswordDialogBinding mBindingDialog = CustomPasswordDialogBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(mBindingDialog.getRoot());
        dialog.setCancelable(true);

        // enable button yes if password not empty
        mBindingDialog.resetInputEmailPassword.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mBindingDialog.yesButtonResetEmailPassword.setEnabled(!mBindingDialog.resetInputEmailPassword.getText().toString().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // preme no
        mBindingDialog.noButtonResetEmailPassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                mUserViewModel.clear();
            }
        });

        // preme si
        mBindingDialog.yesButtonResetEmailPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(mBindingDialog.resetInputEmailPassword.getText().toString().isEmpty())
                {
                    mBindingDialog.resetInputEmailPasswordLayout.setError(getString(R.string.email_not_empty));
                    mBindingDialog.resetInputEmailPassword.requestFocus();
                }
                else
                {
                    UserHelper userObj = new UserHelper(onClickName, onClickMail);
                    String passwordInsert = mBindingDialog.resetInputEmailPassword.getText().toString();

                    //reauthentication
                    mUserViewModel.clear();
                    mUserViewModel.reauthenticateUser(userObj, displayEmail, passwordInsert).observe(getViewLifecycleOwner(), firebaseResponse -> {
                        if(firebaseResponse != null)
                        {
                            if(firebaseResponse.isSuccess())
                            {
                                dialog.dismiss();
                                mAuth.signOut();
                                navController.navigate(R.id.action_userProfileFragment_to_authenticationActivity);
                                requireActivity().finish();
                            }
                            else
                            {
                                //makeMessage(firebaseResponse.getMessage());
                                //dialog.dismiss();
                                mUserViewModel.clear();
                                mBindingDialog.resetInputEmailPassword.setError(getString(R.string.incorrect_password));
                            }
                        }
                    });
                }
            }
        });

        dialog.show();
    }

    private void makeMessage(String message)
    {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        mUserViewModel.clear();
    }

    private boolean checkSession()
    {
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
            return true;

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        if(item.getItemId() == R.id.action_logout)
        {
            mAuth.signOut();
            navController.navigate(R.id.action_userProfileFragment_to_authenticationActivity);
            requireActivity().finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}