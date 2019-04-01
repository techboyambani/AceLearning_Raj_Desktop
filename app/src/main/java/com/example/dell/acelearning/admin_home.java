package com.example.dell.acelearning;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class admin_home extends AppCompatActivity {
    ListView listView;
    int count;
    ArrayList<UserInformation> list;
    FirebaseDatabase mFirebaseDatabase;
    Button logout;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        list = new ArrayList<>();
        logout=findViewById(R.id.logout);
        listView = (ListView) findViewById(R.id.listviewid);
        mFirebaseDatabase.getReference().child("temporary_faculty").addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange (@NonNull com.google.firebase.database.DataSnapshot dataSnapshot){
                for (com.google.firebase.database.DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserInformation list_f= snapshot.getValue(UserInformation.class);
                        list.add(list_f);
                }
                Userslist facultydetails = new Userslist(admin_home.this,list);
                listView.setAdapter(facultydetails);
            }


            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth;
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                startActivity(new Intent(admin_home.this, login.class));
                finish();
            }
        });
    }


}




























/*
    RecyclerView recyclerView;
    Userslist adapter;
    List<UserInformation> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        productList = new ArrayList<>();
        DatabaseReference dbProducts = FirebaseDatabase.getInstance().getReference("users");
        dbProducts.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(com.google.firebase.database.DataSnapshot productSnapshot : dataSnapshot.getChildren()){
                        UserInformation p = productSnapshot.getValue(UserInformation.class);
                        productList.add(p);
                    }
                    adapter = new Userslist(admin_home.this, productList);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/
