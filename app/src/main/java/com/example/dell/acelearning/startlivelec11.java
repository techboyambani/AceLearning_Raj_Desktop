package com.example.dell.acelearning;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wowza.gocoder.sdk.api.WowzaGoCoder;
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcast;
import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcastConfig;
import com.wowza.gocoder.sdk.api.configuration.WOWZMediaConfig;
import com.wowza.gocoder.sdk.api.devices.WOWZAudioDevice;
import com.wowza.gocoder.sdk.api.devices.WOWZCameraView;
import com.wowza.gocoder.sdk.api.errors.WOWZError;
import com.wowza.gocoder.sdk.api.errors.WOWZStreamingError;
import com.wowza.gocoder.sdk.api.status.WOWZState;
import com.wowza.gocoder.sdk.api.status.WOWZStatus;
import com.wowza.gocoder.sdk.api.status.WOWZStatusCallback;


        import android.Manifest;
                import android.content.Context;
                import android.content.pm.PackageManager;
                import android.os.Build;
                import android.os.Handler;
                import android.os.Looper;
                import android.support.v4.app.ActivityCompat;
                import android.support.v7.app.AppCompatActivity;
                import android.os.Bundle;
                import android.view.View;
                import android.widget.Button;
                import android.widget.Toast;

                import com.wowza.gocoder.sdk.api.WowzaGoCoder;
                import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcast;
                import com.wowza.gocoder.sdk.api.broadcast.WOWZBroadcastConfig;
                import com.wowza.gocoder.sdk.api.configuration.WOWZMediaConfig;
                import com.wowza.gocoder.sdk.api.devices.WOWZAudioDevice;
                import com.wowza.gocoder.sdk.api.devices.WOWZCameraView;
                import com.wowza.gocoder.sdk.api.errors.WOWZError;
                import com.wowza.gocoder.sdk.api.errors.WOWZStreamingError;
                import com.wowza.gocoder.sdk.api.status.WOWZState;
                import com.wowza.gocoder.sdk.api.status.WOWZStatus;
                import com.wowza.gocoder.sdk.api.status.WOWZStatusCallback;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
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


public class startlivelec11 extends AppCompatActivity
        implements WOWZStatusCallback, View.OnClickListener {



    private DatabaseReference UsersRef,GroupMessageKeyReference, Groupnamerefernce;
    private EditText userMessageInput;
    private ScrollView mScrollView;
    private TextView displayTextMessages;
    private ImageButton SendMessageButton;

    private FirebaseAuth mAuth;
    private String currentuserid,currentusername, currentdate, currenttime;



    // The top-level GoCoder API interface
    private WowzaGoCoder goCoder;

    // The GoCoder SDK camera view
    private WOWZCameraView goCoderCameraView;

    // The GoCoder SDK audio device
    private WOWZAudioDevice goCoderAudioDevice;

    // The GoCoder SDK broadcaster
    private WOWZBroadcast goCoderBroadcaster;

    // The broadcast configuration settings
    private WOWZBroadcastConfig goCoderBroadcastConfig;

    // Properties needed for Android 6+ permissions handling
    private static final int PERMISSIONS_REQUEST_CODE = 0x1;
    private boolean mPermissionsGranted = true;
    private String[] mRequiredPermissions = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startlivelec11);
        mAuth=FirebaseAuth.getInstance();
        currentuserid=mAuth.getCurrentUser().getUid();
        UsersRef= FirebaseDatabase.getInstance().getReference().child("users");
        Groupnamerefernce=FirebaseDatabase.getInstance().getReference().child("livedbt");


        InitializeFields();
        GetUserInfo();
        SendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savemessageinfotodatabase();
                userMessageInput.setText("");
            }
        });
        // Initialize the GoCoder SDK
        goCoder = WowzaGoCoder.init(getApplicationContext(), "GOSK-1246-010C-0844-540E-40B3");

        if (goCoder == null) {
            // If initialization failed, retrieve the last error and display it
            WOWZError goCoderInitError = WowzaGoCoder.getLastError();
            Toast.makeText(this,
                    "GoCoder SDK error: " + goCoderInitError.getErrorDescription(),
                    Toast.LENGTH_LONG).show();
            return;
        }

        goCoderCameraView = (WOWZCameraView) findViewById(R.id.camera_preview);
        goCoderAudioDevice = new WOWZAudioDevice();
        goCoderBroadcaster = new WOWZBroadcast();
        goCoderBroadcastConfig = new WOWZBroadcastConfig(WOWZMediaConfig.FRAME_SIZE_1920x1080);

        goCoderBroadcastConfig.setHostAddress("182239.entrypoint.cloud.wowza.com");
        goCoderBroadcastConfig.setPortNumber(1935);
        goCoderBroadcastConfig.setApplicationName("app-9f94");
        goCoderBroadcastConfig.setStreamName("885ce897");
        //goCoderBroadcastConfig.setUsername("Ritu");
        //goCoderBroadcastConfig.setPassword("projectg7");



        // Designate the camera preview as the video broadcaster
        goCoderBroadcastConfig.setVideoBroadcaster(goCoderCameraView);


        goCoderBroadcastConfig.setAudioBroadcaster(goCoderAudioDevice);

        // Associate the onClick() method as the callback for the broadcast button's click event
        Button broadcastButton = (Button) findViewById(R.id.broadcast_button);
        broadcastButton.setOnClickListener(this);
    }

    //
    // Called when an activity is brought to the foreground
    //
    @Override
    protected void onResume() {
        super.onResume();

        // If running on Android 6 (Marshmallow) or above, check to see if the necessary permissions
        // have been granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mPermissionsGranted = hasPermissions(this, mRequiredPermissions);
            if (!mPermissionsGranted)
                ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE);
        } else
            mPermissionsGranted = true;

        // Start the camera preview display
        if (mPermissionsGranted && goCoderCameraView != null) {
            if (goCoderCameraView.isPreviewPaused())
                goCoderCameraView.onResume();
            else
                goCoderCameraView.startPreview();
        }

    }

    //
    // Callback invoked in response to a call to ActivityCompat.requestPermissions() to interpret
    // the results of the permissions request
    //
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mPermissionsGranted = true;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE: {
                // Check the result of each permission granted
                for(int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        mPermissionsGranted = false;
                    }
                }
            }
        }
    }

    //
    // Utility method to check the status of a permissions request for an array of permission identifiers
    //
    private static boolean hasPermissions(Context context, String[] permissions) {
        for(String permission : permissions)
            if (context.checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
                return false;

        return true;
    }

    //
    // The callback invoked when the broadcast button is tapped
    //
    @Override
    public void onClick(View view) {
        // return if the user hasn't granted the app the necessary permissions
        if (!mPermissionsGranted) return;

        // Ensure the minimum set of configuration settings have been specified necessary to
        // initiate a broadcast streaming session
        WOWZStreamingError configValidationError = goCoderBroadcastConfig.validateForBroadcast();

        if (configValidationError != null) {
            Toast.makeText(this, configValidationError.getErrorDescription(), Toast.LENGTH_LONG).show();
        } else if (goCoderBroadcaster.getStatus().isRunning()) {
            // Stop the broadcast that is currently running
            goCoderBroadcaster.endBroadcast(this);
        } else {
            // Start streaming
            goCoderBroadcaster.startBroadcast(goCoderBroadcastConfig, this);
        }
    }

    //
    // The callback invoked upon changes to the state of the steaming broadcast
    //
    @Override
    public void onWZStatus(final WOWZStatus goCoderStatus) {
        // A successful status transition has been reported by the GoCoder SDK
        final StringBuffer statusMessage = new StringBuffer("Broadcast status: ");

        switch (goCoderStatus.getState()) {
            case WOWZState.STARTING:
                statusMessage.append("Broadcast initialization");
                break;

            case WOWZState.READY:
                statusMessage.append("Ready to begin streaming");
                break;

            case WOWZState.RUNNING:
                statusMessage.append("Streaming is active");
                break;

            case WOWZState.STOPPING:
                statusMessage.append("Broadcast shutting down");
                break;

            case WOWZState.IDLE:
                statusMessage.append("The broadcast is stopped");
                break;

            default:
                return;
        }

        // Display the status message using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(startlivelec11.this, statusMessage, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onWZError(final WOWZStatus wowzStatus) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(startlivelec11.this,
                        "Streaming error: " + wowzStatus.getLastError().getErrorDescription(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    //
    // The callback invoked when an error occurs during a broadcast
    //
    // @Override
    public void onWOWZError(final WOWZStatus goCoderStatus) {
        // If an error is reported by the GoCoder SDK, display a message
        // containing the error details using the U/I thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(startlivelec11.this,
                        "Streaming error: " + goCoderStatus.getLastError().getErrorDescription(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    //
    // Enable Android's immersive, sticky full-screen mode
    //
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        View rootView = getWindow().getDecorView().findViewById(android.R.id.content);
        if (rootView != null)
            rootView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(startlivelec11.this,home_fac.class);
        startActivity(i);
    }
    private void savemessageinfotodatabase() {
        String message=userMessageInput.getText().toString();
        String messagKEY=Groupnamerefernce.push().getKey();
        if(TextUtils.isEmpty(message))
        {
            Toast.makeText(startlivelec11.this,"Please write message first ...",Toast.LENGTH_SHORT);
        }
        else
        {
            Calendar ccalForDate= Calendar.getInstance();
            SimpleDateFormat currentDateFormat =new SimpleDateFormat("dd/MM/yyyy");
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

        SendMessageButton=findViewById(R.id.send_message_button);
        userMessageInput=findViewById(R.id.input_group_message);
        displayTextMessages=findViewById(R.id.group_chat_text_display);
        mScrollView=findViewById(R.id.my_scroll_view);
    }

}

