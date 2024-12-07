package com.example.binsos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BreathingAndHeartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing_and_heart);

        // Find the Call for Help button
        Button callForHelpButton = findViewById(R.id.callForHelpButton);
        FloatingActionButton backBtn = findViewById(R.id.backBtn);

        // Set click listener for the button
        callForHelpButton.setOnClickListener(v -> callForHelp());
        backBtn.setOnClickListener(v -> finish());
    }

    // Method to call a specific emergency number
    private void callForHelp() {
        String emergencyNumber = "911";
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + emergencyNumber));
        startActivity(intent);
    }
}
