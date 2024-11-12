package com.example.pamkelompok;

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

public class AddLaptopActivity extends AppCompatActivity {

    private EditText editTextNama, editTextMerk, editTextHarga, editTextTahunRilis;
    private Button submitButton;
    private FirebaseFirestore db;

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

        // Set up the submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLaptopToFirestore();
            }
        });
    }

    private void addLaptopToFirestore() {
        // Get the input values
        String nama = editTextNama.getText().toString().trim();
        String merk = editTextMerk.getText().toString().trim();
        String harga = editTextHarga.getText().toString().trim();
        String tahunRilis = editTextTahunRilis.getText().toString().trim();

        // Check if fields are empty
        if (TextUtils.isEmpty(nama) || TextUtils.isEmpty(merk) || TextUtils.isEmpty(harga) || TextUtils.isEmpty(tahunRilis)) {
            Toast.makeText(AddLaptopActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddLaptopActivity.this, "Laptop added successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Close the activity after saving
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AddLaptopActivity.this, "Error adding laptop: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
