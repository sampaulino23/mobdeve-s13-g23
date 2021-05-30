package com.mobdeve.group23.socialfitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class HomeActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email;

//    private static final int[] pics = {R.drawable.chicken_sq, R.drawable.cow_sq,
//            R.drawable.goat_sq, R.drawable.pig_sq};

    private ArrayList<Program> programList;
    private ArrayList<Program> temp;
    private RecyclerView recyclerView;

    private Spinner homeSortSp;
    private ToggleButton homeWorkoutTBtn;
    private ToggleButton homeNutritionTBtn;
    private ToggleButton homeSeminarTBtn;
    private ToggleButton homeOthersTBtn;
    private Button homeCreateProgramBtn;
    private LinearLayout homeCreateProgramLL;

    private BottomNavigationView bottomNavigationView;

    int dateSort = 3; // 0 - closest to nearest;  1 - nearest to closest; 2 - oldest to newest; 3 - newest to oldest;

    boolean workoutFilter = false;
    boolean nutritionFilter = false;
    boolean seminarFilter = false;
    boolean othersFilter = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        db = FirebaseFirestore.getInstance();

        this.homeSortSp = findViewById(R.id.homeSortSp);
        this.homeWorkoutTBtn = findViewById(R.id.homeWorkoutTBtn);
        this.homeNutritionTBtn = findViewById(R.id.homeNutritionTBtn);
        this.homeSeminarTBtn = findViewById(R.id.homeSeminarTBtn);
        this.homeOthersTBtn = findViewById(R.id.homeOthersTBtn);
        this.homeCreateProgramBtn = findViewById(R.id.homeCreateProgramBtn);
        this.homeCreateProgramLL = findViewById(R.id.homeCreateProgramLL);

        this.bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });

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

                            //ViewGroup.LayoutParams params=recyclerView.getLayoutParams();
                            //params.height=1211;
                            homeCreateProgramLL.setVisibility(View.VISIBLE);
                            //recyclerView.setLayoutParams(params);
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






        System.out.println(workoutFilter);

//        populateList();
//        setupRecyclerView();

        ArrayList<String> sortOptions = new ArrayList<>();
        sortOptions.add(0, "Date added ▼");
        sortOptions.add("Date added ▲");
        sortOptions.add("Date of program ▼");
        sortOptions.add("Date of program ▲");



        
        ArrayAdapter<Object> dataAdapter;
        dataAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sortOptions);

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        this.homeSortSp.setAdapter(dataAdapter);

        this.homeSortSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Date of program ▲")) {
                    dateSort = 0;
                    populateList();
                    setupRecyclerView();
                }
                else if (parent.getItemAtPosition(position).equals("Date of program ▼")) {
                    dateSort = 1;
                    populateList();
                    setupRecyclerView();
                }
                else if (parent.getItemAtPosition(position).equals("Date added ▲")) {
                    dateSort = 2;
                    populateList();
                    setupRecyclerView();
                }
                else if (parent.getItemAtPosition(position).equals("Date added ▼")) {
                    dateSort = 3;
                    populateList();
                    setupRecyclerView();
                }
                else {
                    dateSort = 3;
                    populateList();
                    setupRecyclerView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.homeWorkoutTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeWorkoutTBtn.isChecked()) {
                    workoutFilter = true;
                    populateList();
                    setupRecyclerView();
                } else {
                    workoutFilter = false;
                    populateList();
                    setupRecyclerView();
                }
            }
        });

        this.homeNutritionTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeNutritionTBtn.isChecked()) {
                    nutritionFilter = true;
                    populateList();
                    setupRecyclerView();
                } else {
                    nutritionFilter = false;
                    populateList();
                    setupRecyclerView();
                }
            }
        });

        this.homeSeminarTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeSeminarTBtn.isChecked()) {
                    seminarFilter = true;
                    populateList();
                    setupRecyclerView();
                } else {
                    seminarFilter = false;
                    populateList();
                    setupRecyclerView();
                }
            }
        });

        this.homeOthersTBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(homeOthersTBtn.isChecked()) {
                    othersFilter = true;
                    populateList();
                    setupRecyclerView();
                } else {
                    othersFilter = false;
                    populateList();
                    setupRecyclerView();
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

        filterQuery(workoutFilter, nutritionFilter, seminarFilter, othersFilter, dateSort).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
//                    recyclerView.setVisibility(View.VISIBLE);
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
                        oneProgram.setId(d.getId());

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

//                    recyclerView.setVisibility(View.GONE);
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

    private Query filterQuery(boolean workout, boolean nutrition, boolean seminar, boolean others, int dateSort) {
        Query filter = db.collection("programs").orderBy("programDate", Query.Direction.ASCENDING);

        if (workout == true && nutrition == false && seminar == false && others == false) {
            if (dateSort == 0)
                filter = db.collection("programs").whereEqualTo("type", "Workout").orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereEqualTo("type", "Workout").orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereEqualTo("type", "Workout").orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereEqualTo("type", "Workout").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == true && seminar == false && others == false) {
            if (dateSort == 0)
                filter = db.collection("programs").whereEqualTo("type", "Nutrition").orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereEqualTo("type", "Nutrition").orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereEqualTo("type", "Nutrition").orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereEqualTo("type", "Nutrition").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == false && seminar == true && others == false) {
            if (dateSort == 0)
                filter = db.collection("programs").whereEqualTo("type", "Seminar").orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereEqualTo("type", "Seminar").orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereEqualTo("type", "Seminar").orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereEqualTo("type", "Seminar").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == false && seminar == false && others == true) {
            if (dateSort == 0)
                filter = db.collection("programs").whereEqualTo("type", "Others").orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereEqualTo("type", "Others").orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereEqualTo("type", "Others").orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereEqualTo("type", "Others").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == false && seminar == true && others == true) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Seminar", "Others")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Seminar", "Others")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Seminar", "Others")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Seminar", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == true && seminar == false && others == true) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Others")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Others")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Others")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == true && seminar == true && others == false) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == true && seminar == true && others == true) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar", "Others")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar", "Others")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar", "Others")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Nutrition", "Seminar", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == false && seminar == false && others == true) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Others")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Others")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Others")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == false && seminar == true && others == false) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == false && seminar == true && others == true) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar", "Others")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar", "Others")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar", "Others")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Seminar", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == true && seminar == false && others == false) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == true && seminar == false && others == true) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Others")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Others")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Others")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Others")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == true && seminar == true && others == false) {
            if (dateSort == 0)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Seminar")).orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Seminar")).orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Seminar")).orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").whereIn("type", Arrays.asList("Workout", "Nutrition", "Seminar")).orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == false && nutrition == false && seminar == false && others == false) {
            if (dateSort == 0)
                filter = db.collection("programs").orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else if (workout == true && nutrition == true && seminar == true && others == true) {
            if (dateSort == 0)
                filter = db.collection("programs").orderBy("programDate", Query.Direction.ASCENDING);
            else if (dateSort == 1)
                filter = db.collection("programs").orderBy("programDate", Query.Direction.DESCENDING);
            else if (dateSort == 2)
                filter = db.collection("programs").orderBy("creationDate", Query.Direction.ASCENDING);
            else if (dateSort == 3)
                filter = db.collection("programs").orderBy("creationDate", Query.Direction.DESCENDING);
        }
        else {
            filter = db.collection("programs").orderBy("creationDate", Query.Direction.DESCENDING);
        }

        return filter;
    }

}
