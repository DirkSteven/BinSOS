package com.example.binsos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<LocationItem> locationList;
    private OnItemClickListener onItemClickListener;

    public LocationAdapter(List<LocationItem> locationList, OnItemClickListener onItemClickListener) {
        this.locationList = locationList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        LocationItem locationItem = locationList.get(position);
        holder.locationDetails.setText("User ID: " + locationItem.getUserID() + "\n" +
                "Latitude: " + locationItem.getLatitude() + "\n" +
                "Longitude: " + locationItem.getLongitude() + "\n" +
                "Uploaded at: " + locationItem.getTimestamp());

        // Handle item click
        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(locationItem));

        // Handle delete button click
        holder.deleteButton.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            // Use the userID as the document ID
            String userID = locationItem.getUserID();

            db.collection("users").document(userID)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Remove from the local list
                        locationList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, locationList.size());
                        Toast.makeText(holder.itemView.getContext(), "Location deleted for User ID: " + userID, Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(holder.itemView.getContext(), "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(LocationItem locationItem);
    }

    public static class LocationViewHolder extends RecyclerView.ViewHolder {
        TextView locationDetails;
        Button deleteButton;

        public LocationViewHolder(View itemView) {
            super(itemView);
            locationDetails = itemView.findViewById(R.id.locationDetails);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
