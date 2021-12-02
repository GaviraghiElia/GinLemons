package it.unimib.ginlemons.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.ListeRecyclerViewAdapter;

public class RicetteDiscoverFragment extends Fragment {

    private static final String TAG = "Discover_Recipes";
    private ListeRecyclerViewAdapter listeRecyclerViewAdapter;

    // Dati per test della RecycleView
    private String [] names = {"Leporati", "Zandron", "Dennunzio", "Caravenna", "Stella", "Schettini",
            "Leporati", "Zandron", "Dennunzio", "Caravenna", "Stella", "Schettini",
            "Leporati", "Zandron", "Dennunzio", "Caravenna", "Stella", "Schettini",
            "Leporati", "Zandron", "Dennunzio", "Caravenna", "Stella", "Schettini",};
    //


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
        listeRecyclerViewAdapter = new ListeRecyclerViewAdapter(names, new ListeRecyclerViewAdapter.OnItemClickListener() {
            // Listener per il click su un elemento della RecyclerView
            @Override
            public void onIntemClick(String s) {
                Log.d(TAG, "onItemClickListener " + s);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(listeRecyclerViewAdapter);

        // Bordi per gli item della RecycleView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

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

        // Cattura l'evento generato dalla scrittura del testo nella searchbar e dal submit della ricerca
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void filter (String text) {
        String[] filteredList = new String[names.length];
        int position = 0;

        for (int i = 0; i < names.length; i++)
            if (names[i].toLowerCase().contains(text.toLowerCase())) {
                filteredList[position] = names[i];
                position++;
            }

        // Se è vuota
        /*
        if(filteredList.isempty()){
             // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
        */
            // at last we are passing that filtered
            // list to our adapter class.
        listeRecyclerViewAdapter.filterList(filteredList);
        //}
    }
}