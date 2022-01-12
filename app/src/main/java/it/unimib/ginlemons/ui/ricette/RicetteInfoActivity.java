package it.unimib.ginlemons.ui.ricette;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.databinding.ActivityRicetteInfoBinding;
import it.unimib.ginlemons.repository.GetRecipeRepository;
import it.unimib.ginlemons.repository.IGetRecipeRepository;
import it.unimib.ginlemons.utils.Constants;
import it.unimib.ginlemons.utils.ResponseCallback;
import it.unimib.ginlemons.utils.Ricetta;

public class RicetteInfoActivity extends AppCompatActivity implements ResponseCallback {

    private ActivityRicetteInfoBinding mBinding;
    private String fragmentProvenienza;
    private IGetRecipeRepository iGetRecipeRepository;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iGetRecipeRepository = new GetRecipeRepository(this.getApplication(), this);

        mBinding = ActivityRicetteInfoBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);


        Intent intent = getIntent();
        fragmentProvenienza = intent.getStringExtra(Constants.FRAGMENTFORTRANSITION);
        id = intent.getStringExtra(Constants.ITEM_ID_PRESSED_KEY);

        iGetRecipeRepository.getRecipeById(id);

        // serve almeno la versione 23, noi lavoriamo con la 21
        // non c'è un gran divario
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mBinding.scrollViewInfo.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY + 5 && mBinding.addToListExtButton.isShown()) {
                        mBinding.addToListExtButton.shrink();
                    }else if (scrollY < oldScrollY - 20) {
                        mBinding.addToListExtButton.extend();
                    } else if (scrollY == 0) {
                        mBinding.addToListExtButton.extend();
                    }
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();

        // transition in base al fragment di provenienza taaac
        if(fragmentProvenienza.equals("RicettePreferiti")) {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }else{
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // Metodi che gestiscono i dati ricevuti dalle chiamate all'API
    // Aggiunge una ricetta dettagliata alla lista della RecyclerView
    @Override
    public void onResponse(Ricetta recipe) {

        if(recipe != null)
        {
            mBinding.nomeRicettaInfo.setText(recipe.getName());

            // Codice Toolbar -- ultima push
            mBinding.activityInfoToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
            setSupportActionBar(mBinding.activityInfoToolbar);
            mBinding.activityInfoToolbar.setTitle(recipe.getName());
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);


            mBinding.descrizioneRicettaInfo.setText(recipe.getIstruzioni());
        }
    }

    // In caso di fallimento della chiamata avviso l'utente con un messaggio
    // nella snackbar
    @Override
    public void onFailure(String errorString) {
        Snackbar msg = Snackbar.make(this.findViewById(android.R.id.content), errorString, Snackbar.LENGTH_LONG);

        // Appare anche un pulsante che permette di riprovare la chiamata
        msg.setAction( getString(R.string.retry), new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                iGetRecipeRepository.getRecipeById(id);
            }
        });

        msg.show();
    }
}