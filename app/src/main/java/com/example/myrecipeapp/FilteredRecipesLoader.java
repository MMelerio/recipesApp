package com.example.myrecipeapp;

import android.text.TextUtils;
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
import java.util.HashMap;
import java.util.logging.Filter;

public class FilteredRecipesLoader implements Runnable {

    /**
     * Background thread that runs to make API calls for
     * individual recipes for FilterResultsActivity
     */

    // Create variables for URL elements
    private static final String url = "https://api.spoonacular.com/recipes/complexSearch";
    private static final String API_KEY = "a3f6c7c9e522490c86b86681e683606b";
    private static final String apiCharset = "UTF-8";

    private static final String TAG = "FullRecipeLoader";
    private String ingredients;
    private WeakReference<FilterResultsActivity> activityRef;
    public HashMap<String, Boolean> filters;
    public FiltersResult results;
    public Recipe[] recipes;

    FilteredRecipesLoader(FilterResultsActivity activity, ArrayList<String> ingredientsList, HashMap<String, Boolean> userFilters) {
        this.ingredients = TextUtils.join(",+ ", ingredientsList);
        this.activityRef = new WeakReference<>(activity);
        this.filters = userFilters;
    }

    @Override
    public void run() {

        try {
            String searchQuery = String.format("?includeIngredients=%s&apiKey=%s&number=20",
                    URLEncoder.encode(ingredients, apiCharset),
                    URLEncoder.encode(API_KEY, apiCharset));

            // Check for filters
            if (filters.get("Dairy-free")) {
                searchQuery += "&intolerances=dairy";
            }
            else if (filters.get("Gluten-free")) {
                searchQuery += "&intolerances=gluten";
            }
            else if (filters.get("Vegan")) {
                searchQuery += "&diet=vegan";
            }
            else {
                searchQuery += "";
            }

            Log.d(TAG, "Making a call to: " + url + searchQuery);

            // Connect to current recipes search call URL
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
            results = gson.fromJson(searchStringBuilder.toString(), FiltersResult.class);

            Log.d(TAG, String.valueOf(results.getRecipes()));

            recipes = results.getRecipes();

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

                    activity.handleFullRecipes(recipes);

                }
            });

        }

    }
}

