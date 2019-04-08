package com.exploretalent.hireme;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Password_Activity extends Activity {

    EditText etdEmailVerify;
    Button btnReset;
    FirebaseAuth firebaseAuth;
    Bundle bundle;
    String bundleString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot__password_);

        etdEmailVerify = (EditText)findViewById(R.id.emailVerifyID);
        btnReset = (Button) findViewById(R.id.btnResetID);

        firebaseAuth = FirebaseAuth.getInstance();
        bundle = getIntent().getExtras();
        bundleString = bundle.getString("login_to_forgot_password");

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailString = etdEmailVerify.getText().toString();
                if(emailString.equals("")){
                    Toast.makeText(Forgot_Password_Activity.this, "Please Enter your email!", Toast.LENGTH_SHORT).show();
                    etdEmailVerify.requestFocus();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(emailString).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Forgot_Password_Activity.this, "Password Reset Email Sent!",
                                        Toast.LENGTH_SHORT).show();
                               if(bundleString.equals("Customer")) {
                                   startActivity(new Intent(Forgot_Password_Activity.this,
                                           Login_Customer_Activity.class));
                                   finish();
                               }
                               else {
                                   startActivity(new Intent(Forgot_Password_Activity.this,
                                           Service_Provider_Login_Activity.class));
                                   finish();
                               }
                            }else{
                                Toast.makeText(Forgot_Password_Activity.this, "Error in sending password reset code!", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
                }

            }
        });
    }
}
