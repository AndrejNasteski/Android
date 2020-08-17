package com.example.commerz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.commerz.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        int tabInt = getIntent().getExtras().getInt("access");
        if (tabInt == 0) { // profile
            TabLayout.Tab selected = tabs.getTabAt(0);
            selected.select();
        } else if (tabInt == 1) { // my ads
            TabLayout.Tab selected = tabs.getTabAt(1);
            selected.select();
        }
    }
}