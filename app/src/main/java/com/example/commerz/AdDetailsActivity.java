package com.example.commerz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import java.util.List;

public class AdDetailsActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;

    private ViewPager viewPager;
    private ImageAdapter imageAdapter;
    private FirebaseFirestore db;

    private TextView Title, Details, Price, Location, Category, phoneText, emailText;
    private Button DeleteAdButton;
    private String phoneNumber, emailAddress, creatorUID;
    private ImageView callImage, emailImage;
    private boolean fromHome, showPhone, showMail;


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
        DeleteAdButton = findViewById(R.id.delete_ad_button);
        phoneText = findViewById(R.id.details_phone_number);
        emailText = findViewById(R.id.details_email_text);
        callImage = findViewById(R.id.call_image_button);
        emailImage = findViewById(R.id.email_image_button);


        fromHome = getIntent().getExtras().getString("from").equals("home");
        final String documentID = getIntent().getExtras().getString("documentID"); // Ad ID


        callImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });

        emailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
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

        db.collection("ads")
                .document(documentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Ad temp = task.getResult().toObject(Ad.class);
                        Title.setText(temp.getTitle());
                        Details.setText(temp.getDetails());
                        Price.setText(temp.getPrice().toString() + " " + temp.getCurrency());
                        Location.setText(temp.getStringLocation());
                        creatorUID = temp.getCreatorUID();
                        Category.setText(temp.getCategory());
                        showMail = temp.getShowMail();
                        showPhone = temp.getShowPhone();
                        phoneText.setText("Number isn't shown");
                        emailText.setText("E-mail isn't shown");
                        setButtonVisibility();
                        if (fromHome && !temp.getCreatorUID().equals(MainActivity.userID)) { // user created Ad
                            db.collection("users")
                                    .document(temp.getCreatorUID())
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot ds = task.getResult();
                                            phoneNumber = ds.get("phone").toString();
                                            emailAddress = ds.get("email").toString();
                                            if (!showPhone) {
                                                callImage.setImageResource(R.drawable.ic_call_disabled);
                                                callImage.setClickable(false);
                                                callImage.setFocusable(false);
                                                phoneText.setText("Number isn't shown");
                                            } else {
                                                callImage.setImageResource(R.drawable.ic_call);
                                                callImage.setClickable(true);
                                                callImage.setFocusable(true);
                                                phoneText.setText(phoneNumber);
                                            }
                                            if (!showMail) {
                                                emailImage.setImageResource(R.drawable.ic_email_disabled);
                                                emailImage.setClickable(false);
                                                emailImage.setFocusable(false);
                                                emailText.setText("E-mail isn't shown");
                                            } else {
                                                emailImage.setImageResource(R.drawable.ic_email);
                                                emailImage.setClickable(true);
                                                emailImage.setFocusable(true);
                                                emailText.setText(emailAddress);
                                            }
                                        }
                                    });
                        }
                    }
                });
        setupList(documentID);
    }

    public void setupList(String documentID) {
        db.collection("images")
                .document(documentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot ds = task.getResult();
                            List<String> tempList = (List<String>) ds.get("images");

                            viewPager = findViewById(R.id.image_view_pager);
                            imageAdapter = new ImageAdapter(AdDetailsActivity.this, tempList);
                            viewPager.setAdapter(imageAdapter);
                        }
                    }
                });
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
        if (!MainActivity.userID.equals(creatorUID)) { // accessed from home fragment
            callImage.setVisibility(View.VISIBLE);
            emailImage.setVisibility(View.VISIBLE);
            DeleteAdButton.setVisibility(View.INVISIBLE);
        } else { // accessed from my_ads fragment or ad is created by user
            callImage.setVisibility(View.INVISIBLE);
            emailImage.setVisibility(View.INVISIBLE);
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

    private void sendEmail() {
        String recipient = emailAddress;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, recipient);
        intent.putExtra(Intent.EXTRA_SUBJECT, Title.getText().toString());

        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose an e-mail client"));
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