package com.ensak.emploi.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ensak.emploi.R;
import com.ensak.emploi.model.Prof;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProfAdapter extends RecyclerView.Adapter<ProfAdapter.ProfViewHolder> implements Filterable {

    private final Context context;
    private final List<Prof> profList;
    private final OnItemClickListener listener;
    private List<Prof> profListFull; // List to hold all items for filtering


    public interface OnItemClickListener {
        void onModifyClick(int position);
        void onDeleteClick(int position);
    }

    public ProfAdapter(Context context, List<Prof> profList, OnItemClickListener listener) {
        this.context = context;
        this.profList = profList;
        this.listener = listener;
        this.profListFull = new ArrayList<>(profList); // Store the full list for filtering
    }

    @NonNull
    @Override
    public ProfViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.content_list_prof, parent, false);
        return new ProfViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfViewHolder holder, int position) {
        Prof prof = profList.get(position);
        holder.profName.setText(prof.getName());

        holder.modifyButton.setOnClickListener(v -> listener.onModifyClick(position));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() {
        return profList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint == null || constraint.length() == 0) {
                    results.values = new ArrayList<>(profListFull); // Return all items
                    results.count = profListFull.size();
                } else {
                    List<Prof> filtered = new ArrayList<>();
                    for (Prof prof : profListFull ) {
                        if (prof.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filtered.add(prof);
                        }
                    }
                    results.values = filtered;
                    results.count = filtered.size();
                }

                return results;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                profList.clear();

                if (results.values instanceof List<?>) {
                    @SuppressWarnings("unchecked")
                    List<?> resultList = (List<?>) results.values;

                    if (resultList.isEmpty()) {
                        // Handle the "no match" case
                        Toast.makeText(context, "No results found", Toast.LENGTH_SHORT).show();
                    } else {
                        profList.addAll((Collection<? extends Prof>) resultList); // Cast safely
                    }
                }
                notifyDataSetChanged();
            }
        };
    }

    public static class ProfViewHolder extends RecyclerView.ViewHolder {
        TextView profName;
        ImageButton modifyButton, deleteButton;

        public ProfViewHolder(@NonNull View itemView) {
            super(itemView);
            profName = itemView.findViewById(R.id.prof_name);
            modifyButton = itemView.findViewById(R.id.btn_modify);
            deleteButton = itemView.findViewById(R.id.btn_delete);
        }
    }
}
