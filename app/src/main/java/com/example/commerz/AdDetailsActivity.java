package com.example.commerz;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AdDetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ImageAdapter imageAdapter;
    private FirebaseFirestore db;

    private TextView Title;
    private TextView Details;
    private TextView Price;
    private TextView Location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        db = FirebaseFirestore.getInstance();

        Title = findViewById(R.id.details_title_text);
        Details = findViewById(R.id.details_details_text);
        Price = findViewById(R.id.details_price_text);
        Location = findViewById(R.id.details_location_text);

        final int position = getIntent().getExtras().getInt("card");

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
                .document("U1UhZT1BTs0s5RwuySen");


        /*documentReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Ad adddddd = documentSnapshot.toObject(Ad.class);
                        details.setText(adddddd.getTitle());
                    }
                });*/


        //U1UhZT1BTs0s5RwuySen======{ShowMail=true, Details=Test za Detali, Price=123, StringLocation=Prilep, ShowPhone=true, Title=Test Naslov}

        //String kur = "kurac"; //  dataList.get(0).get("Title").toString();

        //details.setText(kur);


        viewPager = findViewById(R.id.image_view_pager);
        imageAdapter = new ImageAdapter(this);
        viewPager.setAdapter(imageAdapter);
    }

    private class AdViewHolder extends RecyclerView.ViewHolder {
        public AdViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


}