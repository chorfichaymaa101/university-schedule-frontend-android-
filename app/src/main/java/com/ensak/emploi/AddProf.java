package com.ensak.emploi;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ensak.emploi.model.Prof;
import com.ensak.emploi.model.ProfProgram;
import com.ensak.emploi.retrofit.ProfApi;
import com.ensak.emploi.retrofit.RetrofitService;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProf extends AppCompatActivity {

    private final List<CheckBox> checkBoxes = new ArrayList<>();
    private final String[] filieres = {"p1", "p2", "p3", "p4", "p5"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_prof);

        // Initialize checkboxes dynamically
        setupCheckboxes();

        // Initialize components for adding a professor
        initializeComponents();
    }

    private void setupCheckboxes() {
        // Find the container
        LinearLayout checkboxContainer = findViewById(R.id.checkbox_container_prof);

        // Dynamically add checkboxes, two per row
        LinearLayout currentRow = null;
        for (int i = 0; i < filieres.length; i++) {
            if (i % 2 == 0) {
                // Create a new horizontal row
                currentRow = new LinearLayout(this);
                currentRow.setOrientation(LinearLayout.HORIZONTAL);
                currentRow.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                checkboxContainer.addView(currentRow);
            }

            // Create a new checkbox
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(filieres[i]);
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1 // Weight for equal distribution
            ));
            checkBoxes.add(checkBox);

            // Add the checkbox to the current row
            if (currentRow != null) {
                currentRow.addView(checkBox);
            }
        }
    }

    private void initializeComponents() {
        TextInputLayout nameInput = findViewById(R.id.input_name_prof);
        TextInputLayout emailInput = findViewById(R.id.input_email_prof);
        Button addButton = findViewById(R.id.button_ajouter_prof);

        RetrofitService retrofitService = new RetrofitService();
        ProfApi profApi = retrofitService.getRetrofit().create(ProfApi.class);

        addButton.setOnClickListener(view -> {
            String name = nameInput.getEditText().getText().toString();
            String email = emailInput.getEditText().getText().toString();

            if (name.isEmpty() || email.isEmpty()) {
                Toast.makeText(AddProf.this, "Name and email are required!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a professor object
            Prof prof = new Prof();
            prof.setName(name);
            prof.setEmail(email);

            // Collect selected programs
            List<String> profPrograms = new ArrayList<>();
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isChecked()) {
                    profPrograms.add(filieres[i]);
                }
            }
            prof.setPrograms(profPrograms);

            if (profPrograms.isEmpty()) {
                Toast.makeText(AddProf.this, "Please select at least one program!", Toast.LENGTH_SHORT).show();
                return;
            }

            profApi.save(prof)
                    .enqueue(new Callback<Prof>() {
                        @Override
                        public void onResponse(Call<Prof> call, Response<Prof> response) {
                            Toast.makeText(AddProf.this, "Professor saved successfully!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Prof> call, Throwable t) {
                            Toast.makeText(AddProf.this, "Save failed!", Toast.LENGTH_SHORT).show();
                            Logger.getLogger(AddProf.class.getName()).log(Level.SEVERE, "Error occurred", t);
                        }
                    });
        });
    }
}