package com.exploretalent.hireme;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.exploretalent.hireme.Manifest.permission;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView service_provider, customer;
    ConnectivityManager cm;
    NetworkInfo networkInfo;

    Toolbar toolbar;

    long backpressedTime;
    Toast backToast;
    int STORAGE_PERMISSION_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("HireMe");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_directions_bike_white_24dp);


        service_provider = (ImageView) findViewById(R.id.serviceprovider);
        customer = (ImageView) findViewById(R.id.servicereceiver);
        service_provider.setOnClickListener(this);
        customer.setOnClickListener(this);

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] { android.Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            return;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // On Back Pressed Event code is here below:
    @Override
    public void onBackPressed() {
        if(backpressedTime+2000>System.currentTimeMillis()){
            backToast.cancel();
            super.onBackPressed();
            return;

        }else {
            backToast = Toast.makeText(MainActivity.this,"Press back again to exit",Toast.LENGTH_SHORT);
            backToast.show();
        }
        backpressedTime=System.currentTimeMillis();

    }

    @Override
    public void onClick(View v) {
        cm=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo=cm.getActiveNetworkInfo();
        if(networkInfo!=null && networkInfo.isConnectedOrConnecting()) {
            View view= v;
            if (view == service_provider) {

                Intent intent = new Intent(this,Service_Provider_Login_Activity.class);
                startActivity(intent);

            }
            if(view == customer){

                Intent intent = new Intent(this,Login_Customer_Activity.class);
                startActivity(intent);
            }
        }
        else
        {
            Toast.makeText(this, "No Internet Connection!! Please connect to internet", Toast.LENGTH_SHORT).show();
        }

    }
}