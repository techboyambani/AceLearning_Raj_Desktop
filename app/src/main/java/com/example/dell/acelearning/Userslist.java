package com.example.dell.acelearning;


import android.app.Activity;
import android.content.Context;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.CardView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
public class Userslist extends ArrayAdapter<UserInformation> {
    ArrayList<UserInformation> list;
    Activity context;
    public Userslist(Activity context, ArrayList<UserInformation> list){
        super(context,R.layout.user_layout , list);
        this.context = context;
        this.list = list;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.user_layout, null,true);
        TextView fac_name =rowView.findViewById(R.id.username);
        TextView fac_email = rowView.findViewById(R.id.user_email);
        TextView fac_subject =  rowView.findViewById(R.id.user_subject);
        Button delete=rowView.findViewById(R.id.delete);
        Button accept= rowView.findViewById(R.id.accept);
        Button view_resume=rowView.findViewById(R.id.resume);
        final FirebaseAuth auth=FirebaseAuth.getInstance();
        final UserInformation model = list.get(position);
       final DatabaseReference temp=FirebaseDatabase.getInstance().getReference().child("temporary_faculty");
        fac_name.setText(model.getName());
        fac_email.setText(model.getEmail());
        fac_subject.setText(model.getF_subjects());
        accept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), model.getEmail(), Toast.LENGTH_SHORT).show();
                auth.createUserWithEmailAndPassword(model.getEmail(),model.getPassword()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = mFirebaseDatabase.getReference();
                        FirebaseUser user = auth.getCurrentUser();
                        String userID = user.getUid();
                        UserInformation userInformation = new UserInformation(model.getName() ,model.getEmail(),"faculty",model.getPhn(),model.getPassword(),model.getF_subjects(),model.getUrl());
                        myRef.child("users").child(userID).setValue(userInformation);
                        temp.child(model.getPhn()).removeValue();
                        myRef.child("subjects_faculty").child(model.getF_subjects()).setValue(userID);
                        Intent i= new Intent(getContext(),admin_home.class);
                        context.startActivity(i);
                        context.finish();
                    }
                });
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
                myRef.child("subject").child(model.getF_subjects()).setValue(model.getF_subjects());
                Toast.makeText(getContext(), model.getF_subjects(), Toast.LENGTH_SHORT).show();
                temp.child(model.getPhn()).removeValue();
                Intent i= new Intent(getContext(),admin_home.class);
                context.startActivity(i);
                context.finish();
            }
        });
        view_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                url=model.getUrl();
                Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                if (!url.startsWith("http://") && !url.startsWith("https://"))
                    url = "http://" + url;
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(browserIntent);
            }
        });
        return rowView;
    }
}











































/* Context mCtx;
  //  Button delete , accept;
  String email;
     String password;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference myRef;
    Button delete, approve;
    int flag=0;
    List<UserInformation> productList;

    public Userslist(Context mCtx, List<UserInformation> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.user_layout,
                parent, false);
        ProductViewHolder productViewHolder = new ProductViewHolder(view);
        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(final ProductViewHolder holder, int position) {
        final UserInformation product = productList.get(position);

        final String[] userid = new String[1];
        final String password=product.getPassword();
        email=product.getEmail();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseDatabase.getReference().child("users").addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
        FirebaseUser uid;
            @Override
            public void onDataChange (@NonNull DataSnapshot dataSnapshot){
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot childnodes : snapshot.getChildren()) {
                        String value = childnodes.getValue().toString();
                        Map<String, String> usertype = (Map) snapshot.getValue();
                        userid[0] =snapshot.getValue().toString();
                        String usert = usertype.get("usertype");
                        if ((usertype.get("email")).equals(email) && usert.equals("faculty")) {
                            holder.textviewPrice.setText(String.valueOf(usertype.get("phn")));
                            holder.textViewRating.setText(String.valueOf(usertype.get("f_subjects")));
                            holder.textViewTitle.setText(product.getName());
                            holder.textViewShortDesc.setText(product.getEmail());
                            delete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Executor) Userslist.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                            } else{


                                                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                                                final FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                                                currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){}
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });



                                            }
                                        }
                                    });

                                }
                            });
                        }
                    }
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError){
            }
        });
//        holder.imageView.setImageDrawable(mCtx.getResources().getDrawable(product.getImage(), null));
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder {
        private FirebaseDatabase mFirebaseDatabase;
        private FirebaseAuth mAuth;
        private FirebaseAuth.AuthStateListener mAuthListener;
        private DatabaseReference myRef;


        TextView textViewTitle, textViewShortDesc, textViewRating, textviewPrice;
        public ProductViewHolder(final View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewShortDesc = itemView.findViewById(R.id.textViewShortDesc);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textviewPrice = itemView.findViewById(R.id.textViewPrice);
            approve=itemView.findViewById(R.id.accept);
            delete=itemView.findViewById(R.id.delete);

        }

    }
*/