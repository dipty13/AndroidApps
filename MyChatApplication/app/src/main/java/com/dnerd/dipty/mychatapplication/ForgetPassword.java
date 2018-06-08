package com.dnerd.dipty.mychatapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    private EditText mEtEmail;
    private Button mBtnReset;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mEtEmail = findViewById(R.id.forgetPasswordEditText);
        mBtnReset = findViewById(R.id.forgetPasswordButton);

        mAuth = FirebaseAuth.getInstance();

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEtEmail.getText().toString().trim();

                if(email.equals(""))
                {
                    Toast.makeText(ForgetPassword.this, R.string.toastFrogetPassword, Toast.LENGTH_SHORT).show();

                }else{
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgetPassword.this, R.string.toastFrogetPasswordEmailSent, Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgetPassword.this,Login.class));
                            }else{
                                Toast.makeText(ForgetPassword.this, R.string.toastFrogetPasswordError, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
