package com.mobdeve.group23.socialfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    private EditText signupFullNameEt;
    private EditText signupEmailEt;
    private EditText signupPasswordEt;
    private EditText signupConfirmPasswordEt;
    private EditText signupBirthdateEt;
    private Button signupSignUpBtn;
    private Button signupFacebookBtn;
    private TextView signupSignInTv;

    private FirebaseAuth mAuth;

    boolean validDate = false;
//    int age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        this.signupFullNameEt = findViewById(R.id.signupFullNameEt);
        this.signupEmailEt = findViewById(R.id.signupEmailEt);
        this.signupPasswordEt = findViewById(R.id.signupPasswordEt);
        this.signupConfirmPasswordEt = findViewById(R.id.signupConfirmPasswordEt);
        this.signupBirthdateEt = findViewById(R.id.signupBirthdateEt);
        this.signupSignUpBtn = findViewById(R.id.signupSignUpBtn);
        this.signupFacebookBtn = findViewById(R.id.signupFacebookBtn);
        this.signupSignInTv = findViewById(R.id.signupSignInTv);

        mAuth = FirebaseAuth.getInstance();

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
                    System.out.println(diff1);
                    return;
                } else{
                    validDate = true;
                    System.out.println(diff1);
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
        System.out.println(validDate);

        if(fullName.isEmpty()) {
            signupFullNameEt.setError("Full name is required!");
            signupFullNameEt.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            signupEmailEt.setError("Email is required!");
            signupEmailEt.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signupEmailEt.setError("Please provide a valid email!");
            signupEmailEt.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            signupPasswordEt.setError("Password is required!");
            signupPasswordEt.requestFocus();
            return;
        }

        if(password.length() < 6) {
            signupPasswordEt.setError("Minimum password length should be 6 characters!");
            signupPasswordEt.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()) {
            signupConfirmPasswordEt.setError("Confirm Password is required!");
            signupConfirmPasswordEt.requestFocus();
            return;
        }

        if(!password.equals(confirmPassword)) {
            signupConfirmPasswordEt.setError("Passwords do not match!");
            signupConfirmPasswordEt.requestFocus();
            return;
        }

        if(birthdate.isEmpty()) {
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

        if(!validDate) {
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

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()) {
                            User user = new User(fullName, email, birthdate);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        Toast t = Toast.makeText(
                                                SignUpActivity.this,
                                                "User has been registered successfully!",
                                                Toast.LENGTH_LONG
                                        );
                                        t.show();

                                    } else {
                                        Toast t = Toast.makeText(
                                                SignUpActivity.this,
                                                "Failed to register! Try again!",
                                                Toast.LENGTH_LONG
                                        );
                                        t.show();

                                    }
                                }
                            });
                        } else {
                            Toast t = Toast.makeText(
                                   SignUpActivity.this,
                                    "Failed to register!",
                                    Toast.LENGTH_LONG
                            );
                            t.show();

                        }

                    }
                });

    }

}