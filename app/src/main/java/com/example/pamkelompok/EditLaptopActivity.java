package com.example.pamkelompok;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditLaptopActivity extends AppCompatActivity {

    private EditText editTextNama, editTextMerk, editTextHarga, editTextTahunRilis;
    private Button submitButton;
    private FirebaseFirestore db;
    private String laptopId = null; // To track if we are editing

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_laptop);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Get references to the UI components
        editTextNama = findViewById(R.id.editTextNama);
        editTextMerk = findViewById(R.id.editTextMerk);
        editTextHarga = findViewById(R.id.editTextHarga);
        editTextTahunRilis = findViewById(R.id.editTextTahunRilis);
        submitButton = findViewById(R.id.submitButton);

        // Check if we are editing or adding a new laptop
        Intent intent = getIntent();
        if (intent.hasExtra("laptop_id")) {
            laptopId = intent.getStringExtra("laptop_id");
            loadLaptopData(intent); // Load laptop data if we are editing
            submitButton.setText("Update Laptop"); // Change button text for editing
        }

        // Set up the submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (laptopId == null) {
                    addLaptopToFirestore(); // Adding new laptop
                } else {
                    updateLaptopInFirestore(); // Editing existing laptop
                }
            }
        });
    }

    private void loadLaptopData(Intent intent) {
        // Prepopulate fields with the passed data
        editTextNama.setText(intent.getStringExtra("laptop_nama"));
        editTextMerk.setText(intent.getStringExtra("laptop_merk"));
        editTextHarga.setText(intent.getStringExtra("laptop_harga"));
        editTextTahunRilis.setText(intent.getStringExtra("laptop_tahun_rilis"));
    }

    private void addLaptopToFirestore() {
        // Get the input values
        String nama = editTextNama.getText().toString().trim();
        String merk = editTextMerk.getText().toString().trim();
        String harga = editTextHarga.getText().toString().trim();
        String tahunRilis = editTextTahunRilis.getText().toString().trim();

        // Check if fields are empty
        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(merk) || TextUtils.isEmpty(harga) || TextUtils.isEmpty(tahunRilis)) {
            Toast.makeText(EditLaptopActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to store the laptop data
        Map<String, Object> laptop = new HashMap<>();
        laptop.put("nama", nama);
        laptop.put("merk", merk);
        laptop.put("harga", harga);
        laptop.put("tahun_rilis", tahunRilis);

        // Add the data to Firestore
        db.collection("laptops")
                .add(laptop)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(EditLaptopActivity.this, "Laptop added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after saving
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditLaptopActivity.this, "Error adding laptop: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateLaptopInFirestore() {
        // Get the input values
        String nama = editTextNama.getText().toString().trim();
        String merk = editTextMerk.getText().toString().trim();
        String harga = editTextHarga.getText().toString().trim();
        String tahunRilis = editTextTahunRilis.getText().toString().trim();

        // Check if fields are empty
        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(merk) || TextUtils.isEmpty(harga) || TextUtils.isEmpty(tahunRilis)) {
            Toast.makeText(EditLaptopActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a map to store the updated laptop data
        Map<String, Object> laptop = new HashMap<>();
        laptop.put("nama", nama);
        laptop.put("merk", merk);
        laptop.put("harga", harga);
        laptop.put("tahun_rilis", tahunRilis);

        // Update the data in Firestore
        db.collection("laptops").document(laptopId)
                .update(laptop)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(EditLaptopActivity.this, "Laptop updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after updating
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditLaptopActivity.this, "Error updating laptop: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
