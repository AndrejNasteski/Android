package com.example.commerz;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class NewAdFragment extends Fragment {
    private final static int PICK_IMAGE_REQUEST = 1;

    private EditText Title;
    private EditText Details;
    private EditText Price;
    private EditText StringLocation;
    private EditText Category;
    private CheckBox ShowMail;
    private CheckBox ShowPhone;
    private Button CreateAd;
    private RadioGroup radioGroup;

    private Button ChooseImages;


    private ArrayList<Uri> imageURIs;
    private StorageReference storageReference;


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

        storageReference = FirebaseStorage.getInstance().getReference("images");

        ChooseImages = view.findViewById(R.id.choose_images_button);
        imageURIs = new ArrayList<>();


        ChooseImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });


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
        String Scurrency;

        if (radioGroup.getCheckedRadioButtonId() == R.id.MKD) {
            Scurrency = "MKD";
        } else if (radioGroup.getCheckedRadioButtonId() == R.id.EUR) {
            Scurrency = "EUR";
        } else {
            Scurrency = "USD";
        }

        if (!ShowMail.isChecked() && !ShowPhone.isChecked()) {
            Toast.makeText(getContext(), "Either phone number or e-mail must be shown in ad", Toast.LENGTH_SHORT).show();
            return;
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

        reference.add(temp)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        uploadFiles(task.getResult().getId());  // ad ID
                    }
                });


        Bundle b = new Bundle();
        b.putString("list", "home");
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(b);


        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.flMain, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Toast.makeText(getContext(), "New ad successfully created", Toast.LENGTH_LONG).show();
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    public void uploadFiles(String AdID) {
        if (!imageURIs.isEmpty()) {
            List<String> uriList = new ArrayList<>();
            for (int i = 0; i < imageURIs.size(); i++) {
                String imageName = AdID + i + '.' + getFileExtension(imageURIs.get(i));
                uriList.add(imageName);
                StorageReference fileRef = storageReference
                        .child(imageName);
                fileRef.putFile(imageURIs.get(i));
            }
            Map<String, List<String>> tempMap = new HashMap<>();
            tempMap.put("images", uriList);
            FirebaseFirestore.getInstance()
                    .collection("images").document(AdID)
                    .set(tempMap);

        } else {
            Toast.makeText(getContext(), "No images were selected", Toast.LENGTH_SHORT).show();
        }
    }

    public void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null) {
            if (data.getClipData() != null) {
                int total = data.getClipData().getItemCount();
                for (int i = 0; i < total; i++) {
                    Uri uri = data.getClipData().getItemAt(i).getUri();
                    imageURIs.add(uri);
                }
            } else if (data.getData() != null) {
                imageURIs.add(data.getData());
            }
        }
    }
}

