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

    // Latitude & longitude of my current location.
    private double myLat, myLong;
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

                // Step 3.1. Get the instance for the root node in Firebase.
                rootNode = FirebaseDatabase.getInstance();
                // Step 3.2. Set a new path (with name User) for storing data inside the root node.
                reference = rootNode.getReference("Sights");

                Sight s1 = new Sight("Acropolis", "Athens",37.970833, 23.726110, R.drawable.acropolis);
                Sight s2 = new Sight("Knossos", "Crete",35.2921088316,  25.1578927018, R.drawable.knossos);
                Sight s3 = new Sight("Meteora", "Kalampaka",39.6999972,  21.6166642, R.drawable.meteora);
                Sight s4 = new Sight("Delos", "Cyclades",37.389331776,  25.269332256, R.drawable.delos);
                Sight s5 = new Sight("Navagio", "Zakynthos",37.854663248, 20.622664176, R.drawable.navagio);
                Sight s6 = new Sight("Temple of Olympian Zeus", "Athens",37.9682861268, 23.7265104273, R.drawable.temple_olympian_zeus);
                Sight s7 = new Sight("Mykines", "Argolida",37.7195776, 22.7457762,R.drawable.mykines);
                Sight s8 = new Sight("Kokkini Paralia", "Santorini",36.34891, 25.3939,R.drawable.kokkini_paralia);
                Sight s9 = new Sight("Panathenaic Stadium", "Athens",37.967662796, 23.737997048,R.drawable.panathenaic_stadium);
                Sight s10 = new Sight("Lykavitos", "Athens",37.983551, 23.743130,R.drawable.lykavitos);
                Sight s11 = new Sight("Akrotiri", "Santorini",36.35083193, 25.402165058,R.drawable.akrotiri);
                Sight s12 = new Sight("Elafonissi Beach", "Crete",35.268165594, 23.526164562,R.drawable.elafonissi_beach);
                Sight s13 = new Sight("Samariá Gorge", "Crete",35.269332256, 23.956829506,R.drawable.samaria_gorge);
                Sight s14 = new Sight("Melissani Cave", "Kefalonia",38.25416565, 20.62083085, R.drawable.melissani_cave);
                Sight s15 = new Sight("Porto Katsiki", "Lefkada",38.601497594, 20.542997828, R.drawable.porto_katsiki);
                Sight s16 = new Sight("Caves of Diros", "Peloponnisos",36.638684536664584, 22.380683759374538,R.drawable.caves_diros);
                Sight s17 = new Sight("Fortress of Palamidi", "Navplio",37.56155384736494, 22.804848866157553,R.drawable.fortress_palamidi);
                Sight s18 = new Sight("Archaio Theatro Asklipieiou Epidavrou", "Peloponnisos",37.60077618554391, 23.07831424676064,R.drawable.theatro_epidauros);
                Sight s19 = new Sight("Costa Navarino", "Kalamata",36.99661050323303, 21.650677981477152,R.drawable.costa_navarino);
                Sight s20 = new Sight("Acrocorinth", "Korinthos",37.891177720167995, 22.87006218460847, R.drawable.acrocorinth);
                Sight s21 = new Sight("Lefkos Pyrgos", "Thessaloniki",40.626446, 22.948426, R.drawable.lefkos_pyrgos);
                Sight s22 = new Sight("Apsida toy Galerioy", "Thessaloniki",40.632362095974884, 22.95174036932001, R.drawable.apsida_galerioy);
                Sight s23 = new Sight("Mouseio Atatourk", "Thessaloniki",40.63585852176295, 22.954302182812555, R.drawable.ataturk);
                Sight s24 = new Sight("Rotonta", "Thessaloniki",40.63344853575982, 22.95300100185786, R.drawable.rotonta);
                Sight s25 = new Sight("Eptapyrgio", "Thessaloniki",40.64433131405023, 22.96204558161576, R.drawable.eptapyrgio);
                Sight s26 = new Sight("Iera Moni Vlatadwn", "Thessaloniki",40.641941317115915, 22.954555471171194, R.drawable.moni_vlatadwn);

                Sight s27 = new Sight("Moni Latomoy", "Thessaloniki",40.641697158680834, 22.952367274960277, R.drawable.moni_latomoy);
                Sight s28 = new Sight("Ieros Naos Panagias Axeiropoihtoy", "Thessaloniki",40.635032133626794, 22.947903728142247, R.drawable.panagia_acheiropoiitos);
                Sight s29 = new Sight("Ieros Naos Agias Sofias", "Thessaloniki",40.63303148922419, 22.94743572460531, R.drawable.ieros_agia_sofia);
                Sight s30 = new Sight("Oi Ompreles tis Zoggolopoulou", "Thessaloniki",40.62194435473573, 22.95143171603668, R.drawable.ompreles_ziggolopoulou);
                Sight s31 = new Sight("Katarraktes Nedas", "Figaleia",37.40486968715838, 21.82249705198038, R.drawable.katarraktes_nedas);
                Sight s32 = new Sight("Achaia Klaous", "Patras",38.19716392976844, 21.77025535523934, R.drawable.achaia_klaous);
                Sight s33 = new Sight("Mouseio Archmidi", "Arxaia Olympia",37.64320388012092, 21.625200549500445, R.drawable.mouseio_arximidi);
                Sight s34 = new Sight("Arxaiologiko Mouseio Patrwn", "Patras",38.26368377816827, 21.752481029893918, R.drawable.arxaiologiko_mouseio_patrwn);
                Sight s35 = new Sight("Rwmaiko Wdeio Patrwn", "Patras",38.24338619528834, 21.739258093331316, R.drawable.rwmaiko_wdeio_patras);
                Sight s36 = new Sight("Sphlaio twn Limnwn", "Peloponnisos",37.9625488795335, 22.142653043856328, R.drawable.spilaio_ton_limnon);
                Sight s37 = new Sight("Pyges Kryas", "Leivadia",38.431923952634264, 22.874995162651032, R.drawable.pyges_kryas);
                Sight s38 = new Sight("Mouseio Delfwn", "Delfoi",38.480330549053335, 22.499901594388625, R.drawable.mouseio_delfoi);
                Sight s39 = new Sight("Karampampa Castle", "Ksirovrysi",38.464982976819265, 23.58531583406306, R.drawable.karababa_castle);
                Sight s40 = new Sight("Temple of Athena Pronaia", "Delfoi",38.480185031158435, 22.507896360738098, R.drawable.temple_athena_pronaia);


                sights.add(s1); sights.add(s2); sights.add(s3);
                sights.add(s4); sights.add(s5); sights.add(s6);
                sights.add(s7); sights.add(s8); sights.add(s9);
                sights.add(s10); sights.add(s11); sights.add(s12);
                sights.add(s13); sights.add(s14); sights.add(s15);
                sights.add(s16); sights.add(s17); sights.add(s18);
                sights.add(s19); sights.add(s20); sights.add(s21);
                sights.add(s22); sights.add(s23); sights.add(s24);
                sights.add(s25); sights.add(s26); sights.add(s27);
                sights.add(s28); sights.add(s29); sights.add(s30);
                sights.add(s31); sights.add(s32); sights.add(s33);
                sights.add(s34);  sights.add(s35); sights.add(s36);
                sights.add(s37); sights.add(s38); sights.add(s39);
                sights.add(s40);

                // Step 3.3. Store all the list data (param in setValue) inside firebase.
                for (Sight s : sights) {
                    reference.child(""+s.getName()).setValue(s);
                }
                /*Intent intent = new Intent(SightsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();*/
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