package com.mobdeve.group23.socialfitnessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity {

    private Button profileLogOutBtn;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView profileRecyclerView;

    private ArrayList<Program> programList;

    private FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String email;
    ArrayList<String> ids = new ArrayList<>();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.profileLogOutBtn = findViewById(R.id.profileLogOutBtn);
        this.bottomNavigationView = findViewById(R.id.bottom_navigation);
        this.profileRecyclerView = findViewById(R.id.profileRecyclerView);

        db = FirebaseFirestore.getInstance();

        this.profileLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                email = profile.getEmail();
            }
            // Name, email address, and profile photo Url

        }


        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0,0);
                        finish();
                        return true;
                }
                return false;
            }
        });

        populateProgramsJoined();





    }


    private void populateProgramsJoined() {

        List<Program> mList = new ArrayList<>();  //this is my arraylist

        programList = new ArrayList<>();
        Program sample = new Program();
        Random rand = new Random();

        sample = new Program();



        db.collection("usersJoined").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
//                    recyclerView.setVisibility(View.VISIBLE);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list) {
                        String id = d.get("programID").toString();
                        System.out.println("PROGRAM ID: " + id);
                        ids.add(0, id);

                        System.out.println("ALL ID: " + ids);
                        System.out.println("i" + i + ids.get(0));
                    }

                    for (int j = 0; j < ids.size(); j++) {
                        findJoinedPrograms(ids.get(j));

                    }








                }

                else {
                    Toast.makeText(ProfileActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
//                    recyclerView.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void findJoinedPrograms (String id) {

        List<Program> mList = new ArrayList<>();  //this is my arraylist

        programList = new ArrayList<>();
        Program sample = new Program();
        Random rand = new Random();

        sample = new Program();



        db.collection("programs").whereEqualTo("id", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
//                    recyclerView.setVisibility(View.VISIBLE);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list) {
                        Program oneProgram = d.toObject(Program.class);

                        // after getting this list we are passing
                        // that list to our object class.


                        // after getting data from Firebase we are
                        // storing that data in our array list
                        oneProgram.setName(d.getData().get("name").toString());
                        oneProgram.setDescription(d.getData().get("description").toString());
                        oneProgram.setType(d.getData().get("type").toString());
                        oneProgram.setDateTime(d.getData().get("dateTime").toString());
                        oneProgram.setLink(d.getData().get("link").toString());
                        oneProgram.setPhotoURL(d.getData().get("photoURL").toString());
                        oneProgram.setId(d.getId());

                        System.out.println("NAME IN PROFILE : " + d.getData().get("name").toString());

                        programList.add(oneProgram);



                    }

                    setupRecyclerView();


                }

                else {
                    Toast.makeText(ProfileActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();

//                    recyclerView.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void logout () {
        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
        finish();
    }

    private void setupRecyclerView() {
        profileRecyclerView = findViewById(R.id.profileRecyclerView);
        //LayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        profileRecyclerView.setLayoutManager(linearLayoutManager);
        //Adapter
        ProgramListAdapter programListAdapter = new ProgramListAdapter(programList);
        profileRecyclerView.setAdapter(programListAdapter);
    }
}



/*
db.collection("programs").whereEqualTo("id", id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots2) {

                                if (!queryDocumentSnapshots.isEmpty()) {
                                    List<DocumentSnapshot> list2 = queryDocumentSnapshots.getDocuments();

                                    for (DocumentSnapshot d2 : list2) {
                                        Program oneProgram = d.toObject(Program.class);

                                        // after getting this list we are passing
                                        // that list to our object class.


                                        // after getting data from Firebase we are
                                        // storing that data in our array list
                                        oneProgram.setName(d2.getData().get("name").toString());
                                        oneProgram.setDescription(d2.getData().get("description").toString());
                                        oneProgram.setType(d2.getData().get("type").toString());
                                        oneProgram.setDateTime(d2.getData().get("dateTime").toString());
                                        oneProgram.setLink(d2.getData().get("link").toString());
                                        oneProgram.setPhotoURL(d2.getData().get("photoURL").toString());
                                        oneProgram.setId(d2.getId());

                                        programList.add(oneProgram);

                                    }
                                }



                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Fail to load data..", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                    setupRecyclerView();
 */