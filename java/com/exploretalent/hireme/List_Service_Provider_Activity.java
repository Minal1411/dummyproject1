package com.exploretalent.hireme;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
//import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class List_Service_Provider_Activity extends AppCompatActivity implements LocationListener{

    Toolbar toolbar;

    FragmentManager fm;
    FragmentTransaction t;

    Spinner spinner;
    ListView listView;

    String workTypeSpinner="";
    String workTypeDatabase="";
    String desLocation;

    DatabaseReference databaseReference, sortDatabaseReference;

    ArrayList<ListViewInfo_Class> list;

    double longitude, latitude;
    double endLatitude,endLongitude;
    float[] resultDistance;

    int count=0;
    String parentKey;
    LocationManager locationManager;
    Location locationVariable;

    int distanceInt;
    TextView textView;
    HashMap<String,Integer> hashMap;
    Map<String, Integer> sortedDistance;
    //ArrayList<Integer> targetList;
    Map<Integer,Information_Class>  intInfoClassHashMap;
    Information_Class information_class;
    private ProgressDialog progressDialog;


    String nameOfSp,emailOfSp,worktypeOfSp,addressOfSp,locationOfSp,contactNoOfSp;


    @Override
    protected void onCreate(Bundle savedInstanceState) throws RuntimeException{
        try{

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__service__provider_);

        toolbar = findViewById(R.id.toolbarListID);
        toolbar.setTitle("Hire");
        setSupportActionBar(toolbar);
        progressDialog=new ProgressDialog(this);

        fm = getFragmentManager();

        spinner = (Spinner) findViewById(R.id.spinnerListID);
        listView = (ListView) findViewById(R.id.list_item_id);

        list = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Service Provider");

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
        //spinner.setSelection(0);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position,true);
                list.clear();
               // targetList.clear();
                hashMap.clear();
                onStart();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListViewInfo_Class info_class = list.get(position);

            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
        locationVariable = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

        // onLocationChanged(locationVariable);

        hashMap = new HashMap<>();
        intInfoClassHashMap = new HashMap<>();
        information_class = new Information_Class();

    }catch(Exception e)
    {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    }

    @Override
    protected void onStart(){
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //listView.setAdapter(null);
               // listView.setAdapter(null);
                textView = (TextView) spinner.getSelectedView();
                workTypeSpinner = textView.getText().toString();

               // MapFragmentClass mapFragmentClass = new MapFragmentClass();
                //mapFragmentClass.spinnerWorktype(workTypeSpinner);
                Bundle bundle = new Bundle();
                bundle.putString("spinner",workTypeSpinner);


                Log.e("spinner Value: ",workTypeSpinner);

                Fragment mapFragmentClass = new MapFragmentClass();
                mapFragmentClass.setArguments(bundle);

                t=fm.beginTransaction();

                t.replace(R.id.mapID, mapFragmentClass).commit();


               /* Bundle bundle = new Bundle();
                bundle.putString("spinner",workTypeSpinner);


                Log.e("spinner Value: ",workTypeSpinner);

                Fragment mapFragmentClass = new MapFragmentClass();
                mapFragmentClass.setArguments(bundle);
                t.replace(R.id.mapID, new MapFragmentClass()).commit();
*/

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){

                    //parentKey = dataSnapshot.getKey().push().getKey();
                    parentKey = snapshot.getKey();
                    //String key = dataSnapshot.child("https://hireme-3a23f.firebaseio.com/Service Provider").getKey();//dataSnapshot.child("Service Provider").getKey();
                    Information_Class information_class = snapshot.getValue(Information_Class.class);
                    desLocation=information_class.getLocation();
                    workTypeDatabase=information_class.getWorktype();
                    if(workTypeDatabase.equals(workTypeSpinner)) {


                        //list.add(information_class);
                        //parentKey = databaseReference.push().getKey();//dataSnapshot.getKey();
                        onLocationChanged(locationVariable);
                          distanceCalculate();
                        //count++;
                    }
                }

                try {
                    sortingDistanceWithFirebaseID();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    showSortedListViewItemsMethod();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public  void sortingDistanceWithFirebaseID(){
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(hashMap.entrySet());

        if(hashMap.size()>1) {
            Collections.sort(entries, new Comparator<Entry<String, Integer>>() {
                @Override
                public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });
        }
        sortedDistance = new LinkedHashMap<>();

        for (Map.Entry<String,Integer> entry : entries) {
            sortedDistance.put(entry.getKey(), entry.getValue());
        }

    }


    public  void  showSortedListViewItemsMethod(){
        try{
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        for (final String keys : sortedDistance.keySet()) {
           // System.out.println("Key = " + key);
            sortDatabaseReference=FirebaseDatabase.getInstance().getReference("Service Provider/"+keys);
            sortDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                            //parentKey = dataSnapshot.getKey();
                            Information_Class information_class = dataSnapshot.getValue(Information_Class.class);
                            nameOfSp=information_class.getName();
                            emailOfSp=information_class.getEmail();
                            worktypeOfSp=information_class.getWorktype();
                            addressOfSp=information_class.getAddress();
                            locationOfSp=information_class.getLocation();
                            contactNoOfSp=information_class.getContact();

                            ListViewInfo_Class listViewInfo_class = new ListViewInfo_Class(nameOfSp,emailOfSp,worktypeOfSp,addressOfSp,
                                    locationOfSp,contactNoOfSp,sortedDistance.get(keys),keys);
                            list.add(listViewInfo_class);


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }
        }catch (Exception e)
        {
            Toast.makeText(List_Service_Provider_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something here
                showInListViewMethod();
            }
        }, 5000);




    }
    public void showInListViewMethod(){

            ListViewAdapter listViewAdapter = new ListViewAdapter(List_Service_Provider_Activity.this,
                    R.layout.custom_listview_items, list);
            listView.setAdapter(listViewAdapter);
            progressDialog.dismiss();

    }


    public void distanceCalculate(){

        String[] latlang=desLocation.split(",");
        endLatitude=Double.parseDouble(latlang[0]);
        endLongitude=Double.parseDouble(latlang[1]);

        resultDistance = new float[10];

        Location.distanceBetween(latitude,longitude,endLatitude,endLongitude,resultDistance);

        distanceInt = Math.round(resultDistance[0]);//Math.round() is overloaded function that takes both float and double data


        hashMap.put(parentKey,distanceInt);

    }

    //LocationListener interface's implemented methods
    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
