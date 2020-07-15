package com.example.myrecipeapp;

import android.util.Log;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.logging.Filter;

public class FullRecipeLoader implements Runnable {

    /**
     * Background thread that runs to make API calls for
     * individual recipes for FilterResultsActivity
     */

    // Create variables for URL elements
    private static final String url = "https://api.spoonacular.com/recipes/";
    private static final String API_KEY = "a3f6c7c9e522490c86b86681e683606b";
    private static final String apiCharset = "UTF-8";
    private static final String TAG = "FullRecipeLoader";
    private ArrayList<Integer> idList;
    private WeakReference<FilterResultsActivity> activityRef;
    public ArrayList<RecipeFull> recipeList = new ArrayList<>();

    FullRecipeLoader(FilterResultsActivity activity, ArrayList<Integer> idList) {
        this.idList = idList;
        this.activityRef = new WeakReference<>(activity);
    }

    @Override
    public void run() {

        Log.d(TAG, String.valueOf(idList));

        try {
            for (Integer id : idList) {
                String searchQuery = String.format("%s/information?includeNutrition=false&apiKey=%s",
                        URLEncoder.encode(String.valueOf(id), apiCharset),
                        URLEncoder.encode(API_KEY, apiCharset));

                Log.d(TAG, "Making a call to: " + url + searchQuery);

                // Connect to current autocomplete URL
                URLConnection conditionConnect = new URL(url + searchQuery).openConnection();
                conditionConnect.setRequestProperty("Accept-Charset", apiCharset);
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
                RecipeFull recipe = gson.fromJson(searchStringBuilder.toString(), RecipeFull.class);

                recipeList.add(recipe);
            }

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Getting the activity from the WeakReference
        final FilterResultsActivity activity = activityRef.get();

        // Check that activity was not destroyed
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Log attempt to display autocomplete result
                    Log.d(TAG, "Full Recipes extracted. Now updating UI");

                    activity.handleFullRecipes(recipeList);

                }
            });

        }

    }
}

