package it.unimib.ginlemons.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unimib.ginlemons.R;
import it.unimib.ginlemons.utils.Ricetta;

public class PreferitiRicetteRecyclerviewAdapter extends RecyclerView.Adapter<PreferitiRicetteRecyclerviewAdapter.ListeViewHolder> implements Filterable {

    private ArrayList<Ricetta> ricettePreferitiList;
    private ArrayList<Ricetta> getRicettaListFiltered;

    // Interfaccia per definire la reazione al click su un elemento della RecyclerView
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onIntemClick(Ricetta ricetta);
    }

    // costruttore
    public PreferitiRicetteRecyclerviewAdapter(ArrayList<Ricetta> ricettePreferitiList, OnItemClickListener onItemClickListener){
        this.ricettePreferitiList = ricettePreferitiList;
        this.getRicettaListFiltered = ricettePreferitiList;
        this.onItemClickListener = onItemClickListener;
    }

    // Istanzio il ViewHolder
    @NonNull
    @Override
    public ListeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter, parent, false);
        return new ListeViewHolder(view);
    }

    // Bind tra item e dati
    @Override
    public void onBindViewHolder(@NonNull ListeViewHolder holder, int position) {
        Ricetta ricetta = ricettePreferitiList.get(position);
        holder.bind(ricetta.getName(), ricetta.getLevel(), ricetta.getAlcool());
    }

    @Override
    public int getItemCount() {
        return ricettePreferitiList.size();
    }


    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if(constraint == null | constraint.length() == 0){
                    filterResults.count = getRicettaListFiltered.size();
                    filterResults.values = getRicettaListFiltered;

                }else{
                    // NON è case sensitive
                    String searchChr = constraint.toString().toLowerCase();
                    List<Ricetta> resultData = new ArrayList<>();

                    for(Ricetta r: getRicettaListFiltered){
                        if(r.getName().toLowerCase().contains(searchChr)){
                            resultData.add(r);
                        }
                    }

                    filterResults.count = resultData.size();
                    filterResults.values = resultData;
                }

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                ricettePreferitiList = (ArrayList<Ricetta>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    // nuova classe
    class ListeViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView level;
        TextView alcool;

        public ListeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nomeRicetta);
            level = itemView.findViewById(R.id.costoRicetta);
            alcool = itemView.findViewById(R.id.alcoolRicetta);
        }

        // set item RecyclerView
        public void bind(String n, int l, int a){
            name.setText(n);
            alcool.setText(Integer.toString(a));

            //temporaneo, da spostare/modificare il parametro level
            if(l == 1){
                level.setText("€");
            }else if(l == 2){
                level.setText("€€");
            }else{
                level.setText("€€€");
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onIntemClick(ricettePreferitiList.get(getAdapterPosition()));
                }
            });
        }
    }

}
