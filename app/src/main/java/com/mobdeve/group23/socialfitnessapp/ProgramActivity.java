package com.mobdeve.group23.socialfitnessapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProgramActivity extends AppCompatActivity {

    private TextView sProgramNameTv;
    private TextView sProgramTypeTv;
    private TextView sProgramDateTimeTv;
    private TextView sProgramDescriptionTv;
    private LinearLayout sProgramLinkLL;
    private TextView sProgramLinkTv;
    private ImageView sProgramPhotoIv;
    private Button sProgramJoinBtn;
    private ProgressBar sProgramProgressBar;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email;
    String sProgramId;
    private FirebaseFirestore db;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private ShareButton sbLink;
    private ShareButton sbPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program);

        this.sProgramNameTv = findViewById(R.id.sProgramNameTv);
        this.sProgramTypeTv = findViewById(R.id.sProgramTypeTv);
        this.sProgramDateTimeTv = findViewById(R.id.sProgramDateTimeTv);
        this.sProgramDescriptionTv = findViewById(R.id.sProgramDescriptionTv);
        this.sProgramLinkLL = findViewById(R.id.sProgramLinkLL);
        this.sProgramLinkTv = findViewById(R.id.sProgramLinkTv);
        this.sProgramPhotoIv = findViewById(R.id.sProgramPhotoIv);
        this.sProgramJoinBtn = findViewById(R.id.sProgramJoinBtn);
        this.loginButton = findViewById(R.id.login_button);
        this.sbLink = findViewById(R.id.sb_link);
        this.sbPhoto = findViewById(R.id.sb_photo);
        this.sProgramProgressBar = findViewById(R.id.sProgramProgressBar);
        sProgramProgressBar.setVisibility(View.INVISIBLE);
        db = FirebaseFirestore.getInstance();
        callbackManager = CallbackManager.Factory.create();

        loginButton.setPermissions(Arrays.asList("email, public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Tag", "Login Successful");
                sbPhoto.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {
                Log.d("Tag", "Login canceled");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Tag", "Login error");
            }
        });

        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                email = profile.getEmail();
            }
            // Name, email address, and profile photo Url

        }

        Intent i = getIntent();
        String sProgramNameTv = i.getStringExtra("name");
        String sProgramTypeTv = i.getStringExtra("type");
        String sProgramDateTimeTv = i.getStringExtra("dateTime");
        String sProgramDescriptionTv = i.getStringExtra("description");
        String sProgramLinkTv = i.getStringExtra("link");
        String sProgramPhotoURL = i.getStringExtra("photoURL");
        sProgramId = i.getStringExtra("id");
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



        DocumentReference docRef = db.collection("users").document("TZG2PktVnkOxQcVLWZzB");
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
                            sbPhoto.setVisibility(View.GONE);
                            loginButton.setVisibility(View.GONE);
                        }

                        else {
                            Log.d("TAG", "DocumentSnapshot data NOT ADMIN: " + document.getId());
                        }
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        db.collection("usersJoined")
                .whereEqualTo("email", email).whereEqualTo("programID", sProgramId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            Log.d("TAG", "Inside successful");
                            System.out.println("EMAIL INSIDE USERS JOINED: " + email + sProgramId);
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                if (document.getData().get("email").equals(email) && document.getData().get("programID").equals(sProgramId)) {
                                    System.out.println("SIZE: " + document.getData().size());
                                    sProgramLinkLL.setVisibility(View.VISIBLE);
                                    sProgramJoinBtn.setEnabled(false);
                                    sProgramProgressBar.setVisibility(View.INVISIBLE);


                                    loginButton.setVisibility(View.VISIBLE);
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });


        sProgramJoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sProgramProgressBar.setVisibility(View.VISIBLE);
                Map<String, Object> usersJoined = new HashMap<>();
                usersJoined.put("email", email);
                usersJoined.put("programID", sProgramId);

                db.collection("usersJoined").add(usersJoined)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                sProgramProgressBar.setVisibility(View.INVISIBLE);
                                finish();
                                overridePendingTransition(0,0);
                                startActivity(getIntent());
                                overridePendingTransition(0,0);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                sProgramProgressBar.setVisibility(View.INVISIBLE);
                                Toast t = Toast.makeText(
                                        getApplicationContext(),
                                        "FAILED",
                                        Toast.LENGTH_SHORT
                                );
                                t.show();
                            }
                        });

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

//        ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
//                .setContentUrl(Uri.parse("https://youtu.be/GxrxV37a9YE"))
//                .setShareHashtag(new ShareHashtag.Builder()
//                    .setHashtag("#MOBDEVE").build())
//                .setQuote(sProgramDescriptionTv.getText().toString())
//
//                .build();
//
//        sbLink.setShareContent(shareLinkContent);

        BitmapDrawable bitmapDrawable = (BitmapDrawable) sProgramPhotoIv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        SharePhoto sharePhoto = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();

        SharePhotoContent sharePhotoContent = new SharePhotoContent.Builder()
                .addPhoto(sharePhoto)
                .setShareHashtag(new ShareHashtag.Builder()
                    .setHashtag("#" + sProgramNameTv.getText().toString().replace(" ", "")).build())
                .build();

        sbPhoto.setShareContent(sharePhotoContent);


//        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//                Log.d("Tag", object.toString());
//                try {
//                    String name = object.getString("name");
//                    String email = object.getString("email");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Bundle bundle = new Bundle();
//        bundle.putString("fields", "email, name");
//
//        graphRequest.setParameters(bundle);
//        graphRequest.executeAsync();
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                LoginManager.getInstance().logOut();
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LoginManager.getInstance().logOut();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.startTracking();

    }

}