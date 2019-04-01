package com.example.dell.acelearning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

/**
 * Created by User on 2/8/2017.
 */

public class CheckUsertype extends AppCompatActivity {
    private static final String TAG = "ViewDatabase";
    private Firebase mref;
    //add Firebase Database stuff
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    private  String userID;
    TextView t1,t2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_check_usertype);
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
        t1=findViewById(R.id.u_name);
        t2=findViewById(R.id.u_email);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("users");
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String value=dataSnapshot.getValue().toString();
                    Map<String,String> usertype=(Map) dataSnapshot.getValue();
                    String usert=usertype.get("usertype");
                    Toast.makeText(CheckUsertype.this, usert, Toast.LENGTH_SHORT).show();
                    if(usert.equals("student"))
                    {
                        //   Intent i= new Intent(CheckUsertype.this, home_student.class);
                        // startActivity(i);
                        //finish();
                    }
                    else if(usert.equals("faculty"))
                    {
                        Intent i=new Intent(CheckUsertype.this,home_fac.class);
                        startActivity(i);
                        finish();
                    }
                    else if(usert.equals("admin"))
                    {
                        Intent i=new Intent(CheckUsertype.this, admin_home.class);
                        startActivity(i);
                        finish();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CheckUsertype.this,"error found",Toast.LENGTH_LONG).show();

            }
        });
    }
}