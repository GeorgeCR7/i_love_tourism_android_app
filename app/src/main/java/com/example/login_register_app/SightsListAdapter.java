package com.example.login_register_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.login_register_app.models.Sight;

import java.util.ArrayList;

public class SightsListAdapter extends ArrayAdapter<Sight> {

    public SightsListAdapter(Context context, ArrayList<Sight> sights){
        super(context, R.layout.list_item, sights);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Sight sight = getItem(position);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        ImageView sightImg = convertView.findViewById(R.id.sightImage);
        TextView sightName = convertView.findViewById(R.id.txtSightName);
        TextView sightCity = convertView.findViewById(R.id.txtSightCity);

        sightImg.setImageResource(sight.getSightImg());
        sightName.setText(sight.getName());
        sightCity.setText(sight.getCity());

        return super.getView(position, convertView, parent);
    }
}
