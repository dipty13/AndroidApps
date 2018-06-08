package com.dnerd.dipty.mychatapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.dnerd.dipty.mychatapplication.R.layout.*;

public class UsersActivity extends AppCompatActivity {
    private RecyclerView mAllUsersList;
    private DatabaseReference mAllUserReference;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_users);

        mAllUsersList = findViewById(R.id.all_users_list);
        mAllUsersList.setHasFixedSize(true);
        mAllUsersList.setLayoutManager(new LinearLayoutManager(this));
        mAllUsersList.setAdapter(adapter);

        mAllUserReference = FirebaseDatabase.getInstance().getReference().child("Users");




    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<AllUsers,AllUsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>(
                AllUsers.class,
                R.layout.all_users_display,
                AllUsersViewHolder.class,
                mAllUserReference

        ) {
            @Override
            protected void populateViewHolder(AllUsersViewHolder viewHolder, AllUsers model, int position) {

                viewHolder.setName(model.getUser_name());
                viewHolder.setStatus(model.getUser_status());
                viewHolder.setImage(model.getUser_image(), getApplicationContext());

                final String user_id = getRef(position).getKey();

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent profileIntent = new Intent(UsersActivity.this,Profile.class);
                        profileIntent.putExtra("user_id",user_id);
                        startActivity(profileIntent);
                    }
                });
            }
        };

        mAllUsersList.setAdapter(firebaseRecyclerAdapter);


    }

    public  static  class AllUsersViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public AllUsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setName(String user_name) {

            TextView usernameView =  mView.findViewById(R.id.all_users_username);
            usernameView.setText(user_name);
        }

        public void setStatus(String user_status) {
            TextView userstatusView =  mView.findViewById(R.id.all_user_status);
            userstatusView.setText(user_status);
        }

        public void setUsersImage(String user_image) {

        }

        public void setImage(final String user_thumb_image, final Context applicationContext) {
            final CircleImageView userThumbImage =  mView.findViewById(R.id.all_users_image);
            //Picasso.with(applicationContext).load(user_thumb_image).placeholder(R.drawable.default_profile_image).into(userThumbImage);

            Picasso.with(applicationContext).load(user_thumb_image).placeholder(R.drawable.default_profile_image).networkPolicy(NetworkPolicy.OFFLINE).into(userThumbImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(applicationContext).load(user_thumb_image).placeholder(R.drawable.default_profile_image).into(userThumbImage);
                }
            });
        }
    }
}
