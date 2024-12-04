package com.example.binsos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Locations extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private LocationAdapter adapter;
    private List<LocationItem> locationItems;
    private Handler handler;
    private Runnable updateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerView);
        locationItems = new ArrayList<>();
        adapter = new LocationAdapter(locationItems, this::onLocationClicked, this);  // Pass context
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Setup Handler for periodic updates
        handler = new Handler();
        updateTask = new Runnable() {
            @Override
            public void run() {
                displayAllLocations();  // Fetch the data
                handler.postDelayed(this, 1000);  // Schedule the next update every 1 second
            }
        };

        // Start periodic updates
        startPeriodicUpdates();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPeriodicUpdates(); // Stop updates to avoid memory leaks
    }

    // Start periodic updates
    private void startPeriodicUpdates() {
        handler.post(updateTask); // Initial fetch
    }

    // Stop periodic updates
    private void stopPeriodicUpdates() {
        handler.removeCallbacks(updateTask);
    }

    // Method to fetch and display locations for every user
    public void displayAllLocations() {
        db.collection("users")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        locationItems.clear(); // Clear previous data
                        for (QueryDocumentSnapshot documentSnapshot : querySnapshot) {
                            String userID = documentSnapshot.getId();
                            double latitude = documentSnapshot.getDouble("latitude");
                            double longitude = documentSnapshot.getDouble("longitude");
                            long timestamp = documentSnapshot.getLong("timestamp");

                            // Calculate "time ago" for timestamp
                            long currentTimeMillis = System.currentTimeMillis();
                            long timeDiffMillis = currentTimeMillis - timestamp;

                            String timeAgo;
                            long minutes = timeDiffMillis / (1000 * 60);
                            if (minutes < 60) {
                                timeAgo = minutes + " minutes ago";
                            } else {
                                long hours = minutes / 60;
                                if (hours < 24) {
                                    timeAgo = hours + " hours ago";
                                } else {
                                    long days = hours / 24;
                                    timeAgo = days + " days ago";
                                }
                            }

                            // Create a LocationItem and add to the list
                            locationItems.add(new LocationItem(userID, latitude, longitude, timeAgo));
                        }
                        // Notify the adapter after all updates
                        adapter.notifyDataSetChanged();
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
