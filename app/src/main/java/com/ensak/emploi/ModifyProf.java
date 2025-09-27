package com.ensak.emploi;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.ensak.emploi.model.Prof;
import com.ensak.emploi.retrofit.ProfApi;
import com.ensak.emploi.retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyProf extends AppCompatActivity {

    private  final String[] filieres = {"p1", "p2", "p3", "p4", "p5"};
    private LinearLayout checkboxContainer;
    private EditText nameInput;
    private EditText emailInput;
    private Button saveButton;
    private Prof currentProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modify_prof);

        checkboxContainer = findViewById(R.id.checkbox_container_prof_modifié);
        nameInput = findViewById(R.id.input_name_prof_modifié);
        emailInput = findViewById(R.id.input_email_prof_modifié);
        saveButton = findViewById(R.id.button_modifier_prof);

        // Example name to fetch (replace with actual input)
        Long professorIdToModify = 1L;

        fetchProfessorById(professorIdToModify);
        setupSaveButton();
    }

    private void fetchProfessorById(Long id) {
        RetrofitService retrofitService = new RetrofitService();
        ProfApi profApi = retrofitService.getRetrofit().create(ProfApi.class);

        profApi.getById(id).enqueue(new Callback<Prof>() {
            @Override
            public void onResponse(Call<Prof> call, Response<Prof> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentProf = response.body();
                    populateFields(currentProf);
                } else {
                    Toast.makeText(ModifyProf.this, "Professor not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Prof> call, Throwable t) {
                Toast.makeText(ModifyProf.this, "Error fetching professor", Toast.LENGTH_SHORT).show();
                Log.e("ModifyProf", "Error fetching professor", t);
            }
        });
    }

    private void populateFields(Prof prof) {
        // Prefill name and email fields
        nameInput.setText(prof.getName());
        emailInput.setText(prof.getEmail());

        // Populate checkboxes for the professor's fields
        checkboxContainer.removeAllViews();

        LinearLayout currentRow = null;
        for (int i = 0; i < filieres.length; i++) {
            if (i % 2 == 0) {
                // Create a new row
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
            checkBox.setChecked(prof.getPrograms().contains(filieres[i])); // Mark if already selected
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1 // Weight for equal distribution
            ));

            // Add the checkbox to the current row
            if (currentRow != null) {
                currentRow.addView(checkBox);
            }
        }
    }

    private void setupSaveButton() {
        RetrofitService retrofitService = new RetrofitService();
        ProfApi profApi = retrofitService.getRetrofit().create(ProfApi.class);

        saveButton.setOnClickListener(view -> {
            if (currentProf == null) {
                Toast.makeText(this, "No professor loaded to modify", Toast.LENGTH_SHORT).show();
                return;
            }

            // Update professor data from user inputs
            currentProf.setName(nameInput.getText().toString());
            currentProf.setEmail(emailInput.getText().toString());

            // Save changes to the server
            profApi.save(currentProf).enqueue(new Callback<Prof>() {
                @Override
                public void onResponse(Call<Prof> call, Response<Prof> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ModifyProf.this, "Professor updated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ModifyProf.this, "Failed to update professor", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Prof> call, Throwable t) {
                    Toast.makeText(ModifyProf.this, "Error saving professor", Toast.LENGTH_SHORT).show();
                    Log.e("ModifyProf", "Error saving professor", t);
                }
            });
        });
    }
}
