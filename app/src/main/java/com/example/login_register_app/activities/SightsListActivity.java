package com.example.login_register_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.login_register_app.SightsListAdapter;
import com.example.login_register_app.databinding.ActivitySightsListBinding;
import com.example.login_register_app.models.Sight;

import java.util.ArrayList;

public class SightsListActivity extends AppCompatActivity {

    private ArrayList<Sight> sights;

    ActivitySightsListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySightsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sights = new ArrayList<>();
        sights = getIntent().getParcelableArrayListExtra("SIGHTS_LIST");

        SightsListAdapter listAdapter = new SightsListAdapter(SightsListActivity.this, sights);
        binding.listViewSights.setAdapter(listAdapter);
        binding.listViewSights.setClickable(true);
        binding.listViewSights.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(SightsListActivity.this, OneSightActivity.class);
                intent.putExtra("SIGHT_NAME", sights.get(position).getName());
                intent.putExtra("SIGHT_CITY", sights.get(position).getCity());
                intent.putExtra("SIGHT_IMG", sights.get(position).getSightImg());
                startActivity(intent);
                finish();
            }
        });
    }
}