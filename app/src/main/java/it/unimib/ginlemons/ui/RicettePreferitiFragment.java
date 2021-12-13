package it.unimib.ginlemons.ui;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.ListeRecyclerViewAdapter;
import it.unimib.ginlemons.utils.Ricetta;

public class RicettePreferitiFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment (Layout fragment ricette preferite)
        View view = inflater.inflate(R.layout.fragment_ricette_preferiti, container, false);

        // Set Toolbar
        setTitleToolbar();

        return view;
    }

    @Override
    public void onResume() {
        setTitleToolbar();
        super.onResume();
    }

    public void setTitleToolbar() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_toolbar);
        toolbar.setTitle(R.string.preferiti_toolbar_title);
    }

}