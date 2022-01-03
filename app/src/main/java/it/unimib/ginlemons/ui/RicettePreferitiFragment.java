package it.unimib.ginlemons.ui;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.ListePreferitiAdapter;
import it.unimib.ginlemons.utils.Ricetta;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RicettePreferitiFragment extends Fragment {

    private static final String TAG = "Discover_Recipes";
    private ListePreferitiAdapter listePreferitiAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;

    // Dati per test della RecycleView
    ArrayList<Ricetta> ricettePreferitiList;

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

        firebaseAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        reference = fDB.getReference("favorites").child(firebaseAuth.getCurrentUser().getUid());

        RecyclerView recyclerView = view.findViewById(R.id.preferiti_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ricettePreferitiList = new ArrayList<>();
        listePreferitiAdapter = new ListePreferitiAdapter(ricettePreferitiList, new ListePreferitiAdapter.OnItemClickListener() {
            @Override
            public void onIntemClick(Ricetta ricetta) {
                Log.d(TAG, "onItemClickListener " + ricetta.getName() + " preferito : " + ricetta.isPreferito());
            }
        });

        recyclerView.setAdapter(listePreferitiAdapter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ricettePreferitiList.clear(); // altrimenti fa i doppioni
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Ricetta ricetta = dataSnapshot.getValue(Ricetta.class);
                    ricettePreferitiList.add(ricetta);
                }
                listePreferitiAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                reference.child(ricetta.getName())
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Preferito", "Rimosso dai preferiti nel real time DB");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Preferito", "Erorre nel real time DB");
                    }
                });

                listePreferitiAdapter.notifyItemRemoved(position);

                Snackbar.make(recyclerView, ricetta.getName() + " rimosso dai preferiti", Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ricettePreferitiList.add(position, ricetta);

                        reference.child(ricetta.getName())
                                .setValue(ricetta)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d("Preferito", "Aggiunto ai preferiti nel real time DB");
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Preferito", "Erorre nel real time DB");
                            }
                        });

                        listePreferitiAdapter.notifyItemInserted(position);
                    }
                }).show();
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);


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
                listePreferitiAdapter.getFilter().filter(newText);
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
                listePreferitiAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alfabetico_decrescente:
                Collections.sort(ricettePreferitiList, Ricetta.OrdinaRicetteAlfabeticoZA);
                listePreferitiAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alcool_crescente:
                Collections.sort(ricettePreferitiList, Ricetta.OrdinaRicetteAlcoolCrescente);
                listePreferitiAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alcool_decrescente:
                Collections.sort(ricettePreferitiList, Ricetta.OrdinaRicetteAlcoolDecrescente);
                listePreferitiAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}