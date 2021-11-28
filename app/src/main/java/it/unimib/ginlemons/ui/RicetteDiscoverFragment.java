package it.unimib.ginlemons.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.ListeRecyclerViewAdapter;

public class RicetteDiscoverFragment extends Fragment {

    private static final String TAG = "BOH";

    private String [] names = {"Leporati", "Zandron", "Dennunzio", "Caravenna", "Stella", "Schettini",
            "Leporati", "Zandron", "Dennunzio", "Caravenna", "Stella", "Schettini",
            "Leporati", "Zandron", "Dennunzio", "Caravenna", "Stella", "Schettini",
            "Leporati", "Zandron", "Dennunzio", "Caravenna", "Stella", "Schettini",};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ricette_discover, container, false);

        // Set Toolbar
        setTitleToolbar();

        //recyclerview
        RecyclerView recyclerView = view.findViewById(R.id.discover_recycler_view);
        ListeRecyclerViewAdapter listeRecyclerViewAdapter = new ListeRecyclerViewAdapter(names, new ListeRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onIntemClick(String s) {
                Log.d(TAG, "onItemClickListener" + s);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(listeRecyclerViewAdapter);

        // aggiungiamo dei bordi che se no fa schifo
        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
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
        toolbar.setTitle("Discover");
    }

}