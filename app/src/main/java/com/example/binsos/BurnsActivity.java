package com.example.binsos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BurnsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burns);

        // Find the Call for Help button
        Button callForHelpButton = findViewById(R.id.callForHelpButton);
        FloatingActionButton backBtn = findViewById(R.id.backBtn);

        // Set click listener for the button
        callForHelpButton.setOnClickListener(v -> callForHelp());
        backBtn.setOnClickListener(v -> finish());
    }

    // Method to call a specific emergency number
    private void callForHelp() {
        String emergencyNumber = "911"; // You can replace this with any emergency number
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + emergencyNumber));
        startActivity(intent);
    }
}
