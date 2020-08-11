package com.example.commerz;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

public class NewAdFragment extends Fragment {


    private EditText Title;
    private EditText Details;
    private EditText Price;
    private EditText StringLocation;
    private EditText Category;
    private CheckBox ShowMail;
    private CheckBox ShowPhone;
    private Button CreateAd;
    private RadioGroup radioGroup;


    public NewAdFragment() {
    }

    public static NewAdFragment newInstance() {
        NewAdFragment fragment = new NewAdFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_ad, container, false);

        Title = view.findViewById(R.id.edit_text_title);
        Details = view.findViewById(R.id.edit_text_details);
        Price = view.findViewById(R.id.edit_text_price);
        Category = view.findViewById(R.id.edit_text_category);
        StringLocation = view.findViewById(R.id.edit_text_location);
        ShowMail = view.findViewById(R.id.checkbox_mail);
        ShowPhone = view.findViewById(R.id.checkbox_phone);
        CreateAd = view.findViewById(R.id.create_ad_button);
        radioGroup = view.findViewById(R.id.radio_group_currency);

        CreateAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Create Ad")
                        .setMessage("Do you want to create this ad?")
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        createAd();
                                    }
                                }).setNegativeButton(android.R.string.no, null)
                        .show();
            }
        });


        return view;
    }

    public void createAd() {
        String Stitle = Title.getText().toString();
        String Sdetails = Details.getText().toString();
        String Sprice = Price.getText().toString();
        String Scategory = Category.getText().toString();
        String Slocation = StringLocation.getText().toString();
        String Scurrency = null;


        if (radioGroup.getCheckedRadioButtonId() == R.id.MKD) {
            Scurrency = "MKD";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.EUR) {
            Scurrency = "EUR";
        } else {
            Scurrency = "USD";
        }

        if (Stitle.trim().isEmpty() || Sdetails.trim().isEmpty() || Sprice.trim().isEmpty()
                || Scategory.trim().isEmpty() || Slocation.trim().isEmpty()) {
            Toast.makeText(getContext(),
                    "Please fill out all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Ad temp = new Ad(Sdetails, Long.valueOf(Sprice), Scurrency, Scategory, ShowMail.isChecked(),
                ShowPhone.isChecked(), Slocation, Stitle, MainActivity.userID, new Date());

        CollectionReference reference = FirebaseFirestore.getInstance()
                .collection("ads");
        reference.add(temp);


        Bundle b = new Bundle();
        b.putString("list", "home");
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(b);

        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMain, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(getContext(), "YES", Toast.LENGTH_LONG).show();
    }
}