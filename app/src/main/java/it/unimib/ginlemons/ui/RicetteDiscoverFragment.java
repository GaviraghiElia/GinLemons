package it.unimib.ginlemons.ui;

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

import androidx.annotation.NonNull;
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
import it.unimib.ginlemons.data.FetchData;
import it.unimib.ginlemons.utils.Ricetta;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RicetteDiscoverFragment extends Fragment {

    private static final String TAG = "Discover_Recipes";
    private ListeRecyclerViewAdapter listeRecyclerViewAdapter;
    public static final String ITEM_ALCOOL_PRESSED_KEY = "ItemAlcoolPressedKey";
    public static final String ITEM_NAME_PRESSED_KEY = "ItemNamePressedKey";
    public static final String ITEM_LEVEL_PRESSED_KEY = "ItemLevelPressedKey";


    // Dati test RecycleView
    List<Ricetta> ricettaList = new ArrayList<>();
    private Ricetta [] names = {new Ricetta("Campari Spritz", 10, 1),
            new Ricetta("Aperol Spritz", 15, 2), new Ricetta("Micucci Spritz", 18, 3),
            new Ricetta("Hugo", 8, 1), new Ricetta("Mojito", 14, 2),
            new Ricetta("Mai Tai", 12, 2), new Ricetta("Martini Spritz", 13, 3),
            new Ricetta("Martini", 20, 3), new Ricetta("Black Russian", 25, 2),
            new Ricetta("White Russian", 26, 2), new Ricetta("Vodka Lemon", 19, 1),
            new Ricetta("Gin Tonic", 17, 2), new Ricetta("Gin Lemon", 17, 2),
            new Ricetta("Negroni", 22, 2), new Ricetta("Daiquiri", 14, 3),
            new Ricetta("Cosmopolitan", 24, 3), new Ricetta("Leporati", 100, 3),
            new Ricetta("Zandron", 100, 3), new Ricetta("Dennunzio", 100, 3)
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setTitleToolbar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment (Layout fragment per il discover recipes)
        View view =  inflater.inflate(R.layout.fragment_ricette_discover, container, false);

        // TEST API
        new FetchData().start();

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

        // si popola l'ArrayList
        Collections.addAll(ricettaList, names);

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
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTitleToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_toolbar);
        toolbar.setTitle(R.string.discover_toolbar_title);
    }
}