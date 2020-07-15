package com.example.myrecipeapp;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

public class GetRecipeAsync implements Runnable {

    private RecipeResultsActivity activity;

    public ArrayList<String> ingredientsList;

    public String ingredients;


    /**
     * Sets up the runnable to be called. It needs the MainActivity so it can run code on the
     * UI thread, and also the id so that it can get the recipe steps.
     * @param activity
     * @param ingredientsList
     */
    public GetRecipeAsync(RecipeResultsActivity activity, ArrayList<String> ingredientsList) {
        this.activity = activity;
        this.ingredientsList = ingredientsList;
        this.ingredients = TextUtils.join(",+ ", ingredientsList);
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {
        // This is the function that will be run on the background thread.

        RecipeLoader loader = new RecipeLoader();

        // Now, call the function that will get the results from the API
        loader.getRecipeAndPostResults(ingredients, new RecipeResultHandler() {

            @Override
            public void handleResult(final Recipe[] recipes) {
                Log.d("GetRecipeAsync", "Back from API, but still on background thread.");
                // At this point we will be back from the API with the results stored in `elements`

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This is code that will now run on the UI thread. Call the function in
                        // MainActivity that will update the UI correctly.
                        activity.handleRecipeListResult(recipes);
                    }
                });
            }
        });
    }
}

