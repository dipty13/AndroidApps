package com.example.earthquake.controller;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.example.earthquake.model.Earthquake;
import com.example.earthquake.util.QueryHelper;

import java.util.List;

public class EarthquakeDataLoader extends AsyncTaskLoader<List<Earthquake>> {
    private final String url;

    public EarthquakeDataLoader(@NonNull Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if(url == null) {
            return null;
        }
        return QueryHelper.fetchEarthquakeData(url);
    }
}
