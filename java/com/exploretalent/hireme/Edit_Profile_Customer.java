package com.exploretalent.hireme;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class Edit_Profile_Customer extends AppCompatActivity implements View.OnClickListener{

    EditText nameUP,addressUP,contactUP,locationUP;
    Button saveUp,btnSelectPic;
    ImageView imageViewUP;

    String name,address,contactNo,location,email;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    StorageReference mStorageRef;

    public static final int PICK_IMAGE = 1;
    Uri imageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile__customer);

        nameUP=findViewById(R.id.nameUpID);
        addressUP=findViewById(R.id.addressUpID);
        contactUP=findViewById(R.id.contactUpID);
        locationUP=findViewById(R.id.locationUpID);
        imageViewUP=findViewById(R.id.imvUpdate);
        btnSelectPic=findViewById(R.id.btnChangeID);
        saveUp = findViewById(R.id.saveBtnID);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Customer/"+firebaseAuth.getUid());

        storageReference = FirebaseStorage.getInstance().getReference("Service_Provider_Customerr_Images");

        storageReference.child(firebaseAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(imageViewUP);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Information_Class customerInfo = dataSnapshot.getValue(Information_Class.class);
                nameUP.setText(customerInfo.getName());
                addressUP.setText(customerInfo.getAddress());
                contactUP.setText(customerInfo.getContact());
                locationUP.setText(customerInfo.getLocation());
                email=customerInfo.getEmail().trim().toString();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Edit_Profile_Customer.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        saveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = nameUP.getText().toString();
                address = addressUP.getText().toString();
                contactNo = contactUP.getText().toString();
                location = locationUP.getText().toString();

                Information_Class cusinfo = new Information_Class(email,name,address,contactNo,location);
                //String email,String name, String address, String contact, String location

                databaseReference.setValue(cusinfo);

                mStorageRef = FirebaseStorage.getInstance().getReference("Service_Provider_Customerr_Images").child(firebaseAuth.getUid());

                UploadTask uploadTask = mStorageRef.putFile(imageUri);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Edit_Profile_Customer.this, "Upload failed!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        Toast.makeText(Edit_Profile_Customer.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Edit_Profile_Customer.this,Customer_Profile.class));
                        finish();

                    }
                });


            }
        });

        btnSelectPic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        View view=v;
       if(view==btnSelectPic){

           Intent intent = new Intent();
           intent.setType("image/*");
           intent.setAction(Intent.ACTION_GET_CONTENT);
           startActivityForResult(intent, 1);

       }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageViewUP.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
