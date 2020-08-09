package com.example.commerz;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CardItem> exampleList;

    private TextView loginTest;

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


        loginTest = view.findViewById(R.id.login_test);
        updateUserLogin();

        createExampleList();
        buildRecyclerView(view);

        mAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), AdDetailsActivity.class);
                intent.putExtra("card", position);
                //intent.putExtra("adID", );
                startActivity(intent);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUserLogin();
    }

    public void updateUserLogin() {
        if (MainActivity.loggedIn) {
            loginTest.setText("logged in");
        } else {
            loginTest.setText("NOT logged in");
        }
    }

    public void buildRecyclerView(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CardAdapter(exampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void createExampleList(){
        exampleList = new ArrayList<>();
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
        exampleList.add(new CardItem(R.drawable.ic_launcher_background, "Prva linija test", "Vtora linija", "Treta"));
    }

}