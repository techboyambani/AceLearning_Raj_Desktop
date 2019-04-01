package com.example.dell.acelearning;

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
import java.util.Map;

public class checksubject extends AppCompatActivity {
    DatabaseReference db;
    Spinner sp;
    int flag;
    private Uri filePath;
    ProgressDialog progressDialog;
    FirebaseUser user;
    FirebaseAuth auth;
    private Button buttonChoose;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri fileuri;
    Firebase ref;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    String selectedItemText;
    String userid, user_name;
    public static final int REQUEST_CODE=1234;
    public static final String FB_STORAGE_PATH="resume/";
    public static final String FB_DATABASE_PATH="resume";
    Button ok;
    FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checksubject);
        flag=0;
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userid=user.getUid();
        mStorageRef= FirebaseStorage.getInstance().getReference();
        mDatabaseRef=FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH);
        sp= (Spinner) findViewById(R.id.spinner);
        db= FirebaseDatabase.getInstance().getReference().child("subject");
        db.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            FirebaseUser uid;
            @Override
            public void onDataChange (@NonNull com.google.firebase.database.DataSnapshot dataSnapshot){
                ArrayList<String> subjects= new ArrayList<>();
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (com.google.firebase.database.DataSnapshot childnodes : snapshot.getChildren()) {
                        String value = childnodes.getValue().toString();
                        Map<String, String> userdetails = (Map) snapshot.getValue();
                        if((userdetails.get("flag")).equals("1")) {
                            subjects.add(userdetails.get("subject"));
                        }
                    }
                    ArrayAdapter<String> f_sub= new ArrayAdapter<String>(checksubject.this,android.R.layout.simple_spinner_dropdown_item,subjects);
                    f_sub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sp.setAdapter(f_sub);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //  db.addValueEventListener(new ValueEventListener() {
        // @Override
        // public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        // ArrayList<String> subjects= new ArrayList<>();
        // for(DataSnapshot ds:dataSnapshot.getChildren())
        // {
        //   String subject= (String) ds.getValue();
        //   subjects.add(subject);
        // }
        //  ArrayAdapter<String> f_sub= new ArrayAdapter<String>(checksubject.this,android.R.layout.simple_spinner_dropdown_item,subjects);
        //  f_sub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //   sp.setAdapter(f_sub);
        //}
        // @Override
        //  public void onCancelled(@NonNull DatabaseError databaseError) {
        //   }
        //});
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        buttonChoose = (Button) findViewById(R.id.choose);
    }
    /*public void selectsubject(View view) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference("users").child(userid).child("f_subjects").setValue(selectedItemText);
        Intent i = new Intent(checksubject.this, home_fac.class);
        startActivity(i);
        finish();
    }*/
    public void uploadfile(Uri uri)
    {
        progressDialog= new ProgressDialog(checksubject.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file ...");
        progressDialog.setProgress(0);
        progressDialog.show();
        StorageReference storageReference=FirebaseStorage.getInstance().getReference("resumes");
        storageReference.child(user_name).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        String url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        mFirebaseDatabase=FirebaseDatabase.getInstance();
                        mFirebaseDatabase.getReference("temporary_faculty").child(user_name).child("resume").setValue(url);
                        Toast.makeText(checksubject.this, "file successfully uploaded ", Toast.LENGTH_SHORT).show();
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        mFirebaseDatabase.getReference("temporary_faculty").child(user_name).child("f_subjects").setValue(selectedItemText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(checksubject.this, "file not uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
        flag=1;
    }
    public void regcomp(View view)
    {
        if(flag==1) {
            Intent i = new Intent(checksubject.this, login.class);
            startActivity(i);
            finish();
        }
        else
        {
            Toast.makeText(checksubject.this, "Please upload resume!", Toast.LENGTH_SHORT).show();
        }
    }
    public void choosefile(View view) {
        if(ContextCompat.checkSelfPermission(checksubject.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            selectfile();
        }
        else
        {
            ActivityCompat.requestPermissions(checksubject.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
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
            Toast.makeText(checksubject.this,"Please provide permission..",Toast.LENGTH_SHORT);
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
            Toast.makeText(checksubject.this,"Please select file",Toast.LENGTH_SHORT);
        }
    }
    public void upd(View view) {
        if (fileuri != null)
        {
            uploadfile(fileuri);
        }
        else
            Toast.makeText(checksubject.this, "select a file", Toast.LENGTH_SHORT).show();
    }
}
