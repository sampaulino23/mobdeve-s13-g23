package com.mobdeve.group23.socialfitnessapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private static final int[] pics = {R.drawable.chicken_sq, R.drawable.cow_sq,
            R.drawable.goat_sq, R.drawable.pig_sq};

    private ArrayList<Program> programList;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        populateList();
        setupRecyclerView();
    }



    private void setupRecyclerView() {
        this.recyclerView = findViewById(R.id.homeRecyclerView);
        //LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(linearLayoutManager);
        //Adapter
        ProgramListAdapter programListAdapter = new ProgramListAdapter(this.programList);
        this.recyclerView.setAdapter(programListAdapter);
    }

    private void populateList() {
        programList = new ArrayList<>();
        Program sample = new Program();

        Random rand = new Random();

        sample = new Program();
        sample.setName("Lorem ipsum dolor sit amet");
        sample.setDescription("In in libero in mauris cursus iaculis nec nec nibh. Vestibulum imperdiet elit et est tristique, id iaculis urna accumsan. Proin nisi arcu, vehicula eu metus vitae, cursus blandit elit. Proin sodales, eros et finibus posuere, lorem enim pretium augue, sit amet aliquam est quam ac sapien. Mauris pretium augue quis elit ultrices aliquet.In in libero in mauris cursus iaculis nec nec nibh. Vestibulum imperdiet elit et est tristique, id iaculis urna accumsan. Proin nisi arcu, vehicula eu metus vitae, cursus blandit elit. Proin sodales, eros et finibus posuere, lorem enIn in libero in mauris cursus iaculis nec nec nibh. Vestibulum imperdiet elit et est tristique, id iaculis urna accumsan. Proin nisi arcu, vehicula eu metus vitae, cursus blandit elit. Proin sodales, eros et finibus posuere, lorem en");
        sample.setType("Workout");
        sample.setDate("Sunday, December 30, 2021");
        sample.setTime("06:00 PM");
        sample.setLink("https://zoom.us/j/92604239679?pwd=R3RZUXNweVV3U0pMbFFtbng4Nmsydz09");
        sample.setPhoto(pics[rand.nextInt(pics.length)]);
        programList.add(0, sample);

        sample = new Program();
        sample.setName("Nulla ut tortor sollicitudin");
        sample.setDescription("In in libero in mauris cursus iaculis nec nec nibh. Vestibulum imperdiet elit et est tristique, id iaculis urna accumsan. Proin nisi arcu, vehicula eu metus vitae, cursus blandit elit. Proin sodales, eros et finibus posuere, lorem enim pretium augue, sit amet aliquam est quam ac sapien. Mauris pretium augue quis elit ultrices aliquet.");
        sample.setType("Seminar");
        sample.setDate("Friday, August 07, 2021");
        sample.setTime("12:00 PM");
        sample.setLink("https://zoom.us/j/92604239679?pwd=R3RZUXNweVV3U0pMbFFtbng4Nmsydz09");
        sample.setPhoto(pics[rand.nextInt(pics.length)]);
        programList.add(0, sample);

        sample = new Program();
        sample.setName("Ut condimentum malesuada lobortis");
        sample.setDescription("In in libero in mauris cursus iaculis nec nec nibh. Vestibulum imperdiet elit et est tristique, id iaculis urna accumsan. Proin nisi arcu, vehicula eu metus vitae, cursus blandit elit. Proin sodales, eros et finibus posuere, lorem enim pretium augue, sit amet aliquam est quam ac sapien. Mauris pretium augue quis elit ultrices aliquet.");
        sample.setType("Nutrition");
        sample.setDate("Tuesday, June 21, 2022");
        sample.setTime("08:00 AM");
        sample.setLink("https://zoom.us/j/92604239679?pwd=R3RZUXNweVV3U0pMbFFtbng4Nmsydz09");
        sample.setPhoto(pics[rand.nextInt(pics.length)]);
        programList.add(0, sample);

        sample = new Program();
        sample.setName("Vestibulum sit amet ante a orci lacinia");
        sample.setDescription("In in libero in mauris cursus iaculis nec nec nibh. Vestibulum imperdiet elit et est tristique, id iaculis urna accumsan. Proin nisi arcu, vehicula eu metus vitae, cursus blandit elit. Proin sodales, eros et finibus posuere, lorem enim pretium augue, sit amet aliquam est quam ac sapien. Mauris pretium augue quis elit ultrices aliquet.");
        sample.setType("Workout");
        sample.setDate("Monday, February 09, 2021");
        sample.setTime("04:30 PM");
        sample.setLink("https://zoom.us/j/92604239679?pwd=R3RZUXNweVV3U0pMbFFtbng4Nmsydz09");
        sample.setPhoto(pics[rand.nextInt(pics.length)]);
        programList.add(0, sample);

    }
}