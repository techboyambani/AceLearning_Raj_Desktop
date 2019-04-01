package com.example.dell.acelearning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class home_fac extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NavigationView navigationView;
    Button upd , test, doubt, stud_score;
    Toolbar toolbar=null;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    public TextView t1,t2;

    private DatabaseReference myRef;
    private String userID;
   // String u_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.nav_header_home_fac);
        //t1=findViewById(R.id.u_name);
       // t2=findViewById(R.id.u_email);
        setContentView(R.layout.activity_home_fac);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        upd=findViewById(R.id.upd);
        doubt=findViewById(R.id.doubts);
        stud_score=findViewById(R.id.stud_score);
        test=findViewById(R.id.tests);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("users");
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
//        myRef.child(userID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                //String value=dataSnapshot.getValue().toString();
//                Map<String, String> usertype = (Map) dataSnapshot.getValue();
//                String u_name = usertype.get("name");
//                String u_email = usertype.get("email");
//                //Toast.makeText(home_fac.this, u_name, Toast.LENGTH_SHORT).show();
//                t1.setText(u_name);
//                t2.setText(u_email);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                //Toast.makeText(setfacuser.this, "error found", Toast.LENGTH_LONG).show();
//
//            }
//        });
        /* test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(home_fac.this,generate_test.class);
                startActivity(i);
                finish();
            }
        }); */
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(home_fac.this,generate_test.class);
                startActivity(i);
                finish();
            }
        });
        doubt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(home_fac.this,solve_doubt.class);
                startActivity(i);
                finish();
            }
        });
        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(home_fac.this,upload_material.class);
                startActivity(i);
                finish();
            }
        });
        stud_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(home_fac.this,stud_score.class);
                startActivity(i);
                finish();
            }
        });
     //   start1();
        //   Intent i=new Intent(home_fac.this,setfacuser.class);
       // startActivity(i);
     /*
*/




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view =navigationView.inflateHeaderView(R.layout.nav_header_home_fac);
        t1=view.findViewById(R.id.u_name);
        t2=view.findViewById(R.id.u_email);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("users");
      //  final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        //  Toast.makeText(CheckUsertype.this,userID,Toast.LENGTH_LONG).show();
        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value=dataSnapshot.getValue().toString();
                Map<String, String> userinfo = (Map) dataSnapshot.getValue();
                String u_name = userinfo.get("name");
                String u_email = userinfo.get("email");
                //Toast.makeText(home_fac.this, u_name, Toast.LENGTH_SHORT).show();
                t1.setText(u_name);
                t2.setText(u_email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Toast.makeText(setfacuser.this, "error found", Toast.LENGTH_LONG).show();

            }
        });
        navigationView.setNavigationItemSelectedListener(this);

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);


      /*  mAuth = FirebaseAuth.getInstance();
        t1=findViewById(R.id.u_name);
        t2=findViewById(R.id.u_email);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("users");
        final FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        //  Toast.makeText(CheckUsertype.this,userID,Toast.LENGTH_LONG).show();
        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String value=dataSnapshot.getValue().toString();
                Map<String,String> usertype=(Map) dataSnapshot.getValue();
                u_name=usertype.get("name");
                String u_email=usertype.get("email");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(home_fac.this,"error found", Toast.LENGTH_LONG).show();

            }
        });   */





    }
    public void start1()
    {
        setContentView(R.layout.activity_home_fac);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_fac, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
         //   return true;
        //}

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.uploadques) {

            Intent i=new Intent(home_fac.this,uploadques.class);
            startActivity(i);
            finish();
            // Handle the camera action
        } else if (id == R.id.livelecture) {
            Intent i=new Intent(home_fac.this,livelecture.class);
            startActivity(i);
            finish();
            //break;

        } else if (id == R.id.changepassword) {
            Intent i=new Intent(home_fac.this,changepassword.class);
            startActivity(i);
            finish();

        } else if (id == R.id.logout) {
            Intent i=new Intent(home_fac.this,logout.class);
            startActivity(i);
            finish();
        }
        else if(id==R.id.home_fac)
        {
            Intent i=new Intent(home_fac.this,home_fac.class);
            startActivity(i);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
