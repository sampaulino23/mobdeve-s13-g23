package com.mobdeve.group23.socialfitnessapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText signupFullNameEt;
    private EditText signupEmailEt;
    private EditText signupPasswordEt;
    private EditText signupConfirmPasswordEt;
    private EditText signupBirthdateEt;
    private Button signupSignUpBtn;
    private TextView signupSignInTv;
    private ProgressBar signupProgressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    boolean validDate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.signupFullNameEt = findViewById(R.id.signupFullNameEt);
        this.signupEmailEt = findViewById(R.id.signupEmailEt);
        this.signupPasswordEt = findViewById(R.id.signupPasswordEt);
        this.signupConfirmPasswordEt = findViewById(R.id.signupConfirmPasswordEt);
        this.signupBirthdateEt = findViewById(R.id.signupBirthdateEt);
        this.signupSignUpBtn = findViewById(R.id.signupSignUpBtn);
        this.signupSignInTv = findViewById(R.id.signupSignInTv);
        this.signupProgressBar = findViewById(R.id.signupProgressBar);


        signupBirthdateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(signupBirthdateEt);
            }
        });

        this.signupSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        this.signupSignInTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });





    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        
        if(currentUser != null){
            System.out.println("UID: " + currentUser.getUid() + "       Email: " + currentUser.getEmail());
            reload();
        }
    }

    private void reload() {
        Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(i);
        finish();
    }

    private void showDateDialog(EditText createDateEt) {
        Calendar calendar = Calendar.getInstance();

//        int yearNow = calendar.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
                createDateEt.setText(simpleDateFormat.format(calendar.getTime()));

                Date current = calendar.getTime();
                int diff1 =new Date().compareTo(current);

// for age
//                int yearSelected = calendar.get(Calendar.YEAR);

//                age = yearNow - yearSelected;
//
//                if(age < 18){
//                    validDate = false;
//                    System.out.println(age);
//                    System.out.println(validDate);
//                    return;
//                } else if (age >= 18){
//                    validDate = true;
//                    System.out.println(age);
//                    System.out.println(validDate);
//                }

                if(diff1 < 0){
                    validDate = false;
                    return;
                } else{
                    validDate = true;
                }


            }
        };

        new DatePickerDialog(SignUpActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void registerUser() {
        String fullName = signupFullNameEt.getText().toString().trim();
        String email = signupEmailEt.getText().toString().trim();
        String password = signupPasswordEt.getText().toString().trim();
        String confirmPassword = signupConfirmPasswordEt.getText().toString().trim();
        String birthdate = signupBirthdateEt.getText().toString().trim();


        if(fullName.isEmpty()) {
            signupFullNameEt.setError("Full name is required!");
            signupFullNameEt.requestFocus();
            return;
        }

        else if(email.isEmpty()) {
            signupEmailEt.setError("Email is required!");
            signupEmailEt.requestFocus();
            return;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmailEt.setError("Please provide a valid email!");
            signupEmailEt.requestFocus();
            return;
        }

        else if(password.isEmpty()) {
            signupPasswordEt.setError("Password is required!");
            signupPasswordEt.requestFocus();
            return;
        }

        else if(password.length() < 6) {
            signupPasswordEt.setError("Minimum password length should be 6 characters!");
            signupPasswordEt.requestFocus();
            return;
        }

        else if(confirmPassword.isEmpty()) {
            signupConfirmPasswordEt.setError("Confirm Password is required!");
            signupConfirmPasswordEt.requestFocus();
            return;
        }

        else if(!password.equals(confirmPassword)) {
            signupConfirmPasswordEt.setError("Passwords do not match!");
            signupConfirmPasswordEt.requestFocus();
            return;
        }

        else if(birthdate.isEmpty()) {
            signupBirthdateEt.setError("Birthdate is required!");
            signupBirthdateEt.requestFocus();

            Toast t = Toast.makeText(
                    getApplicationContext(),
                    "Birthdate is required!",
                    Toast.LENGTH_SHORT
            );
            t.show();

            return;
        }

        else if(!validDate) {
            signupBirthdateEt.setError("Invalid Birthdate!");
            signupBirthdateEt.requestFocus();

            Toast t = Toast.makeText(
                    getApplicationContext(),
                    "Invalid Birthdate!",
                    Toast.LENGTH_SHORT
            );
            t.show();

            return;
        }

        else {
            signupProgressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                storeUser();
                                FirebaseUser user = mAuth.getCurrentUser();
                                signupProgressBar.setVisibility(View.INVISIBLE);
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(fullName).build();
                                user.updateProfile(profileUpdates);
                                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {
                                signupProgressBar.setVisibility(View.INVISIBLE);
                                // If sign in fails, display a message to the user.
                                Toast.makeText(SignUpActivity.this, "Registration failed: Email is already registered.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void storeUser() {

        String fullName = signupFullNameEt.getText().toString().trim();
        String email = signupEmailEt.getText().toString().trim();
        String birthdate = signupBirthdateEt.getText().toString().trim();

        Map<String, Object> user = new HashMap<>();
        user.put("fullName", fullName);
        user.put("email", email);
        user.put("birthdate", birthdate);
        user.put("isAdmin", "false");

// Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast t = Toast.makeText(
                                getApplicationContext(),
                                "FAILED",
                                Toast.LENGTH_SHORT
                        );
                        t.show();
                    }
                });
    }



}