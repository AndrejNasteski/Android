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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<CardItem> exampleList;

    private TextView loginTest;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
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

        // Inflate the layout for this fragment

        loginTest = view.findViewById(R.id.login_test);
        updateUserLogin();

        createExampleList();
        buildRecyclerView(view);

        mAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getContext(), AdDetailsActivity.class);
                intent.putExtra("card", position);
                startActivity(intent);

                /*
                FragmentTransaction f = getActivity().getSupportFragmentManager().beginTransaction();
                AdDetailsFragment fragment = new AdDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("card", Integer.toString(position));
                fragment.setArguments(bundle);
                f.replace(R.id.flMain, fragment);
                f.addToBackStack(null);
                f.commit();*/


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