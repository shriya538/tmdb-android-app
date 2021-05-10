package com.example.uscfilms.detailsPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.uscfilms.R;

public class ReviewsPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_page);
        try {
            this.getSupportActionBar().hide();
        }
        catch (Exception e){
            e.printStackTrace();
        }

        Intent detailsActivityIntent = getIntent();
        String rating = detailsActivityIntent.getStringExtra("rating");
        String author = detailsActivityIntent.getStringExtra("author");
        String dateCreated = detailsActivityIntent.getStringExtra("dateCreated");
        String reviewText = detailsActivityIntent.getStringExtra("reviewText");

        TextView activityReviewStarText = findViewById(R.id.activityReviewStarText);
        activityReviewStarText.setText(rating + "/5");

        TextView activityReviewTitle = findViewById(R.id.activityReviewTitle);
        activityReviewTitle.setText("by "+ author + " on " + dateCreated);

        TextView activityReviewText = findViewById(R.id.activityReviewText);
        activityReviewText.setText(reviewText);
    }
}