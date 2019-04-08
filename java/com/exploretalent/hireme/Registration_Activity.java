package com.exploretalent.hireme;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Registration_Activity extends AppCompatActivity {
    EditText etLocation;
    Button pickButton, registerButton, sendCode, verifyCodeBTN;
    ImageView setImage;
    Uri imageUri = null;
    String data;
    Uri tempUri = null;
    private StorageTask storageTask;
    public static final int PICK_IMAGE = 1;

    TextView textView;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    EditText name, username, password, confPassword, contact, address, verifyingCOdetxt;
    String names, addresses, contacts, worktype, locations, email;

    FirebaseDatabase database;
    DatabaseReference myRef, mRefCus;
    StorageReference mStorageRef, mStorageRefSP_CUS;
    StorageTask mUploadTask;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    String codeSent;  //string code which will be sent to phone no.
    String codeFromcodeetd;  //to get the text string from code field
    String codeString="";  //To store value that defines verified no. used in verification before registration

    Spinner sp;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                setImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        super.onResume();

        //All xml elements are initialized here
        pickButton = (Button) findViewById(R.id.btnPick);
        registerButton = (Button) findViewById(R.id.register);
        setImage = (ImageView) findViewById(R.id.imageView);
        name = (EditText) findViewById(R.id.name);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.passWord);
        confPassword = (EditText) findViewById(R.id.confirmPassword);
        contact = (EditText) findViewById(R.id.contactNumber);
        address = (EditText) findViewById(R.id.address);
        sendCode = (Button) findViewById(R.id.sendCodeID);
        verifyCodeBTN = (Button) findViewById(R.id.verifyCodeID);
        verifyingCOdetxt = (EditText) findViewById(R.id.codeID);

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {


            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                codeSent=s;
                Toast.makeText(Registration_Activity.this, "Code sent to number", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {


            }
        };



        //sendCode To phone no. click event
        sendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCodeToPhone();
            }
        });

        //Verify code sent to the phone no. click event
        verifyCodeBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyCodeSentToPhone();
            }
        });

        progressDialog = new ProgressDialog(this);


        firebaseAuth = FirebaseAuth.getInstance();
        //database = FirebaseDatabase.getInstance();



        sp = (Spinner) findViewById(R.id.spinner);

        //This Bundle is form Login_Customer_Activity if "customer" hide spinner for worktype
        Bundle bundle = getIntent().getExtras();
        data = bundle.getString("check");
        if (data.equals("Customer")) {
            sp.setVisibility(View.INVISIBLE);
        }

        etLocation = (EditText) findViewById(R.id.location);
        etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration_Activity.this, MapsActivity.class);
                intent.putExtra("Checking", data);
                startActivity(intent);
                onPause();
            }
        });

        //Bundle from mapsActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            double latitude = extras.getDouble("lat");
            double longitude = extras.getDouble("lng");
            etLocation.setText(String.valueOf(latitude) + " , " + String.valueOf(longitude));

        }

        //image retrieving from storage btn
        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);

            }

        });


        //Registering button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                registerServiceProviderCustomer();

            }
        });

        ArrayList<String> jobType = new ArrayList<>();
        jobType.add("Electrician");
        jobType.add("Plumber");
        jobType.add("Maid");
        jobType.add("Florist");
        jobType.add("Mechanic");
        jobType.add("Painter");
        jobType.add("Photographer");
        jobType.add("Tailor");
        jobType.add("Carpenter");
        jobType.add("Firefighter");
        jobType.add("PoliceOfficer");
        jobType.add("Builder");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.spinner_layout, jobType);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
        sp.setSelection(0);
        sp.setAdapter(arrayAdapter);



        myRef = FirebaseDatabase.getInstance().getReference("Service Provider");
        mRefCus = FirebaseDatabase.getInstance().getReference("Customer");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //Registering user with some information on database = registerServiceProvider()
    private void registerServiceProviderCustomer() {

        textView = (TextView) sp.getSelectedView();
        worktype = textView.getText().toString();

        names = name.getText().toString().trim();
       /* final TextView textView = (TextView) sp.getSelectedView();
        worktype = textView.getText().toString();*/

        addresses = address.getText().toString().trim();

        //contacts = contact.getText().toString().trim();


        locations = etLocation.getText().toString().trim();

        if (names == null || addresses == null || contacts == null || locations == null) {
            Toast.makeText(this, "Please fill the information and try again!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!codeString.equals("verified")){
            Toast.makeText(this, "Please verify phone no. and try again", Toast.LENGTH_SHORT).show();
            contact.requestFocus();
            return;
        }
        if(imageUri==null){
            Toast.makeText(this, "Please choose image and try again!", Toast.LENGTH_SHORT).show();
        }

        email = username.getText().toString().trim();
        String passwords = password.getText().toString().trim();
        String confirmPasswords = confPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "email field is vacant", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(passwords)) {
            Toast.makeText(this, "password field is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(passwords)) {
            Toast.makeText(this, "password field is empty", Toast.LENGTH_SHORT).show();
            confPassword.requestFocus();
            return;
        }

        progressDialog.setMessage("Registering.....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        if (passwords.equals(confirmPasswords)) {
            try {
                firebaseAuth.createUserWithEmailAndPassword(email, passwords).addOnCompleteListener
                        (Registration_Activity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {

                                    sendEmailVerification();//Email Verification method call


                                } else {

                                    Toast.makeText(Registration_Activity.this, "please validate your username", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                        });
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Password Doesn't match", Toast.LENGTH_SHORT).show();
        }
    }//End Of registerServiceProviderCustomer() method

    //sendEmailVerification() Method starts from here
    private void sendEmailVerification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        sendUserData();
                        Toast.makeText(Registration_Activity.this, "Successfully Registered, Verification mail sent!", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();

                        if (data.equals("ServiceProvider")) {
                            startActivity(new Intent(Registration_Activity.this, Service_Provider_Login_Activity.class));
                        }
                        if (data.equals("Customer")) {
                            startActivity(new Intent(Registration_Activity.this, Login_Customer_Activity.class));
                        }

                    } else {
                        Toast.makeText(Registration_Activity.this, "Verification mail has'nt been sent!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    //sendUserData Method starts from here
    //
    public void sendUserData() {
        if (imageUri != null) {

            mStorageRef = FirebaseStorage.getInstance().getReference("Service_Provider_Customerr_Images").child(firebaseAuth.getUid());

            UploadTask uploadTask = mStorageRef.putFile(imageUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Registration_Activity.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(Registration_Activity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                }
            });
            String uploadId = firebaseAuth.getUid();

            if (data.equals("ServiceProvider")) {
                Information_Class info = new Information_Class(email, names, worktype, addresses, contacts, locations);
                myRef.child(uploadId).setValue(info);
            }

            if (data.equals("Customer")) {
                Information_Class infos = new Information_Class(email, names, addresses, contacts, locations);
                mRefCus.child(uploadId).setValue(infos);

            }

        } else {
            Toast.makeText(Registration_Activity.this, "No pic is selected!", Toast.LENGTH_SHORT).show();
            return;

        }
    }


    //to restore the activity instance state
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        name.setText(savedInstanceState.getString("Name"));
        username.setText(savedInstanceState.getString("UserName"));
        password.setText(savedInstanceState.getString("Password"));
        confPassword.setText(savedInstanceState.getString("ConfPassword"));
        address.setText(savedInstanceState.getString("Address"));
        etLocation.setText(savedInstanceState.getString("Location"));
        contact.setText(savedInstanceState.getString("Contact"));
        sp.setSelection(savedInstanceState.getInt("Spinner"));
        setImage.setImageBitmap((Bitmap) savedInstanceState.getParcelable("Image"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("Name", name.getText().toString());
        outState.putString("UserName", username.getText().toString());
        outState.putString("Password", password.getText().toString());
        outState.putString("ConfPassword", confPassword.getText().toString());
        outState.putString("Contact", contact.getText().toString());
        outState.putString("Address", address.getText().toString());
        outState.putString("Location", etLocation.getText().toString());
        outState.putInt("Spinner", sp.getSelectedItemPosition());
        outState.putParcelable("Image", setImage.getDrawingCache(true));

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    // sendCodeToPhone() method to verify phone no.
    private void sendCodeToPhone() {
        contacts = contact.getText().toString();
        if (contacts.isEmpty()) {
            contact.setError("Phone number required");
            contact.requestFocus();
            return;
        }
        if (contacts.length() < 10) {
            contact.setError("Please enter a valid number");
            contact.requestFocus();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                contacts,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    public  void  verifyCodeSentToPhone(){
        codeFromcodeetd = verifyingCOdetxt.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, codeFromcodeetd);

        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            codeString="verified";

                            Toast.makeText(Registration_Activity.this, "Code is verified",
                                    Toast.LENGTH_SHORT).show();
                            verifyingCOdetxt.setText("Verified");
                        }

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(Registration_Activity.this, "Incorrect verification code",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }



}
