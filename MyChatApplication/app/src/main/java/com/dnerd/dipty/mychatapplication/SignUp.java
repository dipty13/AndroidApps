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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class SignUp extends AppCompatActivity {
    private static final String TAG = "SignUp";
    private EditText mName,mEmail,mPassword;
    private Button mSignUp;
    private FirebaseAuth mAuth;
    private ProgressDialog mRegProgress;
    private FirebaseDatabase mFirebaseDatabse;
    private DatabaseReference mStoreUserDefaultDataReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        mName = findViewById(R.id.signup_name_edit_text);
        mEmail = findViewById(R.id.signup_email_edit_text);
        mPassword = findViewById(R.id.signup_password_edit_text);
        mSignUp = findViewById(R.id.signup_signup_button);

        mRegProgress = new ProgressDialog(this);

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {
        final String name = mName.getText().toString();
        String password = mPassword.getText().toString();
        String email = mEmail.getText().toString();

        //username = username.trim();
        password = password.trim();
        email = email.trim();
        int cntSpecialCaracter = 0,cntCapital = 0,cntSmall = 0,cntNumber = 0;
        for(int i=0;i<password.length();i++)
        {
            if(password.charAt(i) == '@' || password.charAt(i) == '!' ||
                    password.charAt(i) == '$' || password.charAt(i) == '#'
                    || password.charAt(i) == '%' || password.charAt(i) == '&'
                    || password.charAt(i) == '^' || password.charAt(i) == '~'
                    || password.charAt(i) == '*' || password.charAt(i) == '/'
                    || password.charAt(i) == '(' || password.charAt(i) == ')'
                    || password.charAt(i) == '=' || password.charAt(i) == '-'
                    || password.charAt(i) == '_' || password.charAt(i) == '+'
                    || password.charAt(i) == ':' || password.charAt(i) == ';'
                    || password.charAt(i) == '{' || password.charAt(i) == '}'
                    || password.charAt(i) == '[' || password.charAt(i) == ']'
                    || password.charAt(i) == '|' || password.charAt(i) == '\\'
                    || password.charAt(i) == ',' || password.charAt(i) == '<'
                    || password.charAt(i) == '>' || password.charAt(i) == '?'
                    || password.charAt(i) == '/' || password.charAt(i) == '`')
            {
                cntSpecialCaracter++;
            }
            else if(password.charAt(i)>='A'&&password.charAt(i)<='Z')
            {
                cntCapital++;
            }
            else if(password.charAt(i)>='a'&&password.charAt(i)<='z')
            {
                cntSmall++;
            }
            else if(password.charAt(i)>='0'&&password.charAt(i)<='9')
            {
                cntNumber++;
            }
        }

        /*If any of the fields are empty then alert dialog will pop up
          and after clicking ok cursor will focus on that field
         */
           if(name.isEmpty()&&password.isEmpty()&&email.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
            builder.setMessage(R.string.sign_up_error_message)
                    .setTitle(R.string.sign_up_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            return;
        }
        else if(name.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
            builder.setMessage(R.string.sign_up_error_message_name)
                    .setTitle(R.string.sign_up_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            mName.requestFocus();
            return;
        }
        else if(email.isEmpty())
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
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
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
            builder.setMessage(R.string.sign_up_error_message_password)
                    .setTitle(R.string.sign_up_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            mPassword.requestFocus();
            return;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
            builder.setMessage(R.string.validEmailError)
                    .setTitle(R.string.sign_up_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            mEmail.requestFocus();
            return;

        }
        else if (password.length() < 8) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
            builder.setMessage(R.string.validPasswordError)
                    .setTitle(R.string.sign_up_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            mPassword.requestFocus();
            return;

        }
        else if (cntCapital == 0|| cntSmall == 0 || cntNumber == 0 || cntSpecialCaracter == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
            builder.setMessage(R.string.validPasswordError2)
                    .setTitle(R.string.sign_up_error_title)
                    .setPositiveButton(android.R.string.ok,null);
            AlertDialog dialog = builder.create();
            dialog.show();
            mPassword.requestFocus();
            return;

        }
        else {
            mRegProgress.setTitle("Registering User");
            mRegProgress.setMessage("Please wait while we create your account !");
            mRegProgress.setCanceledOnTouchOutside(false);
            mRegProgress.show();
            //crating new user
            final String finalEmail = email;
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    mRegProgress.hide();
                    //if not successful
                    if (!task.isSuccessful()) {
                        if(task.getException() instanceof FirebaseAuthUserCollisionException) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                            builder.setMessage(R.string.firebase_signup_alreadyReagistered)
                                    .setTitle(R.string.sign_up_error_title)
                                    .setPositiveButton(android.R.string.ok, null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                            builder.setMessage(R.string.error_signingup)
                                    .setTitle(R.string.sign_up_error_title)
                                    .setPositiveButton(android.R.string.ok,null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            mPassword.requestFocus();
                            return;
                        }
                    }
                    else{
                        FirebaseUser cU =  FirebaseAuth.getInstance().getCurrentUser();

                        final String current_user_id = cU.getUid();

                        String deviceToken = FirebaseInstanceId.getInstance().getToken();
                        mStoreUserDefaultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_user_id);

                        mStoreUserDefaultDataReference.child("user_name").setValue(name);
                        mStoreUserDefaultDataReference.child("user_email").setValue(finalEmail);
                        mStoreUserDefaultDataReference.child("user_status").setValue("status");
                        mStoreUserDefaultDataReference.child("device_token").setValue(deviceToken);
                        mStoreUserDefaultDataReference.child("online").setValue("not online");
                        mStoreUserDefaultDataReference.child("user_image").setValue("default_profile_image").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(!task.isSuccessful())
                                {
                                    String e = "signup fail "+ task.getException();
                                    Log.e(TAG, "onComplete: "+ e );
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                    builder.setMessage(R.string.firebase_signup_error)
                                            .setTitle(R.string.sign_up_error_title)
                                            .setPositiveButton(android.R.string.ok,null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();
                                }
                                else{

                                    sendVerificationEmail();
                                    Toast.makeText(SignUp.this,R.string.emailSent,Toast.LENGTH_LONG).show();
                                    mAuth.signOut();
                                    Intent intent = new Intent(SignUp.this,Login.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            }
                        });

                    }
                }


            });
        }
    }

    private void sendVerificationEmail() {

        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {

                    }
                    else{
                        Toast.makeText(SignUp.this,R.string.emailSendFailed,Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
