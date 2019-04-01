package com.example.dell.acelearning;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
public class signup extends AppCompatActivity {
    private Button student, faculty, btnSignIn, btnSignUp, btnResetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        student=findViewById(R.id.student);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        faculty = (Button) findViewById(R.id.faculty);
        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // to add student home
                startActivity(new Intent(signup.this, signup.class));
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(signup.this, login.class);
                startActivity(i);
                finish();
            }
        });
        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i=new Intent(signup.this, signup_fac.class);
                    startActivity(i);
                    finish();
            }
        });
    }


}
