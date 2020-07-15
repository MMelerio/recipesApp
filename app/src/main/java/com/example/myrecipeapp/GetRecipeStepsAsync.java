package com.example.myrecipeapp;

import android.util.Log;

public class GetRecipeStepsAsync implements Runnable {

    private RecipeStepsActivity activity;

    private int recipe_id;

    /**
     * Sets up the runnable to be called. It needs the BaseActivity so it can run code on the
     * UI thread, and also the id so that it can get the recipe steps.
     * @param activity
     * @param recipe_id
     */
    public GetRecipeStepsAsync(RecipeStepsActivity activity, int recipe_id) {
        this.activity = activity;
        this.recipe_id = recipe_id;
    }

    @Override
    public void run() {
        // This is the function that will be run on the background thread.

        StepsLoader loader = new StepsLoader();

        // Now, call the function that will get the results from the API

        loader.getStepsAndPostResults(recipe_id, new StepsResultHandler() {
            @Override
            public void handleResult(final RecipeSteps[] recipe) {
                Log.d("GetRecipeAsync", "Back from API, but still on background thread.");
                // At this point we will be back from the API with the results stored in `elements`

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // This is code that will now run on the UI thread. Call the function in
                        // BaseActivity that will update the UI correctly.
                        activity.handleStepsListResult(recipe);
                    }
                });
            }
        });
    }
}
