package com.example.dell.acelearning;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class setfacuser extends AppCompatActivity {
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar=null;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private TextView t1,t2;
    private DatabaseReference myRef;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_home_fac);
        mAuth = FirebaseAuth.getInstance();
        t1 = findViewById(R.id.u_name);
        t2 = findViewById(R.id.u_email);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("users");
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        //  Toast.makeText(CheckUsertype.this,userID,Toast.LENGTH_LONG).show();
        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value=dataSnapshot.getValue().toString();
                Map<String, String> usertype = (Map) dataSnapshot.getValue();
                String u_name = usertype.get("name");
                String u_email = usertype.get("email");
                //Toast.makeText(home_fac.this, usert, Toast.LENGTH_SHORT).show();
                t1.setText(u_name);
                t2.setText(u_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(setfacuser.this, "error found", Toast.LENGTH_LONG).show();

            }
        });
        //Intent i=new Intent(setfacuser.this,home_fac.class);
        //startActivity(i);
    }
}
