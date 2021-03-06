package com.mobdeve.group23.socialfitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.login.LoginManager;
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
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProfileActivity extends AppCompatActivity {

    private Button profileLogOutBtn;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView profileRecyclerView;
    private TextView profileNameTv;
    private TextView profileEmailTv;
    private TextView profileProgramsJoined;
    private LinearLayout profileLinearLayout;


    private ArrayList<Program> programList;

    private FirebaseFirestore db;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    String email;
    String fullName;
    ArrayList<String> ids = new ArrayList<>();
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.profileLogOutBtn = findViewById(R.id.profileLogOutBtn);
        this.bottomNavigationView = findViewById(R.id.bottom_navigation);
        this.profileRecyclerView = findViewById(R.id.profileRecyclerView);
        this.profileNameTv = findViewById(R.id.profileNameTv);
        this.profileEmailTv = findViewById(R.id.profileEmailTv);
        this.profileProgramsJoined = findViewById(R.id.profileProgramsJoined);
        this.profileLinearLayout = findViewById(R.id.profileLinearLayout);


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
                fullName = profile.getDisplayName();
                profileNameTv.setText(fullName);
                profileEmailTv.setText(email);

            }
            // Name, email address, and profile photo Url

        }


        bottomNavigationView.setSelectedItemId(R.id.profile);

        // switching of navigation views
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

        DocumentReference docRef = db.collection("users").document("ToP5LeUwtr1QjCY6tViS");
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
                            profileLinearLayout.setVisibility(View.GONE);
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

        populateProgramsJoined();

    }


    private void populateProgramsJoined() {

        List<Program> mList = new ArrayList<>();  //this is my arraylist

        programList = new ArrayList<>();
        Program sample = new Program();
        Random rand = new Random();

        sample = new Program();


        // to populate list of programs joined
        db.collection("usersJoined").whereEqualTo("email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.isEmpty()) {
//                    recyclerView.setVisibility(View.VISIBLE);
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot d : list) {
                        String id = d.get("programID").toString();
                        ids.add(0, id);
                    }

                    for (int j = 0; j < ids.size(); j++) {
                        findJoinedPrograms(ids.get(j));

                    }

                }

                else {
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

    // finding of joined programs in the DB
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

    // to logout user session
    private void logout () {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
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

