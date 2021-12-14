package it.unimib.ginlemons.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.TransitionInflater;
import android.widget.TextView;

import it.unimib.ginlemons.R;


public class RicetteInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ricette_info);

        Intent intent = getIntent();

        String name = intent.getStringExtra(RicetteDiscoverFragment.ITEM_NAME_PRESSED_KEY);
        int alcool = intent.getIntExtra(RicetteDiscoverFragment.ITEM_ALCOOL_PRESSED_KEY, 0);
        int costo = intent.getIntExtra(RicetteDiscoverFragment.ITEM_LEVEL_PRESSED_KEY, 0);

        TextView textViewName = findViewById(R.id.nomeRicettaInfo);
        TextView textViewAlcool = findViewById(R.id.alcoolRicettaInfo);
        TextView textViewCosto = findViewById(R.id.costoRicettaInfo);
        TextView textViewDescrizione = findViewById(R.id.descrizioneRicettaInfo);

        textViewName.setText(name);
        textViewAlcool.setText(Integer.toString(alcool) + " %");
        if(costo == 1){
            textViewCosto.setText("€");
        }else if(costo == 2){
            textViewCosto.setText("€€");
        }else{
            textViewCosto.setText("€€€");
        }

        textViewDescrizione.setText("Abbiamo chiesto a un barman professionista di preparare con noi la ricetta dello Spritz, o meglio dell'Aperol Spritz, codificato dal 2011 come Italian Spritz o Venetian Spritz.\n" +
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
}