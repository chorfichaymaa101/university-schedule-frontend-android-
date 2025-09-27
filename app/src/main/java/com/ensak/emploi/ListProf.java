package com.ensak.emploi;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.ensak.emploi.adapter.ProfAdapter;
import com.ensak.emploi.model.Prof;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.search.SearchBar;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import android.widget.SearchView;
import android.widget.Toolbar;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ensak.emploi.databinding.ActivityListProfBinding;

import java.util.ArrayList;
import java.util.List;


public class ListProf extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfAdapter adapter;
    private List<Prof> profList;
    SearchView search_bar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_prof);

        search_bar = findViewById(R.id.search_view);


        recyclerView = findViewById(R.id.recycler_view_prof);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data - Replace this with data fetched from the database
        profList = new ArrayList<>();
        profList.add(new Prof("Dr. Smith"));
        profList.add(new Prof("Dr. Johnson"));
        profList.add(new Prof("Dr. Brown"));

        adapter = new ProfAdapter(this, profList, new ProfAdapter.OnItemClickListener() {
            @Override
            public void onModifyClick(int position) {
                Toast.makeText(ListProf.this, "Modify: " + profList.get(position).getName(), Toast.LENGTH_SHORT).show();
                // Implement modify logic
            }

            @Override
            public void onDeleteClick(int position) {
                new AlertDialog.Builder(ListProf.this)
                        .setMessage(getString(R.string.confirm_delete) + " " + profList.get(position).getName() + "?")
                        .setPositiveButton(getString(R.string.delete), (dialog, which) -> {
                            // Perform delete logic
                            profList.remove(position);
                            adapter.notifyItemRemoved(position);
                            Toast.makeText(ListProf.this, "Deleted: " + profList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton(getString(R.string.cancel), (dialog, which) -> {
                            // Cancel deletion
                            dialog.dismiss();
                            Toast.makeText(ListProf.this, "Delete canceled", Toast.LENGTH_SHORT).show();
                        })
                        .show();
            }
        });

        recyclerView.setAdapter(adapter);


        search_bar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Perform search when the user submits the query
                adapter.getFilter().filter(query);
                return false; // returning false allows the query to be submitted
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Perform search as the user types
                adapter.getFilter().filter(newText);
                return false; // returning false allows the query to be changed
            }
        });

        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(view -> {
            // Create an Intent to navigate to the target activity
            Intent intent = new Intent(ListProf.this, AddProf.class);
            startActivity(intent);
        });
    }


}
