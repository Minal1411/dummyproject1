package com.exploretalent.hireme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Service_Provider_Login_Activity extends AppCompatActivity implements View.OnClickListener{

    EditText email,password;
    Button signin,signup;

    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    TextView forgotPassword;

    DatabaseReference databaseReferenceCustomer;
    DatabaseReference databaseReferenceService;
    String key;
    String matchUser="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_service_provider);

        signin = (Button) findViewById(R.id.logInID);
        signup = (Button) findViewById(R.id.signUpID);
        email = (EditText) findViewById(R.id.emailNameID);
        password = (EditText) findViewById(R.id.passWordID);
        progressDialog = new ProgressDialog(this);
        forgotPassword = (TextView)findViewById(R.id.forgotPassword);


        databaseReferenceService= FirebaseDatabase.getInstance().getReference("Service Provider");
        databaseReferenceCustomer = FirebaseDatabase.getInstance().getReference("Customer");

        firebaseAuth = FirebaseAuth.getInstance();

        /*FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                databaseReferenceCustomer.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            key = snapshot.getKey().toString();
                            if(key.equals(firebaseAuth.getUid())){

                                finish();
                                startActivity(new Intent(Service_Provider_Login_Activity.this,Customer_Profile.class));
                                Toast.makeText(Service_Provider_Login_Activity.this,"Logout and try again!"
                                        , Toast.LENGTH_SHORT).show();

                            }

                        }
                        finish();
                        startActivity(new Intent(Service_Provider_Login_Activity.this, Service_Provider_Profile.class));

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(Service_Provider_Login_Activity.this, "Error!!!", Toast.LENGTH_SHORT).show();

                    }
                });


            }*/
        signup.setOnClickListener(this);
        signin.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        View view = v;
        if(view==signin){

            loginCode();

        }
        if (view==signup){

            Intent intent = new Intent(Service_Provider_Login_Activity.this,Registration_Activity.class);
            intent.putExtra("check","ServiceProvider");
            startActivity(intent);

        }
        if(view == forgotPassword){
            finish();
            Intent intent = new Intent(Service_Provider_Login_Activity.this,Forgot_Password_Activity.class);
            intent.putExtra("login_to_forgot_password","Service Provider");
            startActivity(intent);
        }
    }




    private void loginCode() {

        String emailName = email.getText().toString().trim();
        String passwordValue = password.getText().toString().trim();

        if (TextUtils.isEmpty(emailName)) {
            Toast.makeText(this, "email field is vacant", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passwordValue)) {
            Toast.makeText(this, "password field is vacant", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging.....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth.signInWithEmailAndPassword(emailName, passwordValue).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    databaseReferenceCustomer.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                key = snapshot.getKey().toString();
                                if(key.equals(firebaseAuth.getUid())){
                                    matchUser="No";
                                    break;
                                }

                            }
                            if(matchUser.equals("No")) {
                                firebaseAuth.signOut();
                                Toast.makeText(Service_Provider_Login_Activity.this,
                                        "Sorry not authenticated for Service Provider!", Toast.LENGTH_SHORT).show();

                            }else {

                                checkEmailIsVerified();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(Service_Provider_Login_Activity.this, "Error!!!", Toast.LENGTH_SHORT).show();

                        }
                    });





                }else {

                    Toast.makeText(Service_Provider_Login_Activity.this, "Invalid User!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
   // public  int checking(){
        // }
    public void  checkEmailIsVerified(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Boolean isverified= firebaseUser.isEmailVerified();
        if(isverified){
            finish();
            Intent intent = new Intent(Service_Provider_Login_Activity.this, Service_Provider_Profile.class);
            startActivity(intent);
            Toast.makeText(Service_Provider_Login_Activity.this, "Congrats You are successfully Logged in", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Please verify your email!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }
}


