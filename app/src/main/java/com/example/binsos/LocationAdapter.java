package com.example.binsos;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    private List<LocationItem> locationList;
    private OnItemClickListener onItemClickListener;

    public LocationAdapter(List<LocationItem> locationList, OnItemClickListener onItemClickListener) {
        this.locationList = locationList;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_item, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        LocationItem locationItem = locationList.get(position);
        holder.locationDetails.setText("User ID: " + locationItem.getUserID() + "\n" +
                "Latitude: " + locationItem.getLatitude() + "\n" +
                "Longitude: " + locationItem.getLongitude() + "\n" +
                "Uploaded at: " + locationItem.getTimestamp());

        holder.itemView.setOnClickListener(v -> onItemClickListener.onItemClick(locationItem));
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

        public LocationViewHolder(View itemView) {
            super(itemView);
            locationDetails = itemView.findViewById(R.id.locationDetails);
        }
    }
}
