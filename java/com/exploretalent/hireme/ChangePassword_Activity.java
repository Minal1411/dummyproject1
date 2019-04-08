package com.exploretalent.hireme;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;

import java.net.PasswordAuthentication;

public class ChangePassword_Activity extends Activity {

    EditText etdNewPassword,oldPassword,newPassword;
    Button update;
    String confPasswordString,oldpasswordString,newPasswordString,username;

    FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_);

        etdNewPassword = findViewById(R.id.etdChangePassID);
        update=findViewById(R.id.btnUpdateID);
        oldPassword=findViewById(R.id.etdoldPasswordID);
        newPassword=findViewById(R.id.etdNewChangePassID);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
    }

    private void changePassword() {

        confPasswordString = etdNewPassword.getText().toString();
        newPasswordString=newPassword.getText().toString();
        oldpasswordString=oldPassword.getText().toString();

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null){
            username = firebaseUser.getEmail();
        }
        if(newPasswordString.equals(confPasswordString)) {


            AuthCredential credential = EmailAuthProvider.getCredential(username, oldpasswordString);

            firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        firebaseUser.updatePassword(confPasswordString).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    Toast.makeText(ChangePassword_Activity.this, "Password Changed!", Toast.LENGTH_SHORT).show();
                                    finish();

                                } else {
                                    Toast.makeText(ChangePassword_Activity.this, "Failed To Change!", Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }

                }
            });
        }else {
            Toast.makeText(this, "Password doesn't Match!", Toast.LENGTH_SHORT).show();
           newPassword.requestFocus();
        }

    }
}
