package com.dnerd.dipty.mychatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.theartofdev.edmodo.cropper.CropImageActivity;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {
    private static final int gallery_pic = 101;
    private static final String TAG = "SettingsActivity" ;
    private CircleImageView mProfileImage;
    private ImageView mAddProfilePic,mEditStatus;
    private TextView mName, mStatus;
    private EditText mEditTextStatus;
    private Uri uriProfileImage;
    private ProgressDialog mProgressDialog;
    private String mProfileImageUrl;
    private FirebaseAuth mAuth;
    private DatabaseReference mGetUsersDataReference;
    private StorageReference profileImageReference;
    private Button mUpdateStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Toolbar toolbar =  findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        mProfileImage = findViewById(R.id.settings_profileImage);
        mName = findViewById(R.id.settings_username_text_view);
        mStatus = findViewById(R.id.settings_status_text_view);
        mAddProfilePic =findViewById(R.id.addProfilePic);
        mEditStatus = findViewById(R.id.edit_status);
        mEditTextStatus = findViewById(R.id.edit_text_status);
        mUpdateStatus = findViewById(R.id.settings_update_status);

        mAuth = FirebaseAuth.getInstance();
        String onlineUserId = mAuth.getCurrentUser().getUid();
        mGetUsersDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(onlineUserId);
        mGetUsersDataReference.keepSynced(true);

        profileImageReference = FirebaseStorage.getInstance().getReference();
        //offline capablities enabled
        mGetUsersDataReference.keepSynced(true);

        if(toolbar!=null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        mEditTextStatus.setVisibility(View.INVISIBLE);
        mUpdateStatus.setVisibility(View.INVISIBLE);

        mGetUsersDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("user_name").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                final String image = dataSnapshot.child("user_image").getValue().toString();

                mName.setText(name);
                mStatus.setText(status);


                toolbar.setTitle(name);
                //if(!phone.equals("+880***********")){
                //mPhone.setText(phone);
                //}
                if(!image.equals("default_profile_image"))
                {
                    //Picasso.with(Profile.this).load(image).placeholder(R.drawable.default_profile_image).into(mProfileImage);
                    Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_profile_image).networkPolicy(NetworkPolicy.OFFLINE).into(mProfileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_profile_image).into(mProfileImage);
                        }
                    });

                   /* Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_profile_image).networkPolicy(NetworkPolicy.OFFLINE).into(mNavPic, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.default_profile_image).into(mNavPic);
                        }
                    });*/
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            mAddProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

/*
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setMinCropResultSize(512, 512)
                                .setAspectRatio(1, 1)
                                .start(SettingsActivity.this);
*/


                    showImageChooser();
                    saveUserInfo();
                    // loadUserInfo();
                }
            });


        mEditStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTextStatus.setVisibility(View.VISIBLE);
                mUpdateStatus.setVisibility(View.VISIBLE);
                String status = mStatus.getText().toString();

            }
        });

        mUpdateStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = mEditTextStatus.getText().toString();
                mEditTextStatus.setVisibility(View.INVISIBLE);
                mUpdateStatus.setVisibility(View.INVISIBLE);
                mGetUsersDataReference.child("user_status").setValue(status);
            }
        });

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) //requestCode == gallery_pic && resultCode == RESULT_OK && data !=null && data.getData() != null)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //uriProfileImage = data.getData();
            uriProfileImage=result.getUri();
           *//* CropImage.activity(uriProfileImage)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(Profile.this);*//*

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
                mProfileImage.setImageBitmap(bitmap);


                uploadImageToFirebaseStorgae();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }*/
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if(requestCode == gallery_pic && resultCode == RESULT_OK && data !=null && data.getData() != null)
      {
          uriProfileImage = data.getData();
           /* CropImage.activity(uriProfileImage)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(SettingsActivity.this);*/

          try {
              Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfileImage);
              mProfileImage.setImageBitmap(bitmap);


              uploadImageToFirebaseStorgae();
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
  }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user!=null) {
            if (user.getPhotoUrl() != null) {
                //String photoUrl = user.getPhotoUrl().toString();
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(mProfileImage);
            }

            String displayName = user.getDisplayName();

        }
    }

    private void saveUserInfo() {
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null&& mProfileImageUrl!=null)
        {
            UserProfileChangeRequest profileChange = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(mProfileImageUrl)).build();
            user.updateProfile(profileChange).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(SettingsActivity.this,R.string.profileUpdated,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
    private void uploadImageToFirebaseStorgae() {
        StorageReference profileInageRef = FirebaseStorage.getInstance().getReference("user_image/"+System.currentTimeMillis()+".jpg");

        if(uriProfileImage != null)
        {
            mProgressDialog = new ProgressDialog(SettingsActivity.this);
            mProgressDialog.setTitle("Uploading Image...");
            mProgressDialog.setMessage("Please wait while we upload and process the image.");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            profileInageRef.putFile(uriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();

                    mProfileImageUrl = taskSnapshot.getDownloadUrl().toString();
                    mGetUsersDataReference.child("user_image").setValue(mProfileImageUrl);


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    mProgressDialog.dismiss();
                    Toast.makeText(SettingsActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    private void showImageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select image"),gallery_pic);
    }
}
