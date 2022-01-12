package it.unimib.ginlemons.ui.ricette;

import static it.unimib.ginlemons.utils.Constants.FIREBASE_DATABASE_URL;
import static it.unimib.ginlemons.utils.Constants.*;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.List;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.adapter.DiscoverRicetteRecyclerViewAdapter;
import it.unimib.ginlemons.databinding.FragmentRicetteDiscoverBinding;
import it.unimib.ginlemons.utils.Ricetta;
import it.unimib.ginlemons.utils.RicetteList;
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class RicetteDiscoverFragment extends Fragment {

    private static final String TAG = "Discover_Recipes";
    private DiscoverRicetteRecyclerViewAdapter discoverRicetteRecyclerViewAdapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase fDB;
    private DatabaseReference reference;
    private FragmentRicetteDiscoverBinding mBinding;
    private NavController navController;
    private RicetteViewModel rViewModel;

    List<Ricetta> ricettaList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setTitleToolbar();

        rViewModel = new ViewModelProvider(requireActivity()).get(RicetteViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment (Layout fragment per il discover recipes)
        mBinding = FragmentRicetteDiscoverBinding.inflate(inflater, container, false);
        View view = mBinding.getRoot();

        firebaseAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance(FIREBASE_DATABASE_URL);
        reference = fDB.getReference("favorites")
                .child(firebaseAuth.getCurrentUser().getUid());

        navController = NavHostFragment.findNavController(this);

        // Swipe right per l'aggiunta al preferite
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY,
                                    int actionState, boolean isCurrentlyActive) {

                // libreria esterna per settare colore e icona facilmente
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addSwipeRightBackgroundColor(getResources().getColor(R.color.yellow_preferits))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_star_24)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }


            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Ricetta ricetta = ricettaList.get(position);

                // check if esiste già nel DB == è già un preferito
                reference.child(ricetta.getId())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.exists()){
                            // se non esiste, allora lo aggiungiamo ai preferiti
                            reference.child(ricetta.getId())
                                    .setValue(ricetta)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Preferito", "Aggiunto " + ricetta.getName() + " ai preferiti nel real time DB");
                                            snackbarMake(mBinding.discoverRecyclerView, ricetta, "aggiunta ai preferiti");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Preferito", "Erorre nel real time DB");
                                }
                            });

                        }else{
                            // se già esiste, allora lo rimuoviamo dai preferiti
                            reference.child(ricetta.getId())
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Log.d("Preferito", "Rimosso " + ricetta.getName() + " dai preferiti nel real time DB");
                                            snackbarMake(mBinding.discoverRecyclerView, ricetta, "rimossa dai preferiti");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Preferito", "Erorre nel real time DB");
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d("Preferito", "Errore " +databaseError.getMessage());
                    }
                });
                discoverRicetteRecyclerViewAdapter.notifyItemChanged(position);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(mBinding.discoverRecyclerView);


        discoverRicetteRecyclerViewAdapter = new DiscoverRicetteRecyclerViewAdapter(ricettaList, new DiscoverRicetteRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onIntemClick(Ricetta ricetta) {
                Log.d(TAG, "onItemClickListener " + ricetta.getName());
                // non posso usare il Navigation Component!
                // Sto navigando da un fragment verso un'altra activity: non c'è modo di recuperare
                // i dati dall'altro parte, almeno stando ai tutorial disponibili....
                // stack overflow consiglia ciò
                navigateToRicettaInfo(ricetta);
            }
        });

        mBinding.discoverRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mBinding.discoverRecyclerView.setAdapter(discoverRicetteRecyclerViewAdapter);

        // Observer per il LiveData dei cocktails alcolici
        final Observer<RicetteList> observer_alcolici = new Observer<RicetteList>() {
            @Override
            public void onChanged(RicetteList ricette) {
                if (ricette != null)
                {
                    String errorString = ricette.getError();
                    // In caso di fallimento della chiamata avviso l'utente con un messaggio nella snackbar
                    if (errorString !=null)
                    {
                        ricette.setError(null);

                        Snackbar msg = Snackbar.make(requireActivity().findViewById(android.R.id.content), errorString, Snackbar.LENGTH_LONG);

                        // Appare anche un pulsante che permette di riprovare la chiamata
                        msg.setAction("Riprova", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MenuItem item = getActivity().findViewById(R.id.change_list);

                                rViewModel.getAlcolici();
                            }
                        });

                        msg.show();
                    }
                    else
                    {
                        ricettaList.clear();
                        rViewModel.setType("Alcoholic");;
                        ricettaList.addAll(ricette.getRepices());
                        Collections.sort(ricettaList, Ricetta.OrdinaRicetteAlfabeticoAZ);
                        discoverRicetteRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        };

        // Observer per il LiveData dei cocktails analcolici
        final Observer<RicetteList> observer_analcolici = new Observer<RicetteList>() {
            @Override
            public void onChanged(RicetteList ricette) {
                if(ricette != null)
                {
                    String errorString = ricette.getError();
                    // In caso di fallimento della chiamata avviso l'utente con un messaggio nella snackbar
                    if (errorString != null) {
                        ricette.setError(null);

                        Snackbar msg = Snackbar.make(requireActivity().findViewById(android.R.id.content), errorString, Snackbar.LENGTH_LONG);

                        // Appare anche un pulsante che permette di riprovare la chiamata
                        msg.setAction("Riprova", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                MenuItem item = getActivity().findViewById(R.id.change_list);

                                rViewModel.getAnalcolici();
                            }
                        });

                        msg.show();
                    } else {
                        ricettaList.clear();
                        rViewModel.setType("Non_Alcoholic");;
                        ricettaList.addAll(ricette.getRepices());
                        Collections.sort(ricettaList, Ricetta.OrdinaRicetteAlfabeticoAZ);
                        discoverRicetteRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        };

        rViewModel.getAlcolici().observe(getViewLifecycleOwner(), observer_alcolici);
        rViewModel.getAnalcolici().observe(getViewLifecycleOwner(), observer_analcolici);

        rViewModel.getAlcolici();


        return view;
    }


    @Override
    public void onResume() {
        if(ricettaList.size() !=  0)
        {
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_toolbar);
            MenuItem item = toolbar.getMenu().findItem(R.id.change_list);

            if(ricettaList.get(1).getType().equalsIgnoreCase( "Alcoholic"))
            {
                item.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.non_alcoholic));
                item.setTitle(getString(R.string.list_analcolici));
            } else
            {
                item.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.alcoholic));
                item.setTitle(getString(R.string.list_alcolici));
            }
        }
        //        discoverRicetteRecyclerViewAdapter.notifyDataSetChanged();

        setTitleToolbar();
        super.onResume();
    }


    public void snackbarMake(View view, Ricetta ricetta, String snack) {
        Snackbar.make(view, ricetta.getName() +" " + snack, Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (snack.equals("aggiunta ai preferiti")) {
                    // fa undo, l'ho inserito prima e ora quindi voglio rimuoverlo
                    Log.d("Preferito", "Snack - rimozione " + ricetta.getName() + " dai preferiti");
                    removePreferitoRealTimeDB(ricetta);
                } else {
                    // fa undo, l'ho rimosso prima e quindi ora voglio aggiungerlo
                    Log.d("Preferito", "Snack - riaggiunta " + ricetta.getName() + " ai preferiti");
                    addPreferitoRealTimeDB(ricetta);
                }
            }
        }).show();
    }

    public void addPreferitoRealTimeDB(Ricetta ricetta){
        reference.child(ricetta.getId())
                .setValue(ricetta)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Preferito", "Snack - OnSuccess Aggiunto " + ricetta.getName() + " ai preferiti nel real time DB");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Preferito", "Snack - Erorre nel real time DB");
            }
        });
    }

    public void removePreferitoRealTimeDB(Ricetta ricetta){
        reference.child(ricetta.getId())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Preferito", "Rimosso " + ricetta.getName()+ " dai preferiti nel real time DB");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Preferito", "Erorre nel real time DB");
            }
        });
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
                discoverRicetteRecyclerViewAdapter.getFilter().filter(newText);
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
                discoverRicetteRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.ordine_alfabetico_decrescente:
                Collections.sort(ricettaList, Ricetta.OrdinaRicetteAlfabeticoZA);
                discoverRicetteRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            case R.id.change_list:
            {
                if(ricettaList.get(1).getType().equalsIgnoreCase( "Non_Alcoholic"))
                {

                    ricettaList.clear();
                    ricettaList.addAll(rViewModel.getAlcolici().getValue().getRepices());
                    discoverRicetteRecyclerViewAdapter.notifyDataSetChanged();

                    item.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.non_alcoholic));
                    item.setTitle(getString(R.string.list_analcolici));
                } else {
                    ricettaList.clear();
                    ricettaList.addAll(rViewModel.getAnalcolici().getValue().getRepices());
                    discoverRicetteRecyclerViewAdapter.notifyDataSetChanged();

                    item.setIcon(ContextCompat.getDrawable(requireContext(), R.drawable.alcoholic));
                    item.setTitle(getString(R.string.list_alcolici));
                }

                Collections.sort(ricettaList, Ricetta.OrdinaRicetteAlfabeticoAZ);
                discoverRicetteRecyclerViewAdapter.notifyDataSetChanged();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setTitleToolbar() {
        if(getActivity() != null){
            Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.activity_toolbar);
            if(toolbar != null){
                toolbar.setTitle(R.string.discover_toolbar_title);
            }
        }
    }

    public void navigateToRicettaInfo(Ricetta ricetta){
        Intent intent = new Intent(getActivity(), RicetteInfoActivity.class);
        intent.putExtra(FRAGMENTFORTRANSITION, "RicetteDiscover");
        intent.putExtra(ITEM_ID_PRESSED_KEY, ricetta.getId());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}