package com.example.commerz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class AdDetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);

        int temp = getIntent().getExtras().getInt("card");

        viewPager = findViewById(R.id.image_view_pager);
        imageAdapter = new ImageAdapter(this);
        viewPager.setAdapter(imageAdapter);
    }
}