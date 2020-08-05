package com.example.commerz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AdDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);

        int temp = getIntent().getExtras().getInt("card");

        TextView textView = (TextView) findViewById(R.id.details_activity_text);
        textView.setText(Integer.toString(temp));
    }
}