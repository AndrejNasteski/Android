package com.example.commerz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    public static boolean loggedIn;
    public static String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loggedIn = FirebaseAuth.getInstance().getCurrentUser() != null;
        userID = FirebaseAuth.getInstance().getUid();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();


        navigationView.setNavigationItemSelectedListener(this);



        View headView = navigationView.getHeaderView(0);
        ImageView imgProfile = headView.findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AccountActivity.class);
                i.putExtra("access", 0);
                startActivity(i);
            }
        });


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Bundle b = new Bundle();
        b.putString("list", "home");
        HomeFragment hf = new HomeFragment();
        hf.setArguments(b);
        fragmentTransaction.add(R.id.flMain, hf);
        fragmentTransaction.commit();

        navigationView.setCheckedItem(R.id.nav_home);
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            Bundle b = new Bundle();
            b.putString("list", "home");
            HomeFragment hf = new HomeFragment();
            hf.setArguments(b);
            fragmentTransaction.replace(R.id.flMain, hf);
            fragmentTransaction.commit();
        } else if (id == R.id.nav_profile) {
            if (loggedIn) {
                openAccountActivity(0);
            } else {
                Toast.makeText(this, "You need to login first", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_add) {
            if (loggedIn) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.flMain, new NewAdFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                Toast.makeText(this, "You need to login to an account to make an ad", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_my_ads) {
            if (loggedIn) {
                openAccountActivity(1);
            } else {
                Toast.makeText(this, "You need to login first", Toast.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_register) {
            androidx.fragment.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.flMain, new RegisterFragment());
            ft.commit();
        } else if (id == R.id.nav_login) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.flMain, new LoginFragment());
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void openAccountActivity(int access) { // 0 profile, 1 my ads
        Intent i = new Intent(MainActivity.this, AccountActivity.class);
        i.putExtra("access", access);
        startActivity(i);
    }


}