package com.mobdeve.group23.socialfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private static final int[] pics = {R.drawable.chicken_sq, R.drawable.cow_sq,
            R.drawable.goat_sq, R.drawable.pig_sq};

    private ArrayList<Program> programList;
    private ArrayList<Program> temp;
    private RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = FirebaseFirestore.getInstance();

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


        List<Program> mList = new ArrayList<>();  //this is my arraylist

        programList = new ArrayList<>();
        temp = new ArrayList<>();
        Program sample = new Program();
        Random rand = new Random();

        sample = new Program();
        Program sample2 = new Program();
        


        db.collection("programs").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list) {
                        // after getting this list we are passing
                        // that list to our object class.
                        Program oneProgram = d.toObject(Program.class);

                        // after getting data from Firebase we are
                        // storing that data in our array list
                        oneProgram.setName(d.getData().get("name").toString());
                        oneProgram.setDescription(d.getData().get("description").toString());
                        oneProgram.setType(d.getData().get("type").toString());
                        oneProgram.setDate(d.getData().get("date").toString());
                        oneProgram.setTime(d.getData().get("time").toString());
                        oneProgram.setLink(d.getData().get("link").toString());

                        programList.add(oneProgram);
                    }

                    // after that we are passing our array list to our adapter class.
                    ProgramListAdapter programListAdapter = new ProgramListAdapter(programList);

                    // after passing this array list to our adapter
                    // class we are setting our adapter to our list view.
                    recyclerView.setAdapter(programListAdapter);



                }

                else {
                    Toast.makeText(HomeActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });







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