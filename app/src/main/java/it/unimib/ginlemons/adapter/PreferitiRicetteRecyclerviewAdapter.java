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
import it.unimib.ginlemons.ui.ricette.RicettaHelper;
import it.unimib.ginlemons.utils.Ricetta;

public class PreferitiRicetteRecyclerviewAdapter extends RecyclerView.Adapter<PreferitiRicetteRecyclerviewAdapter.ListeViewHolder> implements Filterable {

    private ArrayList<RicettaHelper> ricettePreferitiList;
    private ArrayList<RicettaHelper> getRicettaListFiltered;

    // Interfaccia per definire la reazione al click su un elemento della RecyclerView
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener{
        void onIntemClick(RicettaHelper ricetta);
    }


    // costruttore
    public PreferitiRicetteRecyclerviewAdapter(ArrayList<RicettaHelper> ricettePreferitiList, OnItemClickListener onItemClickListener){
        this.ricettePreferitiList = ricettePreferitiList;
        this.getRicettaListFiltered = ricettePreferitiList;
        this.onItemClickListener = onItemClickListener;
    }

    // Istanzio il ViewHolder
    @NonNull
    @Override
    public ListeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ricetta_item_recyclerview, parent, false);
        return new ListeViewHolder(view);
    }

    // Bind tra item e dati
    @Override
    public void onBindViewHolder(@NonNull ListeViewHolder holder, int position) {
        RicettaHelper ricetta = ricettePreferitiList.get(position);
        holder.bind(ricetta.getName());
    }

    @Override
    public int getItemCount() {
        return ricettePreferitiList.size();
    }

    public List<RicettaHelper> getList(){
        return ricettePreferitiList;
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
                    // NON ?? case sensitive
                    String searchChr = constraint.toString().toLowerCase();
                    List<RicettaHelper> resultData = new ArrayList<>();

                    for(RicettaHelper r: getRicettaListFiltered){
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
                ricettePreferitiList = (ArrayList<RicettaHelper>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    class ListeViewHolder extends RecyclerView.ViewHolder {

        TextView name;

        public ListeViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nomeRicetta);
        }

        // set item RecyclerView
        public void bind(String n){
            name.setText(n);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onIntemClick(ricettePreferitiList.get(getAdapterPosition()));
                }
            });

        }
    }

}
