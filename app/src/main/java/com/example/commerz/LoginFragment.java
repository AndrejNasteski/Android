package com.example.commerz;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {

    private EditText email;
    private EditText password;
    private Button login_button;
    private Button logout_button;
    private TextView textView;


    public FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    public LoginFragment() {
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        MainActivity.loggedIn = mAuth.getCurrentUser() != null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email = (EditText) view.findViewById(R.id.edit_text_email);
        password = (EditText) view.findViewById(R.id.edit_text_password);
        login_button = (Button) view.findViewById(R.id.login_button);
        logout_button = (Button) view.findViewById(R.id.logout_button);
        textView = view.findViewById(R.id.text_click_login);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null)
                    MainActivity.userID = mAuth.getCurrentUser().getUid();

            }
        };

        textView.setOnClickListener(new View.OnClickListener() { // switch to register fragment
            @Override
            public void onClick(View v) {
                FragmentTransaction f = getParentFragmentManager().beginTransaction();
                f.addToBackStack(null);
                f.replace(R.id.flMain, new RegisterFragment()).commit();
            }
        });


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLogin();
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                MainActivity.userID = null;
                MainActivity.loggedIn = mAuth.getCurrentUser() != null;
                Toast.makeText(getContext(), "You are now logged out", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void startLogin(){
        String user = email.getText().toString();
        String pass = password.getText().toString();
        if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)){
            Toast.makeText(getContext(), "Please fill out all the fields.", Toast.LENGTH_SHORT).show();
        }else{
            mAuth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FragmentTransaction f = getParentFragmentManager().beginTransaction();
                        MainActivity.loggedIn = mAuth.getCurrentUser() != null;
                        MainActivity.userID = mAuth.getUid();
                        f.replace(R.id.flMain, new HomeFragment()).commit();
                        Toast.makeText(getContext(), "You are now logged in.", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        MainActivity.loggedIn = mAuth.getCurrentUser() != null;
    }
}