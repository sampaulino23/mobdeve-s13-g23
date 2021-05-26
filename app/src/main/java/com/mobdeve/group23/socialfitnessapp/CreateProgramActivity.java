package com.mobdeve.group23.socialfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateProgramActivity extends AppCompatActivity {

    private EditText createNameEt;
    private EditText createDescriptionEt;
    private Spinner createTypeSp;
    private EditText createDateEt;
    private EditText createTimeEt;
    private EditText createLinkEt;
    private TextView createFilenameTv;
    private Button createProgramBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    boolean validProgramDate = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        this.createNameEt = findViewById(R.id.createNameEt);
        this.createDescriptionEt = findViewById(R.id.createDescriptionEt);
        this.createTypeSp = findViewById(R.id.createTypeSp);
        this.createDateEt = findViewById(R.id.createDateEt);
        this.createTimeEt = findViewById(R.id.createTimeEt);
        this.createLinkEt = findViewById(R.id.createLinkEt);
        this.createFilenameTv = findViewById(R.id.createFilenameTv);
        this.createProgramBtn = findViewById(R.id.createProgramBtn);

        this.createDateEt.setInputType(InputType.TYPE_NULL);
        this.createTimeEt.setInputType(InputType.TYPE_NULL);



        createDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog(createDateEt);
            }
        });

        createTimeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog(createTimeEt);
            }
        });

        createProgramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProgram();
                storeProgram();
            }
        });
    }

    private void showDateDialog(EditText createDateEt) {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy");
                createDateEt.setText(simpleDateFormat.format(calendar.getTime()));

                Date current = calendar.getTime();
                int diff1 =new Date().compareTo(current);

                if(diff1 < 0){
                    validProgramDate = true;
                    return;
                } else{
                    validProgramDate = false;
                }
            }
        };

        new DatePickerDialog(CreateProgramActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(EditText createTimeEt) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm aa");
                createTimeEt.setText(simpleDateFormat.format(calendar.getTime()));
                System.out.println(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(CreateProgramActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }

    private void createProgram() {
        String name = createNameEt.getText().toString().trim();
        String description = createDescriptionEt.getText().toString().trim();
        String type = createTypeSp.getSelectedItem().toString().trim();
        String date = createDateEt.getText().toString().trim();
        String time = createTimeEt.getText().toString().trim();
        String link = createLinkEt.getText().toString().trim();
        String filename = createFilenameTv.getText().toString().trim();

        if(name.isEmpty()) {
            createNameEt.setError("Program name is required!");
            createNameEt.requestFocus();
            return;
        }

        if(description.isEmpty()) {
            createDescriptionEt.setError("Description is required!");
            createDescriptionEt.requestFocus();
            return;
        }

        if(date.isEmpty()) {
            createDateEt.setError("Date of program is required!");
            createDateEt.requestFocus();

            Toast t = Toast.makeText(
                    getApplicationContext(),
                    "Date of program is required!",
                    Toast.LENGTH_SHORT
            );
            t.show();

            return;
        }

        if(!validProgramDate) {
            createDateEt.setError("Invalid date of program!");
            createDateEt.requestFocus();

            Toast t = Toast.makeText(
                    getApplicationContext(),
                    "Invalid date of program!",
                    Toast.LENGTH_SHORT
            );
            t.show();

            return;
        }

        if(time.isEmpty()) {
            createTimeEt.setError("Time of program is required!");
            createTimeEt.requestFocus();

            Toast t = Toast.makeText(
                    getApplicationContext(),
                    "Time of program is required!",
                    Toast.LENGTH_SHORT
            );
            t.show();

            return;
        }

        if(!Patterns.WEB_URL.matcher(link).matches()) {
            createLinkEt.setError("Please provide a valid link!");
            createLinkEt.requestFocus();
            return;
        }

    }

    private void storeProgram() {

        String name = createNameEt.getText().toString().trim();
        String description = createDescriptionEt.getText().toString().trim();
        String type = createTypeSp.getSelectedItem().toString().trim();
        String date = createDateEt.getText().toString().trim();
        String time = createTimeEt.getText().toString().trim();
        String link = createLinkEt.getText().toString().trim();
        String filename = createFilenameTv.getText().toString().trim();

        Map<String, Object> program = new HashMap<>();
        program.put("name", name);
        program.put("description", description);
        program.put("type", type);
        program.put("date", date);
        program.put("time", time);
        program.put("link", link);

// Add a new document with a generated ID
        db.collection("programs")
                .add(program)
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