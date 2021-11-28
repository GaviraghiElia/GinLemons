package it.unimib.ginlemons.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unimib.ginlemons.R;

public class ListeRecyclerViewAdapter extends RecyclerView.Adapter<ListeRecyclerViewAdapter.ListeViewHolder> {


    public interface OnItemClickListener{
        void onIntemClick(String s);
    }

    private String [] list;
    private OnItemClickListener onItemClickListener;


    public ListeRecyclerViewAdapter(String[] list, OnItemClickListener onItemClickListener){
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }

    //istanziamo il ViewHolder
    @NonNull
    @Override
    public ListeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter, parent, false);
        return new ListeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListeViewHolder holder, int position) {
        holder.bind(list[position]);
        //holder.textView.setText(list[position]);
    }

    @Override
    public int getItemCount() {
        return list.length;
    }


    class ListeViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public ListeViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewRecyclerView);
        }

        public void bind(String s){
            textView.setText(s);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onIntemClick(s);
                }
            });
        }
    }
}
