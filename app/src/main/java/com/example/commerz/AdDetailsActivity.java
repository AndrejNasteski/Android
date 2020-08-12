package com.example.commerz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
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


        if (getIntent().getExtras().getString("from").equals("home")) {
            CallButton.setVisibility(View.VISIBLE);
            EMailButton.setVisibility(View.VISIBLE);
            DeleteAdButton.setVisibility(View.INVISIBLE);
        } else { // from my ads fragment
            CallButton.setVisibility(View.INVISIBLE);
            EMailButton.setVisibility(View.INVISIBLE);
            DeleteAdButton.setVisibility(View.VISIBLE);
        }


        String documentID = getIntent().getExtras().getString("documentID");

        CallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });


        final int position = getIntent().getExtras().getInt("card");

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
                        Category.setText(temp.getCategory());
                        db.collection("users")
                                .document(temp.getCreatorUID())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        phoneNumber = task.getResult().get("phone").toString();
                                    }
                                });
                    }
                });



        /*
        String userID = db.collection("/users/oaE9MS0YniHy3PqQPMF8")
                .document().get().getResult().getString("UserID");
        Log.v("tag", userID);

        db.collection("/users/oaE9MS0YniHy3PqQPMF8/Ads")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Ad tempAd = task.getResult().getDocuments().get(position).toObject(Ad.class);

                            Title.setText(tempAd.getTitle());
                            Details.setText(tempAd.getDetails());
                            Price.setText(tempAd.getPrice().toString());
                            Location.setText(tempAd.getStringLocation());

                            for (DocumentSnapshot d : task.getResult().getDocuments()) {
                                //setData(d.getData());
                                //adtemp[0] = d.toObject(Ad.class);
                                Log.v("tag", d.getId() + "======" + d.getData());
                            }
                        }
                    }
                });

        final DocumentReference documentReference = db
                .collection("/users/oaE9MS0YniHy3PqQPMF8/Ads")
                .document("U1UhZT1BTs0s5RwuySen");*/

        viewPager = findViewById(R.id.image_view_pager);
        imageAdapter = new ImageAdapter(this);
        viewPager.setAdapter(imageAdapter);
    }

    private class AdViewHolder extends RecyclerView.ViewHolder {
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
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
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CALL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}