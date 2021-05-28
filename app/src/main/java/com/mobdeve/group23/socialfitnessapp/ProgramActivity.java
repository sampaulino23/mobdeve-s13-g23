package com.mobdeve.group23.socialfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProgramActivity extends AppCompatActivity {

    private TextView sProgramNameTv;
    private TextView sProgramTypeTv;
    private TextView sProgramDateTimeTv;
    private TextView sProgramDescriptionTv;
    private TextView sProgramLinkTv;
    private ImageView sProgramPhotoIv;
    private Button sProgramJoinBtn;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email;
    private FirebaseFirestore db;

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
        this.sProgramJoinBtn = findViewById(R.id.sProgramJoinBtn);

        db = FirebaseFirestore.getInstance();

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                email = profile.getEmail();
            }
            // Name, email address, and profile photo Url

        }

        DocumentReference docRef = db.collection("users").document("3LRBU41L2e5OJpr9vgnU");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        String admin = document.getData().get("email").toString();
                        System.out.println("ADMIN" + admin);
                        System.out.println("EMAIL" + email);


                        if (admin.equals(email)) {
                            sProgramJoinBtn.setVisibility(View.GONE);
                        }
                        else {
                            Log.d("TAG", "DocumentSnapshot data NOT ADMIN: " + document.getData().get("email"));
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
        

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
                .placeholder(R.drawable.fitness)
                .into(this.sProgramPhotoIv);
    }
}