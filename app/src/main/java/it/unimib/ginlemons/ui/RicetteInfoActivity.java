package it.unimib.ginlemons.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.databinding.ActivityRicetteInfoBinding;


public class RicetteInfoActivity extends AppCompatActivity {

    private ActivityRicetteInfoBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRicetteInfoBinding.inflate(getLayoutInflater());
        View view = mBinding.getRoot();
        setContentView(view);


        Intent intent = getIntent();

        String name = intent.getStringExtra(RicetteDiscoverFragment.ITEM_NAME_PRESSED_KEY);
        int alcool = intent.getIntExtra(RicetteDiscoverFragment.ITEM_ALCOOL_PRESSED_KEY, 0);
        int costo = intent.getIntExtra(RicetteDiscoverFragment.ITEM_LEVEL_PRESSED_KEY, 0);

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

        // Codice Toolbar -- ultima push
        mBinding.activityInfoToolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_new_24);
        setSupportActionBar(mBinding.activityInfoToolbar);
        mBinding.activityInfoToolbar.setTitle(name);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);

        // Set campi textView

        mBinding.alcoolRicettaInfo.setText(Integer.toString(alcool) + " %");
        if(costo == 1){
            mBinding.costoRicettaInfo.setText("€");
        }else if(costo == 2){
            mBinding.costoRicettaInfo.setText("€€");
        }else{
            mBinding.costoRicettaInfo.setText("€€€");
        }

        mBinding.descrizioneRicettaInfo.setText("Abbiamo chiesto a un barman professionista di preparare con noi la ricetta dello Spritz, o meglio dell'Aperol Spritz, codificato dal 2011 come Italian Spritz o Venetian Spritz.\n" +
                "\n" +
                "L' Aperol Spritz è infatti la famosa versione dello Spritz che ha origini controverse.\n" +
                "\n" +
                "Ecco cosa c'è da sapere su questo cocktail da aperitivo celebre in tutto il mondo: il bicchiere da usare, gli ingredienti, qualche accortezza e curiosità.\n" +
                "\n" +
                "Seguite passo passo la ricetta per preparare l'Aperol Spritz a casa: vi bastano solo 5 minuti. Gli ingredienti sono davvero facili da reperire: 3 parti di Prosecco, 2 di liquore e un po’ di soda. Un consiglio: versate subito il ghiaccio nel bicchiere, in questo modo il Prosecco conserverà meglio la sua parte frizzante.\n" +
                "\n" +
                "Qualche curiosità sulla storia dello Spritz: si racconta sia stato creato dai soldati austriaci di stanza in Triveneto nell'Ottocento, durante l'occupazione negli anni precedenti all'Unità d'Italia; infatti le truppe austriache non erano abituate al tenore alcolico dei vini veneti, che invece gli autoctoni ben sopportavano per tradizione e abitudini culturali. Non a caso, il nome Spritz rimanda al verbo tedesco spritzen, equivalente di \"spruzzare\", che indica appunto l'azione dell'allungamento del vino con l'acqua gasata usando la vecchia pistola da selz.\n" +
                "\n" +
                "La ricetta originale dello Spritz, risalente agli anni '20 -30 e contesa tra Padova, Venezia e Treviso (terra di Prosecco) prevede quindi pari quantità di soda e vino bianco, da non confondere con la ben più famosa variante con l'Aperol, che vi mostriamo noi.\n" +
                "\n" +
                "L'Aperol Spritz si diffode negli anni '40-50, prima a Venezia e poi a Padova: lo Spritz, preparato con il vino veneto bianco frizzante e acqua gasata, inizia ad essere macchiato con una dose di liquore dal tipico colore arancio. L'Aperol, aperitivo alcolico bitter, marchio di Campari dal 2003, nasce infatti a Bassano del Grappa nel 1919 (poi presentato alla Fiera di Padova) ad opera dei fratelli Barbieri.\n" +
                "\n" +
                "Lo Spritz è il cocktail da aperitivo per eccellenza. Per un aperitivo a casa ecco 15 ricette perfette da abbinare.\n" +
                "\n" +
                "ALTRI COCKTAIL DA PROVARE: Mojito, Negroni, Bloody Mary, Moscow Mule, Margarita, Daiquiri, Dry martini, Manhattan, Cuba Libre\n" +
                "\n" +
                "Ecco quali sono i 10 attrezzi imperdibili per preparare ottimi cocktail a casa vostra.");
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}