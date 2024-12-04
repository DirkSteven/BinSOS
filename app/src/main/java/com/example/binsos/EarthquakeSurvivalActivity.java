package com.example.binsos;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class EarthquakeSurvivalActivity extends AppCompatActivity {

    // Declare UI components
    private ImageView earthquakeMainImage, prepareImage, staySafeImage, recoverImage;
    private TextView earthquakeTitle, earthquakeDescription;
    private TextView sectionPrepareTitle, prepareDescription;
    private TextView sectionStaySafeTitle, staySafeDescription;
    private TextView sectionRecoverTitle, recoverDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_survival); // Ensure this matches your XML filename

        // Initialize UI components
        initializeUI();

        // Set initial data
        initializeContent();
    }

    private void initializeUI() {
        earthquakeMainImage = findViewById(R.id.earthquakeMainImage);
        earthquakeTitle = findViewById(R.id.earthquakeTitle);
        earthquakeDescription = findViewById(R.id.earthquakeDescription);

        sectionPrepareTitle = findViewById(R.id.sectionPrepareTitle);
        prepareImage = findViewById(R.id.prepareImage);
        prepareDescription = findViewById(R.id.prepareDescription);

        sectionStaySafeTitle = findViewById(R.id.sectionStaySafeTitle);
        staySafeImage = findViewById(R.id.staySafeImage);
        staySafeDescription = findViewById(R.id.staySafeDescription);

        sectionRecoverTitle = findViewById(R.id.sectionRecoverTitle);
        recoverImage = findViewById(R.id.recoverImage);
        recoverDescription = findViewById(R.id.recoverDescription);
    }

    private void initializeContent() {
        // Main section
        earthquakeTitle.setText(getString(R.string.earthquake_guide_title));
        earthquakeDescription.setText(getString(R.string.earthquake_guide_description));
        earthquakeMainImage.setImageResource(R.drawable.earthquake);
        earthquakeMainImage.setContentDescription(getString(R.string.main_image_content_desc_earthquake));

        // Section 1: Prepare Before the Earthquake
        setupSection(
                sectionPrepareTitle,
                prepareImage,
                prepareDescription,
                R.string.prepare_earthquake_title,
                R.drawable.prepare_earthquake,
                R.string.prepare_image_content_desc_earthquake,
                R.string.prepare_earthquake_description);

        // Section 2: Stay Safe During the Earthquake
        setupSection(
                sectionStaySafeTitle,
                staySafeImage,
                staySafeDescription,
                R.string.stay_safe_earthquake_title,
                R.drawable.stay_safe_earthquake,
                R.string.stay_safe_image_content_desc_earthquake,
                R.string.stay_safe_earthquake_description);

        // Section 3: Recover After the Earthquake
        setupSection(
                sectionRecoverTitle,
                recoverImage,
                recoverDescription,
                R.string.recover_earthquake_title,
                R.drawable.recover_earthquake,
                R.string.recover_image_content_desc_earthquake,
                R.string.recover_earthquake_description);
    }

    private void setupSection(TextView titleView, ImageView imageView, TextView descriptionView,
                              int titleResId, int imageResId, int imageContentDescResId, int descriptionResId) {
        titleView.setText(getString(titleResId));
        imageView.setImageResource(imageResId);
        imageView.setContentDescription(getString(imageContentDescResId));
        descriptionView.setText(getString(descriptionResId));
    }
}
