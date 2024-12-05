package com.example.binsos;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.FirebaseApp;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_USER_ID = "userID";

    private ImageButton shareLocationBtn,firstAidBtn, rainBtn, earthquakeBtn;
    private Button locationBtn;
    private FusedLocationProviderClient fusedLocationClient;
    private FirebaseFirestore db;
    private ActivityResultLauncher<String[]> locationPermissionRequest;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize the location button
        shareLocationBtn = findViewById(R.id.shareLocationBtn);
        locationBtn = findViewById(R.id.locationBtn);
        firstAidBtn = findViewById(R.id.firstAidBtn);
        rainBtn = findViewById(R.id.rainBtn);
        earthquakeBtn = findViewById(R.id.earthquakeBtn);


        // Retrieve or generate a unique user ID
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userID = prefs.getString(KEY_USER_ID, null);
        if (userID == null) {
            userID = UUID.randomUUID().toString();
            prefs.edit().putString(KEY_USER_ID, userID).apply();
        }

        // Initialize permission request launcher
        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if (fineLocationGranted != null && fineLocationGranted) {
                        fetchAndShareLocation();
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        fetchAndShareLocation();
                    } else {
                        Toast.makeText(this, "Location permissions are required to share your location.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        firstAidBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FirstAidActivity.class);
            startActivity(intent);
        });

        rainBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FloodSurvivalActivity.class);
            startActivity(intent);
        });

        earthquakeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EarthquakeSurvivalActivity.class);
            startActivity(intent);
        });



        // Set up click listener for the button
        shareLocationBtn.setOnClickListener(v -> {
            // Check for permissions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Request permissions
                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            } else {
                // Permissions are already granted
                fetchAndShareLocation();
            }
        });



        locationBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Locations.class);
            startActivity(intent);
        });
    }

    private void fetchAndShareLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    shareLocation(location);
                } else {
                    Toast.makeText(this, "Unable to fetch location. Try again later.", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(e ->
                    Toast.makeText(this, "Error fetching location: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        } else {
            Toast.makeText(this, "Location permissions are required to fetch the location.", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareLocation(Location location) {
        Map<String, Object> locationData = new HashMap<>();
        locationData.put("latitude", location.getLatitude());
        locationData.put("longitude", location.getLongitude());
        locationData.put("timestamp", System.currentTimeMillis());

        // Save the location data under the unique user ID
        db.collection("users")
                .document(userID) // Use userID as the document ID
                .set(locationData)
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Location shared successfully!", Toast.LENGTH_SHORT).show()
                )
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to share location: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }
}