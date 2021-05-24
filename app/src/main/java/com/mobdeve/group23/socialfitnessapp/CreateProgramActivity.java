package com.mobdeve.group23.socialfitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateProgramActivity extends AppCompatActivity {

    private EditText createDateEt;
    private EditText createTimeEt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);

        this.createDateEt = findViewById(R.id.createDateEt);
        this.createTimeEt = findViewById(R.id.createTimeEt);

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
                System.out.println(simpleDateFormat.format(calendar.getTime()));
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

}