package com.exploretalent.hireme;

/*import android.app.FragmentManager;
import android.app.FragmentTransaction;*/
import android.app.ProgressDialog;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class Customer_Profile extends AppCompatActivity implements View.OnClickListener{

    private ImageView imageC;
    private TextView emailC,nameC,addressC,contactC,locationC;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Button hireServiceProviderBtn;
    ProgressDialog progressDialog;
    Toolbar tb;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Customer_Profile.this,MainActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profilemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_customer);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();
        tb = (Toolbar)findViewById(R.id.toolbar);
        tb.setTitle("Customer");
        setSupportActionBar(tb);
        progressDialog.setCanceledOnTouchOutside(false);
        imageC = findViewById(R.id.imageC);
        emailC = findViewById(R.id.emailC);
        nameC = findViewById(R.id.nameC);
        addressC = findViewById(R.id.addressC);
        contactC = findViewById(R.id.contactC);
        locationC = findViewById(R.id.locationC);
        hireServiceProviderBtn=(Button)findViewById(R.id.hireSPBtnID);

        hireServiceProviderBtn.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Customer/"+firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    String myparent = dataSnapshot.getKey();
                    Log.e("parentname", myparent);
                    Information_Class customerInfo = dataSnapshot.getValue(Information_Class.class);
                    nameC.setText(customerInfo.getName());
                    addressC.setText(customerInfo.getAddress());
                    contactC.setText(customerInfo.getContact());
                    locationC.setText(customerInfo.getLocation());
                    emailC.setText(customerInfo.getEmail());
                }catch (Exception e){
                    Toast.makeText(Customer_Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Customer_Profile.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference("Service_Provider_Customerr_Images");
        storageReference.child(firebaseAuth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).fit().centerCrop().into(imageC);
                    progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.logoutId:

                firebaseAuth.signOut();
                onBackPressed();
                break;

            case  R.id.editId:

                startActivity( new Intent(Customer_Profile.this,Edit_Profile_Customer.class));
                finish();
                break;

            case R.id.changePassID:
                startActivity(new Intent(Customer_Profile.this,ChangePassword_Activity.class));
                finish();
                break;

            default:
                break;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        if(v==hireServiceProviderBtn){
            startActivity(new Intent(Customer_Profile.this,List_Service_Provider_Activity.class));
            finish();
        }

    }
}
