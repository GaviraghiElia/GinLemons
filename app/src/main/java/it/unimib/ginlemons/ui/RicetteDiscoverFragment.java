package it.unimib.ginlemons.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.ListeRecyclerViewAdapter;
import it.unimib.ginlemons.utils.Ricetta;

public class RicetteDiscoverFragment extends Fragment {

    private static final String TAG = "Discover_Recipes";
    private ListeRecyclerViewAdapter listeRecyclerViewAdapter;

    // Dati per test della RecycleView
    List<Ricetta> ricettaList = new ArrayList<>();
    private String [] names = {"Campari Spritz", "Aperol Spritz", "Hugo", "Mojito", "Mai Tai",
            "Martini Spritz", "Martini", "Black Russian", "White Russian", "Vodka Lemon",
            "Gin Tonic", "Gin Lemon", "Vodka Tonic", "Negroni", "Daiquiri", "Cosmopolitan",
            "Leporati", "Zandron", "Dennunzio", "Caravenna", "Stella", "Schettini"};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment (Layout fragment per il discover recipes)
        View view =  inflater.inflate(R.layout.fragment_ricette_discover, container, false);

        // Set Toolbar
        setTitleToolbar();

        // Recyclerview
        RecyclerView recyclerView = view.findViewById(R.id.discover_recycler_view);
        listeRecyclerViewAdapter = new ListeRecyclerViewAdapter(ricettaList, new ListeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onIntemClick(Ricetta ricetta) {
                Log.d(TAG, "onItemClickListener " + ricetta.getName());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(listeRecyclerViewAdapter);

        // Bordi per gli item della RecycleView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        for(String s: names){
            Ricetta ricetta = new Ricetta(s, 15, "2");
            ricettaList.add(ricetta);
        }

        return view;
    }


    @Override
    public void onResume() {
        setTitleToolbar();
        super.onResume();
    }


    public void setTitleToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_toolbar);
        toolbar.setTitle(R.string.discover_toolbar_title);
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

}