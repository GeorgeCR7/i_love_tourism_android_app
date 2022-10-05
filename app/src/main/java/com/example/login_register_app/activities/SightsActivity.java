package com.example.login_register_app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.login_register_app.R;
import com.example.login_register_app.models.Sight;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SightsActivity extends AppCompatActivity {

    Button btnMap, btnFind, btnBack;

    TextView txtNearestSightLabel, txtNearestSightResult;

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private ArrayList<Sight> sights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sights);

        rootNode = FirebaseDatabase.getInstance();
        sights = new ArrayList<>();

        btnMap = findViewById(R.id.btnMap);
        btnFind = findViewById(R.id.btnFind);
        btnBack = findViewById(R.id.btnBack);

        txtNearestSightLabel= findViewById(R.id.txtNearestSightLabel);
        txtNearestSightResult = findViewById(R.id.txtNearestSightResult);

        txtNearestSightLabel.setVisibility(View.INVISIBLE);

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

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //findNearestSight();



                // Step 3.1. Get the instance for the root node in Firebase.
                rootNode = FirebaseDatabase.getInstance();
                // Step 3.2. Set a new path (with name User) for storing data inside the root node.
                reference = rootNode.getReference("Sights");

                Sight s1 = new Sight("Acropolis", "Athens",37.970833, 23.726110);
                Sight s2 = new Sight("Knossos", "Crete",35.2921088316,  25.1578927018);
                Sight s3 = new Sight("Meteora", "Kalampaka",39.6999972,  21.6166642);
                Sight s4 = new Sight("Delos", "Cyclades",37.389331776,  25.269332256);
                Sight s5 = new Sight("Navagio", "Zakynthos",37.854663248, 20.622664176);
                Sight s6 = new Sight("Temple of Olympian Zeus", "Athens",37.9682861268, 23.7265104273);
                Sight s7 = new Sight("Mykines", "Argolida",37.7195776, 22.7457762);
                Sight s8 = new Sight("Kokkini Paralia", "Santorini",36.34891, 25.3939);
                Sight s9 = new Sight("Panathenaic Stadium", "Athens",37.967662796, 23.737997048);
                Sight s10 = new Sight("Lykavitos", "Athens",37.983551, 23.743130);
                Sight s11 = new Sight("Akrotiri", "Santorini",36.35083193, 25.402165058);
                Sight s12 = new Sight("Elafonissi Beach", "Crete",35.268165594, 23.526164562);
                Sight s13 = new Sight("Samariá Gorge", "Crete",35.269332256, 23.956829506);
                Sight s14 = new Sight("Melissani Cave", "Kefalonia",38.25416565, 20.62083085);
                Sight s15 = new Sight("Porto Katsiki", "Lefkada",38.601497594, 20.542997828);

                sights.add(s1); sights.add(s2); sights.add(s3);
                sights.add(s4); sights.add(s5); sights.add(s6);
                sights.add(s7); sights.add(s8); sights.add(s9);
                sights.add(s10); sights.add(s11); sights.add(s12);
                sights.add(s13); sights.add(s14); sights.add(s15);

                // Step 3.3. Store all the list data (param in setValue) inside firebase.
                for (Sight s : sights) {
                    reference.child(""+s.getName()).setValue(s);
                }
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

    private void findNearestSight() {

        if (txtNearestSightLabel.getVisibility() == View.INVISIBLE) {
            txtNearestSightLabel.setVisibility(View.VISIBLE);
            //txtNearestSightResult.setText("Akropoli");
        } else {
            txtNearestSightLabel.setVisibility(View.VISIBLE);
        }
    }
}