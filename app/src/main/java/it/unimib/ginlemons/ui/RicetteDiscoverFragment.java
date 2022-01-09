package it.unimib.ginlemons.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.transition.Fade;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.ListeRecyclerViewAdapter;
import it.unimib.ginlemons.repository.IRecipeRepository;
import it.unimib.ginlemons.repository.RecipeRepository;
import it.unimib.ginlemons.utils.ResponseCallback;
import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.RicetteList;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RicetteDiscoverFragment extends Fragment implements ResponseCallback{

    private static final String TAG = "Discover_Recipes";
    public static final String ITEM_ALCOOL_PRESSED_KEY = "ItemAlcoolPressedKey";
    public static final String ITEM_NAME_PRESSED_KEY = "ItemNamePressedKey";
    public static final String ITEM_LEVEL_PRESSED_KEY = "ItemLevelPressedKey";
    private ListeRecyclerViewAdapter listeRecyclerViewAdapter;
    private IRecipeRepository iRecipeRepository;

    // Lista delle ricette che finiscono nella RecyclerView
    List<Ricetta> ricettaList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setTitleToolbar();

        iRecipeRepository = new RecipeRepository(requireActivity().getApplication(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment (Layout fragment per il discover recipes)
        View view =  inflater.inflate(R.layout.fragment_ricette_discover, container, false);

        // Riferimento Recyclerview
        RecyclerView recyclerView = view.findViewById(R.id.discover_recycler_view);

        // Swipe right per l'aggiunta al preferite
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                /* Logica: se apprtiene ai preferiti bar rossa, altrimenti bar verde in modo dimnamico...
                Ricetta ricetta = ricettaList.get(position);
                int background = 0;
                if (!ricetta.isPreferito()){
                    background = getResources().getColor(R.color.green_add_light);
                }else{
                    background = getResources().getColor(R.color.red_delete_light);
                }*/

                // libreria esterna per settare colore e icona facilmente
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(getResources().getColor(R.color.green_add_light))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_star_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Ricetta ricetta = ricettaList.get(position);
                String snack;

                if(! ricettaList.get(position).isPreferito()){
                    ricettaList.get(position).addPreferito();
                    snack = " aggiunto ai preferiti";
                }else{
                    ricettaList.get(position).removePreferito();
                    snack = " rimosso dai preferiti";
                }
                listeRecyclerViewAdapter.notifyItemChanged(position);

                Snackbar.make(recyclerView, ricetta.getName() + snack, Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!ricettaList.get(position).isPreferito()){
                            ricettaList.get(position).addPreferito();
                        }else{
                            ricettaList.get(position).removePreferito();
                        }
                        listeRecyclerViewAdapter.notifyItemChanged(position);
                    }
                }).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // set transition on tap
        Fade fade = new Fade();
        View decor = getActivity().getWindow().getDecorView();
        fade.excludeTarget(decor.findViewById(R.id.activity_toolbar), true);
        getActivity().getWindow().setEnterTransition(fade);
        getActivity().getWindow().setExitTransition(fade);

        listeRecyclerViewAdapter = new ListeRecyclerViewAdapter(ricettaList, new ListeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onIntemClick(Ricetta ricetta) {
                Log.d(TAG, "onItemClickListener " + ricetta.getName() + " preferito : " + ricetta.isPreferito());

                Intent intent = new Intent(getActivity(), RicetteInfoActivity.class);
                intent.putExtra(ITEM_NAME_PRESSED_KEY, ricetta.getName());
                intent.putExtra(ITEM_ALCOOL_PRESSED_KEY, ricetta.getAlcool());
                intent.putExtra(ITEM_LEVEL_PRESSED_KEY, ricetta.getLevel());

                TextView textViewName = (TextView) view.findViewById(R.id.nomeRicetta);
                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), textViewName, ViewCompat.getTransitionName(textViewName));

                startActivity(intent, optionsCompat.toBundle());

                // old transition
                //getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(listeRecyclerViewAdapter);

        // Bordi per gli item della RecycleView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Popolo l'ArrayList
        // Di default "scarico" i cocktail alcolici
        iRecipeRepository.fetchRecipes("Alcoholic", false);

        return view;
    }


    @Override
    public void onResume() {
        setTitleToolbar();
        super.onResume();
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Inflate del Layout per la toolbar con la searchbar
        inflater.inflate(R.menu.searchbar_discover_menu, menu);

        // Recupero l'oggetto searchview dal menu
        MenuItem item = menu.findItem(R.id.action_search_discover);
        //SearchView searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint(getString(R.string.search_discover_title));

        // Cattura l'evento generato dalla scrittura del testo nella searchbar e dal submit della ricerca
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listeRecyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    // Selezione la lista da inserire nella RecyclerView a seconda dell'elemento del menu selezionato
    @SuppressLint("RestrictedApi")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search_discover:
                return true;
            case R.id.ordine_alfabetico_crescente:
                Collections.sort(ricettaList, Ricetta.OrdinaRicetteAlfabeticoAZ);
                listeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alfabetico_decrescente:
                Collections.sort(ricettaList, Ricetta.OrdinaRicetteAlfabeticoZA);
                listeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alcool_crescente:
                Collections.sort(ricettaList, Ricetta.OrdinaRicetteAlcoolCrescente);
                listeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alcool_decrescente:
                Collections.sort(ricettaList, Ricetta.OrdinaRicetteAlcoolDecrescente);
                listeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.change_list: {
                if (item.getTitle().equals(getString(R.string.list_alcolici)))
                {
                    item.setTitle(getString(R.string.list_analcolici));
                    iRecipeRepository.fetchRecipes("Alcoholic", true);
                } else {
                    item.setTitle(getString(R.string.list_alcolici));
                    iRecipeRepository.fetchRecipes("Non_Alcoholic", true);
                }

                listeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTitleToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_toolbar);
        toolbar.setTitle(R.string.discover_toolbar_title);
    }

    // Metodi che gestiscono i dati ricevuti dalle chiamate all'API
    // Aggiunge una ricetta dettagliata alla lista della RecyclerView
    @Override
    public void onResponse(Ricetta ricetta) {

        if(ricetta != null)
        {
            int index = 0;

            for (int i = 0; i < ricettaList.size(); i++)
                if(ricettaList.get(i).getId().equals(ricetta.getId()))
                {
                    index = i;
                    break;
                }

            ricettaList.add(index, ricetta);
        }
        // Metodo che avvisa la RecyclerView che i suoi dati sono stati aggiornati
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listeRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    // Ricevo gli id dei cocktails Alcolici o Analcolici
    // Per ognuno effettuo la richiesta all'API dei dati dettagliati del cocktail
    @Override
    public void onResponse(List<Ricetta> ricette, boolean clear) {
        if(ricette != null) {

            if(clear)
                ricettaList.clear();

            ricettaList.addAll(ricette);
            // Metodo che avvisa la RecyclerView che i suoi dati sono stati aggiornati
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    listeRecyclerViewAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    // In caso di fallimento della chiamata avviso l'utente con un messaggio
    // nella snackbar
    @Override
    public void onFailure(String errorString) {
        Snackbar msg = Snackbar.make(requireActivity().findViewById(android.R.id.content), errorString, Snackbar.LENGTH_LONG);

        // Appare anche un pulsante che permette di riprovare la chiamata
        msg.setAction("Riprova", new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                MenuItem item = getActivity().findViewById(R.id.change_list);

                if(item.getTitle().equals(R.string.list_alcolici))
                    iRecipeRepository.fetchRecipes("Non_Alcoholic", true);
                else
                    iRecipeRepository.fetchRecipes("Alcoholic", true);
            }
        });

        msg.show();
    }
}