package com.example.dell.acelearning;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
public class upload_material extends AppCompatActivity {
    DatabaseReference db;
    String f_subject;
    Button back;
    private Uri filePath;
    int flag=0;
    ProgressDialog progressDialog;
    FirebaseUser user;
    private Firebase mref;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    EditText title;
    FirebaseAuth auth;
    private Button buttonChoose;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri fileuri;
    Firebase ref;
    String url;
   Spinner filetype;
    String selectedItemText;
    String userid;
    FirebaseStorage storage;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_material);
        flag=0;
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userid=user.getUid();
        back=findViewById(R.id.back);
        title=(EditText)findViewById(R.id.title);
        filetype=findViewById(R.id.filetype);
        buttonChoose = (Button) findViewById(R.id.choose);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("users");
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(upload_material.this,home_fac.class);
                startActivity(i);
                return;
            }
        });
        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue().toString();
                Map<String, String> usertype = (Map) dataSnapshot.getValue();
                f_subject = usertype.get("f_subjects");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        List<String> list = new ArrayList<String>();
        list.add("lectures");
        list.add("notes");
        list.add("assignments");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filetype.setAdapter(dataAdapter);
        filetype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    public void regcomp(View view)//ok button
    {
        if(flag==1) {
            Intent i = new Intent(upload_material.this, home_fac.class);
            startActivity(i);
            finish();
        }
        else {
            Toast.makeText(upload_material.this, "Please upload file", Toast.LENGTH_SHORT).show();
        return;
        }
    }
    public void choosefile(View view) {
        if(ContextCompat.checkSelfPermission(upload_material.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            flag=0;
            selectfile();
        }
        else
        {
            ActivityCompat.requestPermissions(upload_material.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults)
    {
        if(requestCode==9 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
        {
            selectfile();
        }
        else
            Toast.makeText(upload_material.this,"Please provide permission..",Toast.LENGTH_SHORT);
    }
    private void selectfile()
    {
        Intent i=new Intent();
        i.setType("application/pdf");
        i.setAction(i.ACTION_GET_CONTENT);
        startActivityForResult(i,86);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==86 && resultCode==RESULT_OK && data!=null)
        {
            fileuri=data.getData();
        }
        else
        {
            Toast.makeText(upload_material.this,"Please select file",Toast.LENGTH_SHORT);
        }
    }
    public void upd(View view) {
        if (fileuri != null)
        {
            uploadfile(fileuri);
        }
        else
            Toast.makeText(upload_material.this, "select a file", Toast.LENGTH_SHORT).show();
    }
    public void uploadfile(Uri uri)
    {
        progressDialog= new ProgressDialog(upload_material.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file ...");
        progressDialog.setProgress(0);
        progressDialog.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("study_material");
        storageReference.child(f_subject).child(selectedItemText).child(title.getText().toString()).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        mFirebaseDatabase=FirebaseDatabase.getInstance();
                        mFirebaseDatabase.getReference("study_material").child(f_subject).child(selectedItemText).child(title.getText().toString()).setValue(url);
                        Toast.makeText(upload_material.this, "file successfully uploaded ", Toast.LENGTH_SHORT).show();
                        flag=1;
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(upload_material.this, "file not uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i= new Intent(upload_material.this, home_fac.class);
        startActivity(i);
        finish();
    }
}








