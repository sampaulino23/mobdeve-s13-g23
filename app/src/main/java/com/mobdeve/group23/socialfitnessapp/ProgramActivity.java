package com.mobdeve.group23.socialfitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ProgramActivity extends AppCompatActivity {

    private TextView sProgramNameTv;
    private TextView sProgramTypeTv;
    private TextView sProgramDateTv;
    private TextView sProgramTimeTv;
    private TextView sProgramDescriptionTv;
    private TextView sProgramLinkTv;
    private ImageView sProgramPhotoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);

        this.sProgramNameTv = findViewById(R.id.sProgramNameTv);
        this.sProgramTypeTv = findViewById(R.id.sProgramTypeTv);
        this.sProgramDateTv = findViewById(R.id.sProgramDateTv);
        this.sProgramTimeTv = findViewById(R.id.sProgramTimeTv);
        this.sProgramDescriptionTv = findViewById(R.id.sProgramDescriptionTv);
        this.sProgramLinkTv = findViewById(R.id.sProgramLinkTv);
        this.sProgramPhotoIv = findViewById(R.id.sProgramPhotoIv);

        Intent i = getIntent();
        String sProgramNameTv = i.getStringExtra("name");
        String sProgramTypeTv = i.getStringExtra("type");
        String sProgramDateTv = i.getStringExtra("date");
        String sProgramTimeTv = i.getStringExtra("time");
        String sProgramDescriptionTv = i.getStringExtra("description");
        String sProgramLinkTv = i.getStringExtra("link");
        Bundle bundle = i.getExtras();
        int sProgramPhotoIv = bundle.getInt("photo");

        this.sProgramNameTv.setText(sProgramNameTv);
        this.sProgramTypeTv.setText(sProgramTypeTv);
        this.sProgramDateTv.setText(sProgramDateTv);
        this.sProgramTimeTv.setText(sProgramTimeTv);
        this.sProgramDescriptionTv.setText(sProgramDescriptionTv);
        this.sProgramLinkTv.setText(sProgramLinkTv);
        this.sProgramPhotoIv.setImageResource(sProgramPhotoIv);
    }
}