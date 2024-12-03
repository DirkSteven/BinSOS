package com.example.binsos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class FirstAidActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid);

        // Get references to the tiles
        LinearLayout bleedingTile = findViewById(R.id.bleedingTile);
        LinearLayout breathingTile = findViewById(R.id.breathingTile);
        LinearLayout burnsTile = findViewById(R.id.burnsTile);
        LinearLayout injuriesTile = findViewById(R.id.injuriesTile);

        // Set click listeners for each tile
        bleedingTile.setOnClickListener(v -> navigateToBleedingWounds());
        breathingTile.setOnClickListener(v -> navigateToBreathingAndHeart());
        burnsTile.setOnClickListener(v -> navigateToBurns());
        injuriesTile.setOnClickListener(v -> navigateToInjuriesAndEmergencies());
    }

    private void navigateToBleedingWounds() {
        Intent intent = new Intent(FirstAidActivity.this, BleedingWoundsActivity.class);
        startActivity(intent);
    }

    private void navigateToBreathingAndHeart() {
        Intent intent = new Intent(FirstAidActivity.this, BreathingAndHeartActivity.class);
        startActivity(intent);
    }

    private void navigateToBurns() {
        Intent intent = new Intent(FirstAidActivity.this, BurnsActivity.class);
        startActivity(intent);
    }

    private void navigateToInjuriesAndEmergencies() {
        Intent intent = new Intent(FirstAidActivity.this, InjuriesAndEmergenciesActivity.class);
        startActivity(intent);
    }
}
