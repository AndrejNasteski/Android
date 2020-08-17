package com.example.commerz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        arguments = getArguments().getString("list"); // accessed from ...


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

    private void buildRecyclerView(View view) {
        Query query;
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

    private void searchQuery(String text) {
        Query q = db.collection("ads")
                .startAfter(text)
                .endAt(text + "\uf8ff");

        FirestoreRecyclerOptions<Ad> options = new FirestoreRecyclerOptions.Builder<Ad>()
                .setQuery(q, Ad.class)
                .build();
        adCardAdapter = new AdCardAdapter(options);
        //mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(adCardAdapter);

    }


}