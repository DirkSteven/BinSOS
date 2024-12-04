package com.example.binsos;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FloodSurvivalActivity extends AppCompatActivity {

    // Declare UI components
    private ImageView floodMainImage, prepareImage, staySafeImage, recoverImage;
    private TextView floodTitle, floodDescription;
    private TextView sectionPrepareTitle, prepareDescription;
    private TextView sectionStaySafeTitle, staySafeDescription;
    private TextView sectionRecoverTitle, recoverDescription;
    private FloatingActionButton backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flood_survival); // Ensure this matches your XML filename

        // Initialize UI components
        initializeUI();

        // Set initial data
        initializeContent();
        backBtn.setOnClickListener(v -> finish());
    }

    private void initializeUI() {
        floodMainImage = findViewById(R.id.floodMainImage);
        floodTitle = findViewById(R.id.floodTitle);
        floodDescription = findViewById(R.id.floodDescription);

        sectionPrepareTitle = findViewById(R.id.sectionPrepareTitle);
        prepareImage = findViewById(R.id.prepareImage);
        prepareDescription = findViewById(R.id.prepareDescription);

        sectionStaySafeTitle = findViewById(R.id.sectionStaySafeTitle);
        staySafeImage = findViewById(R.id.staySafeImage);
        staySafeDescription = findViewById(R.id.staySafeDescription);

        sectionRecoverTitle = findViewById(R.id.sectionRecoverTitle);
        recoverImage = findViewById(R.id.recoverImage);
        recoverDescription = findViewById(R.id.recoverDescription);
        backBtn = findViewById(R.id.backBtn);
    }

    private void initializeContent() {
        // Main section
        floodTitle.setText(getString(R.string.flood_guide_title));
        floodDescription.setText(getString(R.string.flood_guide_description));
        floodMainImage.setImageResource(R.drawable.siren);
        floodMainImage.setContentDescription(getString(R.string.main_image_content_desc));

        // Section 1: Prepare Before the Flood
        setupSection(
                sectionPrepareTitle,
                prepareImage,
                prepareDescription,
                R.string.prepare_title,
                R.drawable.prepare,
                R.string.prepare_image_content_desc,
                R.string.prepare_description);

        // Section 2: Stay Safe During the Flood
        setupSection(
                sectionStaySafeTitle,
                staySafeImage,
                staySafeDescription,
                R.string.stay_safe_title,
                R.drawable.stay_safe,
                R.string.stay_safe_image_content_desc,
                R.string.stay_safe_description);

        // Section 3: Recover After the Flood
        setupSection(
                sectionRecoverTitle,
                recoverImage,
                recoverDescription,
                R.string.recover_title,
                R.drawable.recover,
                R.string.recover_image_content_desc,
                R.string.recover_description);
    }

    private void setupSection(TextView titleView, ImageView imageView, TextView descriptionView,
                              int titleResId, int imageResId, int imageContentDescResId, int descriptionResId) {
        titleView.setText(getString(titleResId));
        imageView.setImageResource(imageResId);
        imageView.setContentDescription(getString(imageContentDescResId));
        descriptionView.setText(getString(descriptionResId));
    }
}
