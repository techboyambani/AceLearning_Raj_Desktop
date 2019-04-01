package com.example.dell.acelearning;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class stud_livelecture extends AppCompatActivity {

    private WebView webView;
    private Toolbar mToolbar;
    private DatabaseReference UsersRef,GroupMessageKeyReference, Groupnamerefernce;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;
    private ImageButton SendMessageButton;

    private FirebaseAuth mAuth;
    private String currentuserid,currentusername, currentdate, currenttime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stud_livelecture);
        mAuth=FirebaseAuth.getInstance();
        currentuserid=mAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("users");
        Groupnamerefernce=FirebaseDatabase.getInstance().getReference().child("livedbt");
        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://player.cloud.wowza.com/hosted/wkvxjwby/player.html");
        InitializeFields();
        GetUserInfo();
        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { 
                savemessageinfotodatabase();
                userMessageInput.setText("");
            }
        });
    }

    private void savemessageinfotodatabase() {
        String message=userMessageInput.getText().toString();
        String messagKEY=Groupnamerefernce.push().getKey();
        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(stud_livelecture.this,"Please write message first ...",Toast.LENGTH_SHORT);
        }
        else
        {
            Calendar ccalForDate= Calendar.getInstance();
            SimpleDateFormat currentDateFormat =new SimpleDateFormat("dd mm yyyy");
            currentdate= currentDateFormat.format(ccalForDate.getTime());



            Calendar ccalForTime= Calendar.getInstance();
            SimpleDateFormat currentTimeFormat =new SimpleDateFormat("hh:mm a");
            currenttime= currentTimeFormat.format(ccalForTime.getTime());

            HashMap<String ,Object > groupMessageKey=new HashMap<>();
            Groupnamerefernce.updateChildren(groupMessageKey);
            GroupMessageKeyReference=Groupnamerefernce.child(messagKEY);
            HashMap<String,Object> messageInfoMap= new HashMap<>();
            messageInfoMap.put("name",currentusername);
            messageInfoMap.put("message",message);
            messageInfoMap.put("date",currentdate);
            messageInfoMap.put("time",currenttime);
            GroupMessageKeyReference.updateChildren(messageInfoMap);

        }

    }

    private void GetUserInfo() {
        UsersRef.child(currentuserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentusername= dataSnapshot.child("name").getValue().toString();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    protected void onStart()
    {
        super.onStart();
        Groupnamerefernce.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists())
                {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();
        while(iterator.hasNext())
        {
            String chatDate=(String)((DataSnapshot)iterator.next()).getValue();
            String chatMessage=(String)((DataSnapshot)iterator.next()).getValue();
            String chatName=(String)((DataSnapshot)iterator.next()).getValue();
            String chatTime=(String)((DataSnapshot)iterator.next()).getValue();
            displayTextMessages.append(chatName+":\n" + chatMessage +"\n"+ chatTime +"    "+
            chatDate + "\n\n\n");
        }
    }

    private void InitializeFields()
    {
        //mToolbar =findViewById(R.id.group_chat_bar_layout);
        //setSupportActionBar(mToolbar);
        //getSupportActionBar().setTitle("Queries");

        SendMessageButton=findViewById(R.id.send_message_button);
        userMessageInput=findViewById(R.id.input_group_message);
        displayTextMessages=findViewById(R.id.group_chat_text_display);
        mScrollView=findViewById(R.id.my_scroll_view);
    }

}
