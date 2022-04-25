package com.example.earthquake.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;

import com.example.earthquake.R;
import com.example.earthquake.model.Earthquake;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {
    private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";

    private EarthquakeDataListAdapter adapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView textViewNoData;

    private View progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView earthquakeListView = findViewById(R.id.listview_data);

        textViewNoData =  findViewById(R.id.textview_no_data);
        earthquakeListView.setEmptyView(textViewNoData);

        progressbar = findViewById(R.id.progressbar_loading);

        adapter = new EarthquakeDataListAdapter(this, new ArrayList<Earthquake>());

        earthquakeListView.setAdapter(adapter);

        earthquakeListView.setOnItemClickListener((adapterView, view, position, id) -> {
            Earthquake currentEarthquake = adapter.getItem(position);
            Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
            if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(websiteIntent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            progressbar.setVisibility(View.GONE);

            textViewNoData.setText(R.string.no_internet);
        }

    }

    @NonNull
    @Override
    public androidx.loader.content.Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        return new EarthquakeDataLoader(this, uriBuilder.toString());
    }


    @Override
    public void onLoadFinished(@NonNull androidx.loader.content.Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        progressbar.setVisibility(View.GONE);
        textViewNoData.setText(R.string.no_data_available);

        adapter.clear();
        if (earthquakes != null && !earthquakes.isEmpty()) {
            adapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(@NonNull androidx.loader.content.Loader<List<Earthquake>> loader) {
        adapter.clear();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}