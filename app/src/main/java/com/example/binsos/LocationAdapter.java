package com.example.binsos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<LocationItem> locationItems;
    private OnLocationClickListener onLocationClickListener;
    private Context context;
    private FirebaseFirestore db;

    public interface OnLocationClickListener {
        void onLocationClicked(LocationItem locationItem);
    }

    public LocationAdapter(List<LocationItem> locationItems, OnLocationClickListener onLocationClickListener, Context context) {
        this.locationItems = locationItems;
        this.onLocationClickListener = onLocationClickListener;
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for each location item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        LocationItem locationItem = locationItems.get(position);

        // Set data to views
        holder.userIDTextView.setText("User: " + locationItem.getUserID());
        holder.latitudeTextView.setText("Latitude: " + locationItem.getLatitude());
        holder.longitudeTextView.setText("Longitude: " + locationItem.getLongitude());
        holder.timestampTextView.setText("Timestamp: " + locationItem.getTimestamp());

        // Get current location
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double myLatitude = location.getLatitude();
                        double myLongitude = location.getLongitude();

                        // Calculate the distance
                        double distance = LocationUtils.calculateDistance(
                                myLatitude, myLongitude,
                                locationItem.getLatitude(), locationItem.getLongitude()
                        );

                        // Display the distance
                        holder.distanceTextView.setText("Distance: " + String.format("%.2f", distance) + " km");
                    }
                });

        // Set click listener for the map button
        holder.viewOnMapButton.setOnClickListener(v -> onLocationClickListener.onLocationClicked(locationItem));

        // Set click listener for the delete button
        holder.deleteButton.setOnClickListener(v -> {
            // Delete location from Firestore
            db.collection("users").document(locationItem.getUserID())
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Remove the item from the list and notify the adapter
                        locationItems.remove(position);
                        notifyItemRemoved(position);
                    })
                    .addOnFailureListener(e -> {
                        // Show an error message
                        Toast.makeText(context, "Failed to delete location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return locationItems.size();
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView userIDTextView;
        TextView latitudeTextView;
        TextView longitudeTextView;
        TextView timestampTextView;
        TextView distanceTextView;
        Button viewOnMapButton;
        Button deleteButton;

        public LocationViewHolder(View itemView) {
            super(itemView);

            // Initialize views
            userIDTextView = itemView.findViewById(R.id.userIDTextView);
            latitudeTextView = itemView.findViewById(R.id.latitudeTextView);
            longitudeTextView = itemView.findViewById(R.id.longitudeTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            distanceTextView = itemView.findViewById(R.id.distanceTextView);
            viewOnMapButton = itemView.findViewById(R.id.viewOnMapButton);
            deleteButton = itemView.findViewById(R.id.deleteButton); // Initialize delete button
        }
    }
}
