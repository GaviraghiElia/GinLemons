package it.unimib.ginlemons.ui;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.ListeRecyclerViewAdapter;
import it.unimib.ginlemons.utils.Ricetta;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RicettePreferitiFragment extends Fragment {

    private static final String TAG = "Discover_Recipes";
    private ListeRecyclerViewAdapter listeRecyclerViewAdapter;

    // Dati per test della RecycleView
    List<Ricetta> ricettePreferitiList = new ArrayList<>();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment (Layout fragment ricette preferite)
        View view = inflater.inflate(R.layout.fragment_ricette_preferiti, container, false);

        // Set Toolbar
        setTitleToolbar();

        RecyclerView recyclerView = view.findViewById(R.id.preferiti_recycler_view);

        // Swipe right do Add preferite
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            // backgound swipe


            // controllo lo swipe LEFT
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(getResources().getColor(R.color.red_delete_light))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();
                Ricetta ricetta = ricettePreferitiList.get(position);

                // andrà inserita la logica: "ricetta.removePreferito() quando avremo un DB/qualsiasi
                // che sia aggiornabile e condiviso
                ricettePreferitiList.remove(position);
                listeRecyclerViewAdapter.notifyItemRemoved(position);

                Snackbar.make(recyclerView, ricetta.getName() + " rimosso dai preferiti", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ricettePreferitiList.add(position, ricetta);
                        listeRecyclerViewAdapter.notifyItemInserted(position);
                    }
                }).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


        listeRecyclerViewAdapter = new ListeRecyclerViewAdapter(ricettePreferitiList, new ListeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onIntemClick(Ricetta ricetta) {
                Log.d(TAG, "onItemClickListener " + ricetta.getName() + " preferito : " + ricetta.isPreferito());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(listeRecyclerViewAdapter);

        // Bordi per gli item della RecycleView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        for(int i = 0; i < names.length; i++){
            names[i].addPreferito();
            ricettePreferitiList.add(names[i]);
        }

        return view;
    }


    @Override
    public void onResume() {
        setTitleToolbar();
        super.onResume();
    }

    public void setTitleToolbar() {
        if (getActivity() != null) {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_toolbar);
            if (toolbar != null) {
                toolbar.setTitle(R.string.preferiti_toolbar_title);
            }
        }
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
                //filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_search_discover:
                return true;
            case R.id.ordine_alfabetico_crescente:
                Collections.sort(ricettePreferitiList, Ricetta.OrdinaRicetteAlfabeticoAZ);
                listeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alfabetico_decrescente:
                Collections.sort(ricettePreferitiList, Ricetta.OrdinaRicetteAlfabeticoZA);
                listeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alcool_crescente:
                Collections.sort(ricettePreferitiList, Ricetta.OrdinaRicetteAlcoolCrescente);
                listeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alcool_decrescente:
                Collections.sort(ricettePreferitiList, Ricetta.OrdinaRicetteAlcoolDecrescente);
                listeRecyclerViewAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}