package com.example.myrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FilterResultsActivity extends AppCompatActivity {

    private static final String TAG = "FilterResultsActivity";
    public HashMap<String, Boolean> filters = new HashMap<>();
    public ArrayList<Integer> idList = new ArrayList<>();
    public ArrayList<RecipeFull> filteredRecipes = new ArrayList<>();


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

        // Get idList from FiltersActivity
        idList = intent.getIntegerArrayListExtra("idList");
        Log.d(TAG, "Received idList from FiltersActivity");

        // Set up a new instance of our runnable object that will be run on the background thread
        FullRecipeLoader fullRecipeLoader = new FullRecipeLoader(this, (ArrayList<Integer>) idList);

        // Set up the thread that will use our runnable object
        Thread t = new Thread(fullRecipeLoader);
        t.start();

    }

    /**
     * Displays recipes in custom layout ListView
     * @param recipes
     */
    void handleFullRecipes(final ArrayList<RecipeFull> recipes) {
        Log.d(TAG, "Completed API calls for individual recipes");

        // Check for filters
        if (filters.get("Dairy-free")) {
            for (RecipeFull recipe : recipes) {
                if (recipe.getDairyFree()) {
                    filteredRecipes.add(recipe);
                }
            }
        }
        else if (filters.get("Gluten-free")) {
            for (RecipeFull recipe : recipes) {
                if (recipe.getGlutenFree()) {
                    filteredRecipes.add(recipe);
                }
            }
        }
        else if (filters.get("Vegan")) {
            for (RecipeFull recipe : recipes) {
                if (recipe.getVegan()) {
                    filteredRecipes.add(recipe);
                }
            }
        }
        else {
            filteredRecipes = recipes;
        }


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
            FilteredRecipesListAdapter adapter = new FilteredRecipesListAdapter(this, R.layout.custom_recipe_layout, filteredRecipes);
            listView.setAdapter(adapter);


            // Launch new Activity when ListView item is clicked
            // @MARTIN: Change RecipeInfo.class to your class
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // When clicked, open new Activity for Recipe's full info
                    Intent intent = new Intent(getApplicationContext(), RecipeStepsActivity.class);
                    intent.putExtra("recipe_id", filteredRecipes.get(position).getId());
                    intent.putExtra("recipe_title", filteredRecipes.get(position).getTitle());
                    startActivity(intent);
                }
            });

        }

    }
}
