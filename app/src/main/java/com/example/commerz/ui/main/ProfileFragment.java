package com.example.commerz.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.commerz.MainActivity;
import com.example.commerz.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class ProfileFragment extends Fragment {

    private EditText email;
    private EditText name;
    private EditText surname;
    private EditText phone;
    private Button editButton;
    private Button saveButton;

    private FirebaseFirestore db;


    public ProfileFragment() {
    }

    public static ProfileFragment newInstance() {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        db = FirebaseFirestore.getInstance();

        email = view.findViewById(R.id.profile_user_email);
        name = view.findViewById(R.id.profile_user_name);
        surname = view.findViewById(R.id.profile_user_surname);
        phone = view.findViewById(R.id.profile_user_phone);
        editButton = view.findViewById(R.id.profile_button_edit);
        saveButton = view.findViewById(R.id.profile_button_save);


        db.collection("users")
                .document(MainActivity.userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot temp = task.getResult();
                            email.setText(temp.get("email").toString());
                            name.setText(temp.get("name").toString());
                            surname.setText(temp.get("surname").toString());
                            phone.setText(temp.get("phone").toString());
                        }
                    }
                });


        disableButtons();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setEnabled(true);
                surname.setEnabled(true);
                phone.setEnabled(true);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> testObject = new HashMap<>();
                testObject.put("name", name.getText().toString());
                testObject.put("surname", surname.getText().toString());
                testObject.put("phone", phone.getText().toString());
                db.collection("users")
                        .document(MainActivity.userID)
                        .update(testObject);
                Toast.makeText(getContext(), "Changes saved", Toast.LENGTH_SHORT).show();
                disableButtons();
            }
        });
        return view;
    }

    private void disableButtons() {
        email.setEnabled(false);
        name.setEnabled(false);
        surname.setEnabled(false);
        phone.setEnabled(false);
    }
}