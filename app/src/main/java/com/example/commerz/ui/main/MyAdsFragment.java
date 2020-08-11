package com.example.commerz.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.commerz.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdsFragment extends Fragment {

    private FirebaseFirestore db;

    public MyAdsFragment() {
    }

    public static MyAdsFragment newInstance() {
        MyAdsFragment fragment = new MyAdsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_ads, container, false);
        db = FirebaseFirestore.getInstance();

        final Map<String, Object> putObject = new HashMap<>();
        final Map<String, Object> putObject2 = new HashMap<>();
        putObject.put("tajtl", "vrednost");
        putObject2.put("tajtl2", "vrednost2");

        final List<Map<String, Object>> lista = new ArrayList<>();
        lista.add(putObject);
        lista.add(putObject2);

        Button button = view.findViewById(R.id.put_data);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> ids = new ArrayList<>();
                List<DocumentSnapshot> lista = db.collection("users")
                        .get()
                        .getResult()
                        .getDocuments();
                for (int i = 0; i < lista.size(); i++) {
                    ids.add(lista.get(i).getId());
                }
            }
            /*
            @Override
            public void onClick(View v) { // for deletion
                db.collection("users")
                        .document("newUserTest222333kuraccc")
                        .collection("Ads")
                        .add(putObject)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(getContext(), "SUCCESFUL USER ", Toast.LENGTH_SHORT).show();
                            }
                        });
            }*/
        });
        return view;
    }


}