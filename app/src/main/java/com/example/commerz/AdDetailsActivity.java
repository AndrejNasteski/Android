package com.example.commerz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdDetailsActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;

    private ViewPager viewPager;
    private ImageAdapter imageAdapter;
    private FirebaseFirestore db;

    private TextView Title;
    private TextView Details;
    private TextView Price;
    private TextView Location;
    private TextView Category;
    private Button CallButton;
    private Button EMailButton;
    private Button DeleteAdButton;
    private String phoneNumber;
    private boolean fromHome;
    private String creatorUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        db = FirebaseFirestore.getInstance();

        Title = findViewById(R.id.details_title_text);
        Details = findViewById(R.id.details_details_text);
        Price = findViewById(R.id.details_price_text);
        Location = findViewById(R.id.details_location_text);
        Category = findViewById(R.id.details_category_text);
        CallButton = findViewById(R.id.call_button);
        EMailButton = findViewById(R.id.send_email_button);
        DeleteAdButton = findViewById(R.id.delete_ad_button);

        fromHome = getIntent().getExtras().getString("from").equals("home");


        final String documentID = getIntent().getExtras().getString("documentID"); // Ad ID

        CallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        DeleteAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("Delete Ad")
                        .setMessage("Do you want to delete this ad?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        deleteAd(documentID);
                                    }
                                }).setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });


        final int position = getIntent().getExtras().getInt("card"); // for delete probably

        db.collection("ads")
                .document(documentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Ad temp = task.getResult().toObject(Ad.class);
                        Title.setText(temp.getTitle());
                        Details.setText(temp.getDetails());
                        Price.setText(temp.getPrice().toString());
                        Location.setText(temp.getStringLocation());
                        creatorUID = temp.getCreatorUID();
                        Category.setText(temp.getCategory());
                        setButtonVisibility();
                        if (fromHome && !temp.getCreatorUID().equals(MainActivity.userID)) { // user created Ad
                            db.collection("users")
                                    .document(temp.getCreatorUID())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            Log.e("newTag", task.getResult().get("name").toString());
                                            Log.e("newTag", task.getResult().get("phone").toString());
                                            phoneNumber = task.getResult().get("phone").toString();
                                        }
                                    });
                        }
                    }
                });

        viewPager = findViewById(R.id.image_view_pager);
        imageAdapter = new ImageAdapter(this);
        viewPager.setAdapter(imageAdapter);
    }


    private void deleteAd(String documentID) {
        db.collection("ads")
                .document(documentID)
                .delete();
        finish();
        Toast.makeText(this, "Ad deleted", Toast.LENGTH_LONG).show();
    }

    private class AdViewHolder extends RecyclerView.ViewHolder {
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    private void setButtonVisibility() {
        if (!MainActivity.userID.equals(creatorUID)) { // accessed form home fragment
            CallButton.setVisibility(View.VISIBLE);
            EMailButton.setVisibility(View.VISIBLE);
            DeleteAdButton.setVisibility(View.INVISIBLE);
        } else { // accessed form my_ads fragment
            CallButton.setVisibility(View.INVISIBLE);
            EMailButton.setVisibility(View.INVISIBLE);
            DeleteAdButton.setVisibility(View.VISIBLE);
        }
    }

    private void makePhoneCall() {
        String number = phoneNumber;
        if (ContextCompat.checkSelfPermission(AdDetailsActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AdDetailsActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
        } else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}