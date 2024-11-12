package com.example.pamkelompok;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomePage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LaptopAdapter adapter;
    private List<Laptop> laptopList;
    private FirebaseFirestore db;
    private TextView welcomeText;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the welcome message with user's email
        welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome, " + Objects.requireNonNull(firebaseAuth.getCurrentUser()).getEmail());


        // Initialize RecyclerView and Firestore
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        laptopList = new ArrayList<>();
        adapter = new LaptopAdapter(this, laptopList, new LaptopAdapter.OnLaptopListener() {
            @Override
            public void onEditClick(Laptop laptop) {
                // Handle edit action by passing data to AddLaptopActivity
                Intent intent = new Intent(HomePage.this, EditLaptopActivity.class);
                intent.putExtra("laptop_id", laptop.getId()); // Pass the document ID
                intent.putExtra("laptop_nama", laptop.getNama());
                intent.putExtra("laptop_merk", laptop.getMerk());
                intent.putExtra("laptop_harga", laptop.getHarga());
                intent.putExtra("laptop_tahun_rilis", laptop.getTahunRilis());
                startActivity(intent); // Start the activity for editing
            }

            @Override
            public void onDeleteClick(Laptop laptop) {
                // Handle delete action
                CollectionReference laptopsRef = db.collection("laptops");

                laptopsRef.document(laptop.getId()).delete();
                Toast.makeText(HomePage.this, "Delete " + laptop.getNama(), Toast.LENGTH_SHORT).show();
                fetchLaptopData();
            }
        });
        recyclerView.setAdapter(adapter);

        // Initialize Firestore and fetch data
        db = FirebaseFirestore.getInstance();
        fetchLaptopData();

        setupListener();
    }

    private void setupListener() {
        findViewById(R.id.createButton).setOnClickListener(s -> {
            Intent intent = new Intent(HomePage.this, AddLaptopActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            fetchLaptopData(); // Method to reload the RecyclerView data
        }
    }

    private void fetchLaptopData() {
        CollectionReference laptopsRef = db.collection("laptops");
        laptopsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    laptopList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String nama = document.getString("nama");
                        String merk = document.getString("merk");
                        String harga = document.getString("harga");
                        String tahunRilis = document.getString("tahunRilis");

                        // Add the laptop data to the list
                        Laptop laptop = new Laptop(id, nama, merk, harga, tahunRilis);
                        laptopList.add(laptop);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.w("HomePage", "Error getting documents.", task.getException());
                    Toast.makeText(HomePage.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
