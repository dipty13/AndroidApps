package com.example.earthquake.controller;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.earthquake.R;
import com.example.earthquake.model.Earthquake;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EarthquakeDataListAdapter extends ArrayAdapter<Earthquake> {
    public EarthquakeDataListAdapter(Context context, List<Earthquake> earthquakes) {
        super(context, 0, earthquakes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.earthquake_list_item, parent, false);
        }
        Earthquake currentEarthquake = getItem(position);

        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        double magnitude = currentEarthquake.getMagnitude();
        String formattedMagnitude = decimalFormat.format(magnitude);

        TextView magnitudeView = view.findViewById(R.id.textview_magnitude);
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        magnitudeCircle.setColor(magnitudeColor);
        magnitudeView.setText(formattedMagnitude);

        String offset="", primary;

        String location = currentEarthquake.getPlace();
        if(location != null && location.contains("of"))
        {
            int length = location.length();
            int temp = location.indexOf("of");
            System.out.println(length);
            offset = location.substring(0,temp+2);
            primary = location.substring(temp+3, location.length());
        }
        else
        {
            offset = "Near the";
            primary = location;
        }

        TextView offsetView =  view.findViewById(R.id.textview_offset);
        offsetView.setText(offset);

        TextView locationView  = view.findViewById(R.id.textview_location);
        locationView.setText(primary);

        Date date = new Date(currentEarthquake.getTime());

        TextView dateView = view.findViewById(R.id.date);
        String formattedDate = formatDate(date);

        dateView.setText(formattedDate);

        TextView timeView = view.findViewById(R.id.time);
        String formattedTime = formatTime(date);
        timeView.setText(formattedTime);

        return view;

    }

    private String formatTime(Date time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(time);
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(date);
    }

    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch(magnitudeFloor)
        {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }
}
