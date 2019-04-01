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
        import android.text.TextUtils;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ProgressBar;
        import android.widget.RadioButton;
        import android.widget.RadioGroup;
        import android.widget.Spinner;
        import android.widget.Toast;

        import com.firebase.client.Firebase;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
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
        //import java.util.Map;
public class signup_fac extends AppCompatActivity {
    DatabaseReference db;
    Spinner sp;
    int flag;
    ProgressDialog progressDialog;
    private Button buttonChoose;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Uri fileuri;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference myRef;
    String selectedItemText, url;
    public static final int REQUEST_CODE=1234;
    FirebaseStorage storage;
    private EditText inputEmail, inputPassword, uname, phnno;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_fac);
        flag=0;
        buttonChoose = (Button) findViewById(R.id.choose);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        phnno = findViewById(R.id.phn_no);
        uname = findViewById(R.id.uname);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mStorageRef= FirebaseStorage.getInstance().getReference();
        sp= (Spinner) findViewById(R.id.spinner);
        db= FirebaseDatabase.getInstance().getReference().child("subject");
         db.addValueEventListener(new ValueEventListener() {
         @Override
         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        ArrayList<String> subjects= new ArrayList<>();
         for(DataSnapshot ds:dataSnapshot.getChildren())
         {
           String subject= (String) ds.getValue();
           subjects.add(subject);
         }
         ArrayAdapter<String> f_sub= new ArrayAdapter<String>(signup_fac.this,android.R.layout.simple_spinner_dropdown_item,subjects);
         f_sub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         sp.setAdapter(f_sub);
        }
         @Override
         public void onCancelled(@NonNull DatabaseError databaseError) {
           }
        });
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemText = (String) parent.getItemAtPosition(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signup_fac.this, login.class);
                startActivity(i);
                finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String name = uname.getText().toString();
                final String phn = phnno.getText().toString();
                String MobilePattern = "[0-9]{10}";
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(phn)) {
                    Toast.makeText(getApplicationContext(), "Enter mobile number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!(phn.matches(MobilePattern)))
                {   Toast.makeText(getApplicationContext(), "Please enter 10 digit mobile number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(flag==0)
                {Toast.makeText(getApplicationContext(), "Please upload your resume", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(selectedItemText))
                {
                    Toast.makeText(getApplicationContext(), "Please select a subject", Toast.LENGTH_SHORT).show();
                    return;
                }

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference();
                myRef.child("temporary_faculty").child(phn).setValue(new UserInformation(name,email, "faculty", phn, password,selectedItemText,url ));
                                myRef.child("subject").child(selectedItemText).removeValue();

                    Intent i = new Intent(signup_fac.this, login.class);
                    startActivity(i);
                    finish();
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
    public void choosefile(View view) {
        if(ContextCompat.checkSelfPermission(signup_fac.this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
        {
            flag=0;
            selectfile();
        }
        else
        {
            ActivityCompat.requestPermissions(signup_fac.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);
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
            Toast.makeText(signup_fac.this,"Please provide permission..",Toast.LENGTH_SHORT);
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
            Toast.makeText(signup_fac.this,"Please select file",Toast.LENGTH_SHORT);
        }
    }
    public void upd(View view) {
        if (fileuri != null)
        {
            uploadfile(fileuri);
        }
        else
            Toast.makeText(signup_fac.this, "select a file", Toast.LENGTH_SHORT).show();
    }
    public void uploadfile(Uri uri)
    {
        progressDialog= new ProgressDialog(signup_fac.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle("Uploading file ...");
        progressDialog.setProgress(0);
        progressDialog.show();
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("resumes");
        storageReference.child(phnno.getText().toString()).putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       url = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                        mFirebaseDatabase=FirebaseDatabase.getInstance();
                        mFirebaseDatabase.getReference("temporary_faculty").child(phnno.getText().toString()).child("resume").setValue(url);
                        Toast.makeText(signup_fac.this, "file successfully uploaded ", Toast.LENGTH_SHORT).show();
                        flag=1;
                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                        mFirebaseDatabase.getReference("temporary_faculty").child(phnno.getText().toString()).child("f_subjects").setValue(selectedItemText);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signup_fac.this, "file not uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                int currentProgress= (int) (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setProgress(currentProgress);
            }
        });
    }

}
