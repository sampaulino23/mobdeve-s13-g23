package com.mobdeve.group23.socialfitnessapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class ProgramActivity extends AppCompatActivity {

    private TextView sProgramNameTv;
    private TextView sProgramTypeTv;
    private TextView sProgramDateTimeTv;
    private TextView sProgramDescriptionTv;
    private TextView sProgramLinkTv;
    private ImageView sProgramPhotoIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);

        this.sProgramNameTv = findViewById(R.id.sProgramNameTv);
        this.sProgramTypeTv = findViewById(R.id.sProgramTypeTv);
        this.sProgramDateTimeTv = findViewById(R.id.sProgramDateTimeTv);
        this.sProgramDescriptionTv = findViewById(R.id.sProgramDescriptionTv);
        this.sProgramLinkTv = findViewById(R.id.sProgramLinkTv);
        this.sProgramPhotoIv = findViewById(R.id.sProgramPhotoIv);

        Intent i = getIntent();
        String sProgramNameTv = i.getStringExtra("name");
        String sProgramTypeTv = i.getStringExtra("type");
        String sProgramDateTimeTv = i.getStringExtra("dateTime");
        String sProgramDescriptionTv = i.getStringExtra("description");
        String sProgramLinkTv = i.getStringExtra("link");
        String sProgramPhotoURL = i.getStringExtra("photoURL");
        Bundle bundle = i.getExtras();
//        int sProgramPhotoIv = bundle.getInt("photo");

        this.sProgramNameTv.setText(sProgramNameTv);
        this.sProgramTypeTv.setText(sProgramTypeTv);
        this.sProgramDateTimeTv.setText(sProgramDateTimeTv);
        this.sProgramDescriptionTv.setText(sProgramDescriptionTv);
        this.sProgramLinkTv.setText(sProgramLinkTv);
//        this.sProgramPhotoIv.setImageResource(sProgramPhotoIv);

        Glide.with(this)
                .load(sProgramPhotoURL)
                .into(this.sProgramPhotoIv);
    }
}