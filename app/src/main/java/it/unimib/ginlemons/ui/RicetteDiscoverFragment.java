package it.unimib.ginlemons.ui;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.ginlemons.R;

public class RicetteDiscoverFragment extends Fragment {

    private ListView listView;
    private SearchView searchView;
    private SearchView.OnQueryTextListener queryTextListener;
    private ArrayList<String> stringArrayList = new ArrayList<>();
    private String [] name = {"Micucci", "Leporati", "Ginelli", "Zandron", "Stella", "Messina"};
    private ArrayAdapter<String> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // ATTENZIONE
    // Codice preso su internet e copiato un po' a caso, funziona ma non lo capisco bene
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if(searchItem != null){
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null){
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("OnQueryTextChange", newText);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    // ATTENZIONE
    // Codice preso su internet e copiato un po' a caso, funziona ma non lo capisco bene
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ricette_discover, container, false);
    }


}