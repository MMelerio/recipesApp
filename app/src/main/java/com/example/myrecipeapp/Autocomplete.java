package com.example.myrecipeapp;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Autocomplete implements Runnable {

    /**
     * Background thread that runs to get autocomplete
     * based on user's input in the Search bar
     */

    // Create variables for URL elements
    private static final String url = "https://api.spoonacular.com/food/ingredients/";
    private static final String key = "cab6a9c0a9f8487fb902b9f9a2558a58";
    private static final String charset = "UTF-8";
    private static final String TAG = "Autocomplete";
    private final String input;
    private WeakReference<SearchActivity> activityRef;
    public String first_hit;

    Autocomplete(String input, SearchActivity activity) {
        this.input = input;
        this.activityRef = new WeakReference<>(activity);
    }


    @Override
    public void run() {
        Log.d(TAG, "Now processing autocomplete");

        // Number of words returned
        String num = "1";

        try {
            // Create query for autocomplete query
            String searchQuery = String.format("autocomplete?apiKey=%s&query=%s&number=%s",
                    URLEncoder.encode(key, charset),
                    URLEncoder.encode(input, charset),
                    URLEncoder.encode(num, charset));

            // Connect to current autocomplete URL
            URLConnection conditionConnect = new URL(url + searchQuery).openConnection();
            conditionConnect.setRequestProperty("Accept-Charset", charset);
            InputStream searchResponse = conditionConnect.getInputStream();

            // Read stream from response
            InputStreamReader searchStream = new InputStreamReader(searchResponse);

            BufferedReader readSearch = new BufferedReader(searchStream);

            StringBuilder searchStringBuilder = new StringBuilder();

            String searchString;

            // Add the data to string
            while ((searchString = readSearch.readLine()) != null) {
                searchStringBuilder.append(searchString);
            }

            // Create GSON object
            Gson gson = new Gson();

            // Deserialize JSON data to array of Ingredient objects
            Ingredient[] ingredients = gson.fromJson(searchStringBuilder.toString(), Ingredient[].class);
            this.first_hit = ingredients[0].getName();


        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Getting the activity from the WeakReference
        final SearchActivity activity = activityRef.get();

        // Check that activity was not destroyed
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Log attempt to display autocomplete result
                    Log.d(TAG, "Autocomplete first hit extracted. Now displaying on UI");

                    activity.handleAutocompleteResult(first_hit);

                }
            });

        }

    }

}
