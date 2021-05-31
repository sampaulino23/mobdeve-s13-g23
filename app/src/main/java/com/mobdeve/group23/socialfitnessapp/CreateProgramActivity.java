package com.mobdeve.group23.socialfitnessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreateProgramActivity extends AppCompatActivity {

    private EditText createNameEt;
    private EditText createDescriptionEt;
    private RadioGroup createTypeRGrp;
    private RadioButton createTypeRBtn;
    private RadioButton createWorkoutRBtn;
    private RadioButton createNutritionRBtn;
    private RadioButton createSeminarRBtn;
    private RadioButton createOthersRBtn;
    private EditText createDateTimeEt;
    private EditText createLinkEt;
    private TextView createFilenameTv;
    private Button createProgramBtn;
    private ImageView createUploadIcon;



    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    boolean validProgramDate = false;
    Date programDate;
    Date notificationDate;

    // for uploading images
    private Uri imageUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    String photoURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        this.createNameEt = findViewById(R.id.createNameEt);
        this.createDescriptionEt = findViewById(R.id.createDescriptionEt);
        this.createTypeRGrp = findViewById(R.id.createTypeRGrp);
        this.createWorkoutRBtn = findViewById(R.id.createWorkoutRBtn);
        this.createNutritionRBtn = findViewById(R.id.createNutritionRBtn);
        this.createSeminarRBtn = findViewById(R.id.createSeminarRBtn);
        this.createOthersRBtn = findViewById(R.id.createOthersRBtn);
        this.createDateTimeEt = findViewById(R.id.createDateTimeEt);
        this.createLinkEt = findViewById(R.id.createLinkEt);
        this.createFilenameTv = findViewById(R.id.createFilenameTv);
        this.createProgramBtn = findViewById(R.id.createProgramBtn);
        this.createUploadIcon = findViewById(R.id.createUploadIcon);

        this.createDateTimeEt.setInputType(InputType.TYPE_NULL);

        createDateTimeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(createDateTimeEt);
            }
        });

        createProgramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProgram();
            }
        });

        createUploadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

    }

    private void showDateTimeDialog(EditText createDateTimeEt) {
        Calendar calendar = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                calendar2.set(Calendar.YEAR, year);
                calendar2.set(Calendar.MONTH, month);
                calendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        calendar2.set(Calendar.HOUR_OF_DAY, hourOfDay-1);
                        calendar2.set(Calendar.MINUTE, minute);

                        programDate = calendar.getTime();
                        notificationDate = calendar2.getTime();

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, MMMM dd, yyyy   hh:mm aa");
                        createDateTimeEt.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };

                new TimePickerDialog(CreateProgramActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();


                Date current = calendar.getTime();
                int diff1 =new Date().compareTo(current);

                if(diff1 < 0){
                    validProgramDate = true;
                    return;
                } else{
                    validProgramDate = false;
                }
            }
        };

        new DatePickerDialog(CreateProgramActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // this function creates and stores the program in the DB
    private void createProgram() {
        String name = createNameEt.getText().toString().trim();
        String description = createDescriptionEt.getText().toString().trim();
        int radioId = createTypeRGrp.getCheckedRadioButtonId();
        createTypeRBtn = findViewById(radioId);
        String type = createTypeRBtn.getText().toString();
//        String type = createTypeSp.getSelectedItem().toString().trim();
        String dateTime = createDateTimeEt.getText().toString().trim();
        String link = createLinkEt.getText().toString().trim();
        String filename = createFilenameTv.getText().toString().trim();

        if(name.isEmpty()) {
            createNameEt.setError("Program name is required!");
            createNameEt.requestFocus();
            return;
        }

        else if(description.isEmpty()) {
            createDescriptionEt.setError("Description is required!");
            createDescriptionEt.requestFocus();
            return;
        }

        else if(dateTime.isEmpty()) {
            createDateTimeEt.setError("Date and time of program is required!");
            createDateTimeEt.requestFocus();

            Toast t = Toast.makeText(
                    getApplicationContext(),
                    "Date and time of program is required!",
                    Toast.LENGTH_SHORT
            );
            t.show();

            return;
        }

        else if(!validProgramDate) {
            createDateTimeEt.setError("Invalid date and time of program!");
            createDateTimeEt.requestFocus();

            Toast t = Toast.makeText(
                    getApplicationContext(),
                    "Invalid date and time of program!",
                    Toast.LENGTH_SHORT
            );
            t.show();

            return;
        }

        else if(!Patterns.WEB_URL.matcher(link).matches()) {
            createLinkEt.setError("Please provide a valid link!");
            createLinkEt.requestFocus();
            return;
        }

        else if(photoURL == null) {
            Toast t = Toast.makeText(
                    getApplicationContext(),
                    "Uploading a photo is required!",
                    Toast.LENGTH_SHORT
            );
            t.show();

            return;
        }

        else {
            storeProgram();
        }

    }

    private void storeProgram() {

        String name = createNameEt.getText().toString().trim();
        String description = createDescriptionEt.getText().toString().trim();
        int radioId = createTypeRGrp.getCheckedRadioButtonId();
        createTypeRBtn = findViewById(radioId);
        String type = createTypeRBtn.getText().toString();
        String dateTime = createDateTimeEt.getText().toString().trim();
        String link = createLinkEt.getText().toString().trim();
        String filename = photoURL;


        Map<String, Object> program = new HashMap<>();
        program.put("name", name);
        program.put("description", description);
        program.put("type", type);
        program.put("dateTime", dateTime);
        program.put("link", link);
        program.put("programDate", programDate);
        program.put("notificationDate", notificationDate);
        program.put("creationDate", Timestamp.now());
        program.put("photoURL", photoURL);

        DocumentReference ref = db.collection("programs").document();
        String id = ref.getId();
        program.put("id", id);


// Add a new document with a generated ID
        db.collection("programs")
                .add(program)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {



                        String programID = documentReference.getId();
                        System.out.println("PROGRAM ID" + id);

                        Intent i = new Intent(CreateProgramActivity.this, HomeActivity.class);
                        startActivity(i);
                        finishAffinity();
                        program.put("id", programID);

                        System.out.println("PROGRAM ID 2" + programID);



                        db.collection("programs").document(programID)
                                .set(program)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error writing document", e);
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast t = Toast.makeText(
                                getApplicationContext(),
                                "FAILED",
                                Toast.LENGTH_SHORT
                        );
                        t.show();
                    }
                });

    }

    private void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            createUploadIcon.setImageURI(imageUri);
            uploadPhoto();
        }
    }

    // function for uploading program photo
    private void uploadPhoto() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");;
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference storageRef = storageReference.child("images/" + randomKey);

        storageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                        firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                photoURL = uri.toString();
                            }
                        });

                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded.", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int) progressPercent + "%");
                    }
                });
    }

}