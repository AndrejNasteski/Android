package com.example.commerz;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters


    private FirebaseAuth mAuth;

    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText phone;
    private Button registerButton;
    private TextView textView;

    private ProgressDialog progressDialog;


    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        name = (EditText) view.findViewById(R.id.edit_text_name);
        surname = (EditText) view.findViewById(R.id.edit_text_surname);
        email = (EditText) view.findViewById(R.id.edit_text_email_register);
        phone = (EditText) view.findViewById(R.id.edit_phone_number);
        password = (EditText) view.findViewById(R.id.password_register);
        confirmPassword = (EditText) view.findViewById(R.id.password_register_confirm);
        registerButton = (Button) view.findViewById(R.id.button_register);
        progressDialog = new ProgressDialog(getContext());
        textView = view.findViewById(R.id.text_click_register);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction f = getParentFragmentManager().beginTransaction();
                f.addToBackStack(null);
                f.replace(R.id.flMain, new LoginFragment()).commit();
            }
        });

        final String user = email.getText().toString();
        final String pass = password.getText().toString();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });


        return view;
    }

    private void createAccount() {
        String user = email.getText().toString();
        String pass = password.getText().toString();

        progressDialog.setMessage("Registering");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "SUCCESSFUL REGISTER", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }
        });
    }


    private boolean validateForm() {
        boolean valid = true;

        String email_string = email.getText().toString();

        String name_string = name.getText().toString();
        String surname_string = surname.getText().toString();
        String phone_string = phone.getText().toString();
        String password_confirm_string = confirmPassword.getText().toString();

        String password_string = password.getText().toString();

        if (TextUtils.isEmpty(email_string)) {
            email.setError("Required.");
            Toast.makeText(getContext(), "wrong email", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(name_string)) {
            name.setError("Required.");
            valid = false;
        } else {
            name.setError(null);
        }

        if (TextUtils.isEmpty(surname_string)) {
            surname.setError("Required.");
            valid = false;
        } else {
            surname.setError(null);
        }

        if (TextUtils.isEmpty(phone_string)) {
            phone.setError("Required.");
            valid = false;
        } else {
            phone.setError(null);
        }

        if (TextUtils.isEmpty(password_string)) {
            password.setError("Required.");
            Toast.makeText(getContext(), "wrong pass", Toast.LENGTH_SHORT).show();
            valid = false;
        } else {
            password.setError(null);
        }

        if (TextUtils.isEmpty(password_confirm_string)) {
            confirmPassword.setError("Required.");
            valid = false;
        } else {
            confirmPassword.setError(null);
        }

        if (!password_confirm_string.equals(password_string)) {
            confirmPassword.setError("Password not matching");
        } else {
            confirmPassword.setError(null);
        }

        if (password_string.length() < 5) {
            confirmPassword.setError("Password must be longer than 5 characters");
        } else {
            confirmPassword.setError(null);
        }

        return valid;
    }

}