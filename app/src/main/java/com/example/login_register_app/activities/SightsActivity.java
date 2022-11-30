package com.example.login_register_app.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.login_register_app.LoadingDialog;
import com.example.login_register_app.R;
import com.example.login_register_app.models.Sight;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SightsActivity extends AppCompatActivity implements LocationListener {

    Button btnMap, btnSightsList, btnFind, btnBack;

    Switch switchBtnMap, switchBtnList;

    TextView txtNearestSightLabel, txtNearestSightResult;

    // Firebase objects for reading database.
    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    // List with all sights in Firebase.
    private ArrayList<Sight> sights;

    // Location manager object.
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sights);

        LoadingDialog loadingDialog = new LoadingDialog(SightsActivity.this);

        rootNode = FirebaseDatabase.getInstance();
        sights = new ArrayList<>();

        btnMap = findViewById(R.id.btnMap);
        btnMap.setVisibility(View.INVISIBLE);
        btnSightsList = findViewById(R.id.btnSightsList);
        btnSightsList.setVisibility(View.INVISIBLE);

        btnFind = findViewById(R.id.btnFind);
        btnBack = findViewById(R.id.btnBack);

        switchBtnMap = findViewById(R.id.switchBtnMap);
        switchBtnList = findViewById(R.id.switchBtnList);

        txtNearestSightLabel= findViewById(R.id.txtNearestSightLabel);
        txtNearestSightLabel.setVisibility(View.INVISIBLE);

        txtNearestSightResult = findViewById(R.id.txtNearestSightResult);

        //Runtime permissions for get access to Location.
        if (ContextCompat.checkSelfPermission(SightsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SightsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        switchBtnMap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchBtnList.setChecked(!switchBtnMap.isChecked());
                btnMap.setVisibility(View.VISIBLE);
                btnSightsList.setVisibility(View.INVISIBLE);
            }
        });

        switchBtnList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchBtnMap.setChecked(!switchBtnList.isChecked());
                btnSightsList.setVisibility(View.VISIBLE);
                btnMap.setVisibility(View.INVISIBLE);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference = rootNode.getReference().child("Sights");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Sight sight = dataSnapshot.getValue(Sight.class);
                            sights.add(sight);
                        }
                        Intent intent = new Intent(SightsActivity.this, MapsActivity.class);
                        intent.putParcelableArrayListExtra("SIGHTS_LIST", sights);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });

        btnSightsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = rootNode.getReference().child("Sights");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Sight sight = dataSnapshot.getValue(Sight.class);
                            sights.add(sight);
                        }
                        Intent intent = new Intent(SightsActivity.this, SightsListActivity.class);
                        intent.putParcelableArrayListExtra("SIGHTS_LIST", sights);
                        startActivity(intent);
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingDialog.startLoadingDialog();
                Handler handler = new Handler();
                handler.postDelayed(loadingDialog::dismissDialog, 4855);
                getMyLocation();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SightsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void getMyLocation(){

        try{
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, SightsActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        findNearestSight(location.getLatitude(), location.getLongitude());
    }

    private void findNearestSight(double myLat, double myLong) {

        // List with all the distances from my location to sights.
        ArrayList<Float> distances = new ArrayList<>();

        // Create my location object.
        Location myLocation = new Location("my_location");
        myLocation.setLatitude(myLat);
        myLocation.setLongitude(myLong);

        reference = rootNode.getReference().child("Sights");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Sight sight = dataSnapshot.getValue(Sight.class);
                    sights.add(sight);
                }
                // Compute & save to the proper list all the distances from my location to all sights.
                for (Sight sight : sights){
                    Location otherLocation = new Location(sight.getName());
                    otherLocation.setLatitude(sight.getLatitude());
                    otherLocation.setLongitude(sight.getLongitude());
                    float distance = myLocation.distanceTo(otherLocation);
                    distances.add(distance);
                }

                // Find & save the position of the min distance from the list.
                double minDistance = distances.get(0);
                int minPosCenter = 0;

                for (int i = 1 ; i < distances.size(); i++){
                    if (distances.get(i) < minDistance){
                        minDistance = distances.get(i);
                        minPosCenter = i;
                    }
                }

                if (txtNearestSightLabel.getVisibility() == View.INVISIBLE) {
                    txtNearestSightLabel.setVisibility(View.VISIBLE);
                    txtNearestSightResult.setText(sights.get(minPosCenter).getName());
                } else {
                    txtNearestSightLabel.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}