package com.example.earthquake.util;

import static com.example.earthquake.util.Constants.FEATURES;
import static com.example.earthquake.util.Constants.MAGNITUDE;
import static com.example.earthquake.util.Constants.PLACE;
import static com.example.earthquake.util.Constants.PROPERTIES;
import static com.example.earthquake.util.Constants.TIME;

import android.util.Log;

import com.example.earthquake.model.Earthquake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryHelper {
    private QueryHelper(){
    }

    public static List<Earthquake> fetchEarthquakeData(String requestUrl)
    {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }
        catch (IOException exception)
        {
            Log.e(QueryHelper.class.getName(), "Error doing HTTP request", exception);
        }

        return extractFeatureFromJSON(jsonResponse);
    }

    private static String makeHttpRequest(URL url) throws IOException {
        if(url == null){
            return "";
        }

        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200)
            {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
            {
                Log.e(QueryHelper.class.getName(),
                        "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(QueryHelper.class.getName(), "Error getting earthquake data!");
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(QueryHelper.class.getName(), "Error creating the url!");
            e.printStackTrace();
        }
        return url;
    }

    private static List<Earthquake> extractFeatureFromJSON(String jsonResponse) {
        if(null == jsonResponse){
            return null;
        }
        List<Earthquake> earthquakeData = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            JSONArray jsonArray = jsonObject.getJSONArray(FEATURES);

            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject quake = jsonArray.getJSONObject(i);
                JSONObject props = quake.getJSONObject(PROPERTIES);
                double mag = props.getDouble(MAGNITUDE);
                String place = props.getString(PLACE);
                long time = props.getLong(TIME);
                String url = props.getString(Constants.URL);
                Earthquake earthquake = new Earthquake(mag, place, time, url);
                earthquakeData.add(earthquake);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return earthquakeData;
    }
}
