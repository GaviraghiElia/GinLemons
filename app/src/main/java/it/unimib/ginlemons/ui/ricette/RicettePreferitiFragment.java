package it.unimib.ginlemons.ui.ricette;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;
import static it.unimib.ginlemons.utils.Constants.*;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
import it.unimib.ginlemons.adapter.PreferitiRicetteRecyclerviewAdapter;
import it.unimib.ginlemons.databinding.FragmentRicettePreferitiBinding;
import it.unimib.ginlemons.repository.preferiti.IFavoritesRepository;
import it.unimib.ginlemons.utils.Ricetta;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RicettePreferitiFragment extends Fragment {

    private static final String TAG = "Discover_Recipes";
    private PreferitiRicetteRecyclerviewAdapter preferitiRicetteRecyclerviewAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;
    private FragmentRicettePreferitiBinding mBinding;
    private RicetteViewModel rViewModel;

    // Dati per test della RecycleView
    ArrayList<Ricetta> ricettePreferitiList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        rViewModel = new ViewModelProvider(requireActivity()).get(RicetteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment (Layout fragment ricette preferite)
        mBinding = FragmentRicettePreferitiBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        // Set Toolbar
        setTitleToolbar();

        firebaseAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        reference = fDB.getReference("favorites").child(firebaseAuth.getCurrentUser().getUid());

        mBinding.preferitiRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        ricettePreferitiList = new ArrayList<>();

        preferitiRicetteRecyclerviewAdapter = new PreferitiRicetteRecyclerviewAdapter(ricettePreferitiList, new PreferitiRicetteRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onIntemClick(Ricetta ricetta) {
                Log.d(TAG, "onItemClickListener " + ricetta.getName());
                navigateToRicettaInfo(ricetta);
            }
        });

        mBinding.preferitiRecyclerView.setAdapter(preferitiRicetteRecyclerviewAdapter);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ricettePreferitiList.clear(); // altrimenti fa i doppioni
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RicettaHelper ricetta = dataSnapshot.getValue(RicettaHelper.class);
                    ricettePreferitiList.add(new Ricetta(ricetta));
                }
                preferitiRicetteRecyclerviewAdapter.notifyDataSetChanged();
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
                Ricetta ricetta = preferitiRicetteRecyclerviewAdapter.getList().get(position);

                if(ricettePreferitiList.size() != preferitiRicetteRecyclerviewAdapter.getList().size())
                    preferitiRicetteRecyclerviewAdapter.getList().remove(position);

                // remove on FireBase
                ricettePreferitiList.remove(position);
                RicettaHelper ricettaHelper = new RicettaHelper(ricetta.getId(), ricetta.getName(), ricetta.getType());
                removePreferitoRealTimeDB(ricettaHelper);

                preferitiRicetteRecyclerviewAdapter.notifyItemRemoved(position);

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mBinding.preferitiRecyclerView);

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
                preferitiRicetteRecyclerviewAdapter.getFilter().filter(newText);
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
                preferitiRicetteRecyclerviewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alfabetico_decrescente:
                Collections.sort(ricettePreferitiList, Ricetta.OrdinaRicetteAlfabeticoZA);
                preferitiRicetteRecyclerviewAdapter.notifyDataSetChanged();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void navigateToRicettaInfo(Ricetta ricetta){
        Intent intent = new Intent(getActivity(), RicetteInfoActivity.class);
        intent.putExtra(FRAGMENTFORTRANSITION, "RicettePreferiti");
        intent.putExtra(ITEM_ID_PRESSED_KEY, ricetta.getId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void removePreferitoRealTimeDB(RicettaHelper ricettaHelper){
        rViewModel.removeFavorites(ricettaHelper).observe(getViewLifecycleOwner(), removeFavoritesResponse ->{
            if(removeFavoritesResponse != null){
                if(removeFavoritesResponse.isSuccess()){
                    Log.d("Preferito", "Rimosso " + ricettaHelper.getName() + " dai preferiti nel real time DB");
                    makeMessageSnack(ricettaHelper, getString(R.string.favourite_removed));
                }else{
                    makeMessage(removeFavoritesResponse.getMessage());
                }
            }
        });
    }

    private void makeMessageSnack(RicettaHelper ricettaHelper, String snack) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content),
                ricettaHelper.getName() +" " + snack, Snackbar.LENGTH_SHORT).show();
        rViewModel.clear();
    }

    private void makeMessage(String message) {
        Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
        rViewModel.clear();
    }
}