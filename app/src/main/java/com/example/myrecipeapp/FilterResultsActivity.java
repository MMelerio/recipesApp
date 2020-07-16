package com.example.myrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;

public class FilterResultsActivity extends AppCompatActivity {

    /**
     * Display filtered results to user
     * Uses FilteredRecipesLoader runnable
     */

    private static final String TAG = "FilterResultsActivity";
    public HashMap<String, Boolean> filters = new HashMap<>();
    public ArrayList<String> ingredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtered_recipes);
        Log.d(TAG, "Received intent from SearchActivity");

        // Get the Intent that started this activity
        Intent intent = getIntent();

        // Get filters from FiltersActivity
        filters = (HashMap<String, Boolean>) intent.getSerializableExtra("filters");
        Log.d(TAG, "Received filters from FiltersActivity");

        // Get ingredients from FiltersActivity
        ingredients = intent.getStringArrayListExtra("ingredients");
        Log.d(TAG, "Received ingredientsList from FiltersActivity");

        // Set up a new instance of FilteredRecipesLoader runnable object that will be run on the background thread
        FilteredRecipesLoader filteredRecipesLoader = new FilteredRecipesLoader(this, (ArrayList<String>) ingredients, filters);

        // Set up the thread that will use our runnable object
        Thread t = new Thread(filteredRecipesLoader);
        t.start();

    }

    /**
     * Displays recipes in custom layout ListView
     * @param recipes
     */
    void handleFullRecipes(final Recipe[] recipes) {
        Log.d(TAG, "Completed API call for filtered search results");

        // Check for an error
        if (recipes == null) {
            Log.d(TAG, "API results were null");

            // Inform the user
            Toast.makeText(this, "An error occurred while retrieving the recipes",
                    Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "Successfully retrieved filtered recipes!");

            ListView listView = findViewById(R.id.filterResults);

            // Create the CustomAdapter for the Search Results
            FilteredRecipesListAdapter adapter = new FilteredRecipesListAdapter(this, R.layout.custom_recipe_layout, recipes);
            listView.setAdapter(adapter);

            // Launch new Activity when ListView item is clicked
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, open new Activity for Recipe's full info
                    Intent intent = new Intent(getApplicationContext(), RecipeStepsActivity.class);
                    intent.putExtra("recipe_id", recipes[position].getId());
                    intent.putExtra("recipe_title", recipes[position].getTitle());
                    startActivity(intent);
                }
            });

        }

    }
}
