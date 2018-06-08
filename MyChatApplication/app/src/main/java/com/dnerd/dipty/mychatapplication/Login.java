package com.dnerd.dipty.mychatapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login";
    private TextView mcreateAccount,mForgetPassword;
    private Button mLogin;
    private EditText mEmail,mPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog mRegProgress;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mcreateAccount = findViewById(R.id.login_create_account_text_view);
        mEmail = findViewById(R.id.login_email_edit_text);
        mPassword = findViewById(R.id.login_password_edit_text);
        mForgetPassword = findViewById(R.id.login_forget_password_text_view);
        mLogin = findViewById(R.id.login_login_button);
        mAuth = FirebaseAuth.getInstance();
        mRegProgress = new ProgressDialog(this);
        mUserReference = FirebaseDatabase.getInstance().getReference().child("Users");

        FirebaseUser user = mAuth.getCurrentUser();

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        mForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgetPassword.class));
            }
        });

        mcreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin() {
        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();

        //username = username.trim();
        password = password.trim();
        email = email.trim();

        /*If any of the fields are empty then alert dialog will pop up
          and after clicking ok cursor will focus on that field
         */
        if(email.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setMessage(R.string.sign_up_error_message_email)
                    .setTitle(R.string.sign_up_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            mEmail.requestFocus();
            return;
        }
        else if(password.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setMessage(R.string.login_error_message_password)
                    .setTitle(R.string.sign_up_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            mPassword.requestFocus();
            return;
        }
        else if(password.isEmpty()&&email.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setMessage(R.string.login_error_message)
                    .setTitle(R.string.sign_up_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
            builder.setMessage(R.string.validEmailError)
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            mEmail.requestFocus();
            return;

        }
        else{
            mRegProgress.setTitle(getString(R.string.loginProgTitle));
            mRegProgress.setMessage(getString(R.string.progMsg));
            mRegProgress.setCanceledOnTouchOutside(false);
            mRegProgress.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mRegProgress.hide();
                    if(task.isSuccessful())
                    {
                        try{
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user.isEmailVerified()){
                                Log.d(TAG, "onComplete: user is varified");
                                String onlineUserId = mAuth.getCurrentUser().getUid();
                                String deviceToken = FirebaseInstanceId.getInstance().getToken();

                                mUserReference.child(onlineUserId).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Intent intent = new Intent(Login.this,Feed.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                });


                            }
                            else{

                                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                                builder.setMessage(R.string.emailNotverfied)
                                        .setTitle(R.string.emailNotVerifiedTitle)
                                        .setPositiveButton(android.R.string.ok,null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                                mAuth.signOut();

                            }

                        }catch (NullPointerException e){
                            Log.e(TAG, "onComplete: NullPointerException "+e.getMessage());
                        }

                    }
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                        builder.setMessage(R.string.firebase_login_error)
                                .setTitle(R.string.login_error_title)
                                .setPositiveButton(android.R.string.ok, null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            });
        }
    }
}
