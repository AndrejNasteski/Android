package com.example.commerz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db;

    private RecyclerView mRecyclerView;
    private AdCardAdapter adCardAdapter;
    private TextView loginTest;
    private RadioGroup currency;

    private String arguments;


    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        db = FirebaseFirestore.getInstance();

        arguments = getArguments().getString("list");


        loginTest = view.findViewById(R.id.login_test);
        updateUserLogin();
        buildRecyclerView(view);

        adCardAdapter.setOnItemClickListener(new AdCardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                String documentID = documentSnapshot.getId();
                Intent intent = new Intent(getActivity(), AdDetailsActivity.class);
                intent.putExtra("documentID", documentID);
                intent.putExtra("from", arguments);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserLogin();
        adCardAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adCardAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adCardAdapter.stopListening();
    }

    public void updateUserLogin() {
        if (MainActivity.loggedIn) {
            loginTest.setText("logged in" + arguments + MainActivity.userID);
        } else {
            loginTest.setText("NOT logged in" + arguments + MainActivity.userID);
        }
    }


    private void buildRecyclerView(View view) {
        Query query = null;
        if (arguments.equals("my_ads")) { // my_ads
            query = db.collection("ads")
                    .whereEqualTo("creatorUID", MainActivity.userID)
                    .orderBy("title");
        } else { // home
            query = db.collection("ads")
                    .orderBy("title");
        }


        FirestoreRecyclerOptions<Ad> options = new FirestoreRecyclerOptions.Builder<Ad>()
                .setQuery(query, Ad.class)
                .build();
        adCardAdapter = new AdCardAdapter(options);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adCardAdapter);
    }


}