package com.example.dell.acelearning;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class updquesobj extends AppCompatActivity {
    RadioGroup radioGroup;
    FirebaseUser user;
    FirebaseAuth auth;
    DatabaseReference mdbref;
    String userid;
    EditText ques, op1, op2, op3, op4;
    String selectedqtype;
    String selectedsubject;
    DatabaseReference db,db1;
    String qtype;
    Button back;
    String answer, userID;
    Spinner sp,sp1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updquesobj);

        ques=findViewById(R.id.ques);
        op1=findViewById(R.id.op1);
        op2=findViewById(R.id.op2);
        op3=findViewById(R.id.op3);
        op4=findViewById(R.id.op4);
        back=findViewById(R.id.back);
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userid=user.getUid();
        sp1=findViewById(R.id.sp_typeQ);
        db1=FirebaseDatabase.getInstance().getReference().child("qtype");
        db= FirebaseDatabase.getInstance().getReference().child("subject");
        DatabaseReference myRef;
        myRef = FirebaseDatabase.getInstance().getReference().child("users");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        myRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String,String> usertype=(Map) dataSnapshot.getValue();
                    selectedsubject=usertype.get("f_subjects");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        db1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> qtypes= new ArrayList<>();
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String qtype= (String) ds.getValue();
                    qtypes.add(qtype);
                }
                ArrayAdapter<String> q_type= new ArrayAdapter<String>(updquesobj.this,android.R.layout.simple_spinner_dropdown_item,qtypes);
                q_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp1.setAdapter(q_type);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedqtype = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(updquesobj.this, home_fac.class);
                startActivity(i);
                finish();
            }
        });




    }

    public void insertques(View view)
    {
        String answer1 ="";
        if(TextUtils.isEmpty(ques.getText().toString()))
        {
            Toast.makeText(updquesobj.this, "Please enter question", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(op1.getText().toString()))
        {
            Toast.makeText(updquesobj.this, "Please enter option", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(op2.getText().toString()))
        {
            Toast.makeText(updquesobj.this, "Please enter option", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(op3.getText().toString()))
        {
            Toast.makeText(updquesobj.this, "Please enter option", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(op4.getText().toString()))
        {
            Toast.makeText(updquesobj.this, "Please enter option", Toast.LENGTH_SHORT).show();
            return;
        }


        mdbref=FirebaseDatabase.getInstance().getReference().child("questions").child(selectedsubject).child("objectivetype");
        HashMap<String, String> dataMap= new HashMap<String,String>();
        dataMap.put("ques",ques.getText().toString());
        dataMap.put("op1",op1.getText().toString());
        dataMap.put("op2",op2.getText().toString());
        dataMap.put("op3",op3.getText().toString());
        dataMap.put("op4",op4.getText().toString());
        if(answer.equals("option 1"))
            answer1=op1.getText().toString();
        else if(answer.equals("option 2"))
            answer1=op2.getText().toString();
        else if(answer.equals("option 3"))
            answer1=op3.getText().toString();
        else if(answer.equals("option 4"))
            answer1=op4.getText().toString();
        else
            answer1="default";
        dataMap.put("answer",answer1);
        dataMap.put("type",selectedqtype);
        mdbref.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(updquesobj.this, "Question uploaded", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void selectanswer(View view) {
        radioGroup = findViewById(R.id.answer);
        RadioButton ans;
        int selectedId = radioGroup.getCheckedRadioButtonId();
        ans = findViewById(selectedId);
        answer = (String) ans.getText();
        Toast.makeText(updquesobj.this, answer, Toast.LENGTH_SHORT).show();
    }
}
