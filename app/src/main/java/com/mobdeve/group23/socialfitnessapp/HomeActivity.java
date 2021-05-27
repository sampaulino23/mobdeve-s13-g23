package com.mobdeve.group23.socialfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
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

    private ToggleButton homeWorkoutTBtn;
    private ToggleButton homeNutritionTBtn;
    private ToggleButton homeSeminarTBtn;
    private ToggleButton homeOthersTBtn;
    private Button homeCreateProgramBtn;

    boolean workoutFilter = false;
    boolean nutritionFilter = false;
    boolean seminarFilter = false;
    boolean othersFilter = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = FirebaseFirestore.getInstance();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        this.homeWorkoutTBtn = findViewById(R.id.homeWorkoutTBtn);
        this.homeNutritionTBtn = findViewById(R.id.homeNutritionTBtn);
        this.homeSeminarTBtn = findViewById(R.id.homeSeminarTBtn);
        this.homeOthersTBtn = findViewById(R.id.homeOthersTBtn);
        this.homeCreateProgramBtn = findViewById(R.id.homeCreateProgramBtn);

        System.out.println(workoutFilter);

        populateList();
        setupRecyclerView();

        this.homeWorkoutTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeWorkoutTBtn.isChecked()) {
                    workoutFilter = true;
                    populateList();
                } else {
                    workoutFilter = false;
                    populateList();
                }
            }
        });

        this.homeNutritionTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeNutritionTBtn.isChecked()) {
                    nutritionFilter = true;
                    populateList();
                } else {
                    nutritionFilter = false;
                    populateList();
                }
            }
        });

        this.homeSeminarTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeSeminarTBtn.isChecked()) {
                    seminarFilter = true;
                    populateList();
                } else {
                    seminarFilter = false;
                    populateList();
                }
            }
        });

        this.homeOthersTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeOthersTBtn.isChecked()) {
                    othersFilter = true;
                    populateList();
                } else {
                    othersFilter = false;
                    populateList();
                }
            }
        });

        this.homeCreateProgramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, CreateProgramActivity.class);
                startActivity(i);
                finish();
            }
        });
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

        filterQuery(workoutFilter, nutritionFilter, seminarFilter, othersFilter).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
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
                        oneProgram.setDateTime(d.getData().get("dateTime").toString());
                        oneProgram.setLink(d.getData().get("link").toString());
                        oneProgram.setPhotoURL(d.getData().get("photoURL").toString());

                        programList.add(oneProgram);
                    }

                    // after that we are passing our array list to our adapter class.
                    ProgramListAdapter programListAdapter = new ProgramListAdapter(programList);
//                        programListAdapter.notifyDataSetChanged();
                    // after passing this array list to our adapter
                    // class we are setting our adapter to our list view.
                    recyclerView.setAdapter(programListAdapter);

                }

                else {
                    Toast.makeText(HomeActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();

                    recyclerView.setVisibility(View.GONE);
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });



        /*
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
*/

    }

    private Query filterQuery(boolean workout, boolean nutrition, boolean seminar, boolean others) {
        Query filter = db.collection("programs").orderBy("creationDate", Query.Direction.DESCENDING);

        if (workout == true && nutrition == false && seminar == false && others == false) {
            filter = db.collection("programs").whereEqualTo("type", "Workout").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == true && seminar == false && others == false) {
            filter = db.collection("programs").whereEqualTo("type", "Nutrition").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == false && seminar == true && others == false) {
            filter = db.collection("programs").whereEqualTo("type", "Seminar").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == false && seminar == false && others == true) {
            filter = db.collection("programs").whereEqualTo("type", "Others").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == false && seminar == true && others == true) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Seminar", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == true && seminar == false && others == true) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == true && seminar == true && others == false) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == true && seminar == true && others == true) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == false && seminar == false && others == true) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == false && seminar == true && others == false) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == false && seminar == true && others == true) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == true && seminar == false && others == false) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == true && seminar == false && others == true) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == true && seminar == true && others == false) {
            filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Seminar")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else {
            filter = db.collection("programs").orderBy("creationDate", Query.Direction.DESCENDING);
        }

        return filter;
    }
}
