package com.example.binsos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Locations extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private List<LocationItem> locationItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location); // Ensure you have a layout file for this activity

        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView); // Make sure RecyclerView is in your layout
        locationItems = new ArrayList<>();
        adapter = new LocationAdapter(locationItems, this::onLocationClicked);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Call displayAllLocations to fetch and display locations
        displayAllLocations();
    }

    // Method to fetch and display locations for every user
    public void displayAllLocations() {
        db.collection("users") // Fetch all documents from the 'users' collection
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        locationItems.clear(); // Clear previous data
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            String userID = documentSnapshot.getId();
                            double latitude = documentSnapshot.getDouble("latitude");
                            double longitude = documentSnapshot.getDouble("longitude");
                            long timestamp = documentSnapshot.getLong("timestamp");

                            // Format timestamp to a readable date
                            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                            String formattedDate = sdf.format(timestamp);

                            // Create a LocationItem and add to the list
                            locationItems.add(new LocationItem(userID, latitude, longitude, formattedDate));

                            // Notify the adapter that data has changed
                            adapter.notifyDataSetChanged();
                        }
                    } else {
                        Toast.makeText(this, "No locations found for any user.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch locations: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Handle location item click (open Google Maps)
    private void onLocationClicked(LocationItem locationItem) {
        String mapLink = "https://www.google.com/maps?q=" + locationItem.getLatitude() + "," + locationItem.getLongitude();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mapLink));
        startActivity(mapIntent);
    }
}
