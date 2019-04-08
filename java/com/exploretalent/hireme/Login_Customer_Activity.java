package com.exploretalent.hireme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

public class Login_Customer_Activity extends AppCompatActivity implements View.OnClickListener{

    Button login,signup;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    EditText userName,password;
    DatabaseReference databaseReferenceCustomer;
    DatabaseReference databaseReferenceService;
    String key;
    TextView forgotPassword;

    String matchUser="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);

        login=(Button)findViewById(R.id.logIn);
        signup=(Button)findViewById(R.id.signUp);
        userName = (EditText)findViewById(R.id.userName);
        password = (EditText)findViewById(R.id.passWord);
        progressDialog= new ProgressDialog(this);
        forgotPassword = (TextView)findViewById(R.id.forgotPassword);

        databaseReferenceCustomer= FirebaseDatabase.getInstance().getReference("Customer");
        databaseReferenceService = FirebaseDatabase.getInstance().getReference("Service Provider");


        firebaseAuth=FirebaseAuth.getInstance();

       // currentUserLoggedInorNotLoggedIn();



      signup.setOnClickListener(this);
      login.setOnClickListener(this);
      forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        View view = v;
        if(view==login){
            loginCode();

        }
        if (view==signup){

            Intent intent = new Intent(Login_Customer_Activity.this,Registration_Activity.class);
            intent.putExtra("check","Customer");
            startActivity(intent);

        }
        if(view==forgotPassword){

            finish();
            Intent intent = new Intent(Login_Customer_Activity.this,Forgot_Password_Activity.class);
            intent.putExtra("login_to_forgot_password","Customer");
            startActivity(intent);

        }
    }



    private void loginCode() {

        String email = userName.getText().toString().trim();
        String passwords = password.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "email field is vacant", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passwords)) {
            Toast.makeText(this, "password field is vacant", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Loging.....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        firebaseAuth.signInWithEmailAndPassword(email, passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                try {
                    if (task.isSuccessful()) {

                        //checking();
                        databaseReferenceService.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    key = snapshot.getKey().toString();
                                    if(key.equals(firebaseAuth.getUid())){
                                        matchUser="No";
                                        break;
                                    }

                                }
                                if(matchUser.equals("No")){
                                    firebaseAuth.signOut();
                                    Toast.makeText(Login_Customer_Activity.this, "Sorry not authenticated Customer!", Toast.LENGTH_SHORT).show();
                                }else {
                                    checkEmailIsVerified();
                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(Login_Customer_Activity.this, "Error!!!", Toast.LENGTH_SHORT).show();

                            }
                        });

                        /*if (matchUser.equals("yes")) {
                            firebaseAuth.signOut();
                            Toast.makeText(Login_Customer_Activity.this, "Sorry not authenticated for customer!", Toast.LENGTH_SHORT).show();
                        } else {

                            checkEmailIsVerified();
                        }*/
                    } else {

                        Toast.makeText(Login_Customer_Activity.this, "Invalid User!!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(Login_Customer_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    /*public  void checking(){*/


   // }
    public void  checkEmailIsVerified(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Boolean isverified= firebaseUser.isEmailVerified();
        if(isverified){
            finish();
            Intent intent = new Intent(Login_Customer_Activity.this, Customer_Profile.class);
            startActivity(intent);
            Toast.makeText(Login_Customer_Activity.this, "Congrats You are successfully Logged in", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Please verify your email!", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }
}
