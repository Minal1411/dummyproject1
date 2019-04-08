package com.exploretalent.hireme;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Service_Provider_Profile extends AppCompatActivity {

    ImageView imageSP;
    TextView emailnameSP,worktypeSP,nameSP,addressSP,contactSP,locationSP;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_service_provider);

        imageSP=findViewById(R.id.imageServiceProvider);
        emailnameSP = findViewById(R.id.emailServiceProvider);
        worktypeSP=findViewById(R.id.worktypeServiceProvider);
        nameSP =findViewById(R.id.nameServiceProvider);
        addressSP=findViewById(R.id.addressServiceProvider);
        contactSP=findViewById(R.id.contactServiceProvider);
        locationSP=findViewById(R.id.locationServiceProvider);

        tb = findViewById(R.id.toolbar);
        tb.setTitle("Service Provider");
        setSupportActionBar(tb);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Service Provider"+"/"+firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Information_Class informationSP = dataSnapshot.getValue(Information_Class.class);
                worktypeSP.setText(informationSP.getWorktype());
                nameSP.setText(informationSP.getName());
                addressSP.setText(informationSP.getAddress());
                contactSP.setText(informationSP.getContact());
                locationSP.setText(informationSP.getLocation());
                emailnameSP.setText(informationSP.getEmail());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Service_Provider_Profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Service_Provider_Customerr_Images");
        storageReference.child(firebaseAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(imageSP);

            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Service_Provider_Profile.this,MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profilemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.logoutId:

                firebaseAuth.signOut();
                onBackPressed();
                break;

            case  R.id.editId:

                startActivity( new Intent(Service_Provider_Profile.this,Edit_Profile_Service_Provider.class));
                break;

            case R.id.changePassID:
                startActivity(new Intent(Service_Provider_Profile.this,ChangePassword_Activity.class));
                break;

            default:

                break;



        }
        return true;
    }
}
