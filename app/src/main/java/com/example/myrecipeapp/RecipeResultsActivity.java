package com.example.myrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class RecipeResultsActivity extends AppCompatActivity {

    /**
     * Display recipe search results based on user's
     * ingredients
     */

    private static final String TAG = "RecipeResultsActivity";
    public ArrayList<String> ingredientsList = new ArrayList<>();
    public ImageButton filterButton;
    public Recipe[] recipeList;
    public TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        Log.d(TAG, "Received intent from SearchActivity");

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Get ingredient list from Search Activity
        ingredientsList = intent.getStringArrayListExtra("ingredients");
        Log.d(TAG, "Received ingredient list from SearchActivity");

        // Set up a new instance of our runnable object that will be run on the background thread
        GetRecipeAsync getRecipeAsync = new GetRecipeAsync(this, (ArrayList<String>) ingredientsList);

        // Set up the thread that will use our runnable object
        Thread t = new Thread(getRecipeAsync);

        // starts the thread in the background. It will automatically call the run method of
        // the getRecipeAsync object we gave it earlier
        t.start();

        // Display the ingredients user has typed for search results
        textView = findViewById(R.id.recipesTextView);
        StringBuffer sb = new StringBuffer();
        for (Object s : ingredientsList) {
            sb.append(s);
            sb.append(", ");
        }
        String str = "Recipes with: " + sb.toString();

        // Remove last comma at the end of ingredients list
        textView.setText(str.substring(0, str.length() - 2));

        // Add filter button for search results
        filterButton = findViewById(R.id.filterOptionsButton);

    }


    /**
     * Displays recipes in custom layout ListView
     * @param recipes
     */
    void handleRecipeListResult(final Recipe[] recipes) {
        Log.d(TAG, "Back from API on the UI thread with the recipe results!");

        recipeList = recipes;

        // Check for an error
        if (recipes == null) {
            Log.d(TAG, "API results were null");

            // Inform the user
            Toast.makeText(this, "An error occurred when retrieving the recipes",
                    Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "Successfully retrieved recipes!");

            ListView listView = findViewById(R.id.searchResults);

            // Create the CustomAdapter for the Search Results
            RecipeListAdapter adapter = new RecipeListAdapter(this, R.layout.custom_recipe_layout, recipes);
            listView.setAdapter(adapter);


            // Launch new Activity when ListView item is clicked
            // to display recipe's full info
             listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, open new Activity for Recipe's full info

                 // Store the name of the ingredients in an array of strings to pass it to the next activity.
                 // The purpose of this is to avoid an additional API call to retrieve the ingredients based on the recipe ID
                ArrayList<UsedIngredient> usedIngredients = recipes[position].getUsedIngredients();
                ArrayList<String> usedIngredientsNames = new ArrayList<>();

                 for (int i = 0; i < usedIngredients.size(); i++) {
                     if(usedIngredients.get(i).getName() != null) {
                         usedIngredientsNames.add(usedIngredients.get(i).getName());
                     }
                 }


                 ArrayList<MissedIngredient> MissedIngredients = recipes[position].getMissedIngredients();
                 ArrayList<String> MissedIngredientsNames = new ArrayList<>();

                 for (int i = 0; i < MissedIngredients.size(); i++) {
                     if (MissedIngredients.get(i).getName() != null)
                     MissedIngredientsNames.add(MissedIngredients.get(i).getName());
                 }


                Intent intent = new Intent(getApplicationContext(), RecipeStepsActivity.class);
                intent.putExtra("activity", "SearchResult");
                intent.putExtra("recipe_id", recipes[position].getId());
                intent.putExtra("recipe_title", recipes[position].getTitle());
                intent.putExtra("recipe_image", recipes[position].getImage());
                intent.putStringArrayListExtra("UsedIngredients", usedIngredientsNames);
                intent.putStringArrayListExtra("missedIngredients", MissedIngredientsNames);
                startActivity(intent);
             }
             });

        }

    }

    /**
     * Method for filter option button in Recipe Results Activity
     * Launches Filter Activity and passes ingredientList
     * @param view
     */
    public void filterResults(View view) {
        Log.d(TAG, "About to create intent for Filters Activity");

        Intent intent = new Intent(this, FiltersActivity.class);
        Log.d(TAG, String.valueOf(recipeList));
        intent.putExtra("ingredients", ingredientsList);
        startActivity(intent);
    }

}
