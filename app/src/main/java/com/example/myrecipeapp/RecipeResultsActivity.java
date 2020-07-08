package com.example.myrecipeapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

    private static final String TAG = "RecipeResultsActivity";
    public ArrayList<String> ingredientsList = new ArrayList<>();
    public HashMap<String, Boolean> filters = new HashMap<>();
    public ImageButton filterButton;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        Log.d(TAG, "Received intent from SearchActivity");

        // Get the Intent that started this activity
        intent = getIntent();

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
        TextView textView = findViewById(R.id.recipesTextView);
        StringBuffer sb = new StringBuffer();

        for (Object s : ingredientsList) {
            sb.append(s);
            sb.append(", ");
        }

        String str = "Recipes with: " + sb.toString();
        textView.setText(str);

        // Add filter button for search results
        filterButton = findViewById(R.id.filterOptionsButton);

    }


    /**
     * Displays recipes in custom layout ListView
     * @param recipes
     */
    void handleRecipeListResult(final ArrayList<RecipeFull> recipes) {
        Log.d(TAG, "Back from API on the UI thread with the recipe results!");

        // Get filter info from Filter Activity's intent
        // If null, set default values to false
        if (intent.getSerializableExtra("filters") != null) {
            filters = (HashMap<String, Boolean>) intent.getSerializableExtra("filters");
        } else {
            filters.put("Dairy-free", false);
            filters.put("Gluten-free", false);
            filters.put("Vegan", false);
        }

        // Create arrayList for filtered recipes
        ArrayList<RecipeFull> filteredRecipes = new ArrayList<>();


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
            Toast.makeText(this, "An error occurred when retrieving the recipes",
                    Toast.LENGTH_LONG).show();
        }
        else if (filteredRecipes.size() == 0) {
            Log.d(TAG, "No recipe matches filters");

            // Inform the user that filters did not return any matches
            Toast.makeText(this, "No recipes match your ingredients and filters",
                    Toast.LENGTH_LONG).show();
        } else {
            Log.d(TAG, "Successfully retrieved recipes!");

            ListView listView = findViewById(R.id.searchResults);

            // Create the CustomAdapter for the Search Results
            RecipeListAdapter adapter = new RecipeListAdapter(this, R.layout.custom_recipe_layout, filteredRecipes);
            listView.setAdapter(adapter);


            // Launch new Activity when ListView item is clicked
            // @MARTIN: Change RecipeInfo.class to your class
             listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             // When clicked, open new Activity for Recipe's full info
             Intent intent = new Intent(getApplicationContext(), RecipeStepsActivity.class);
             intent.putExtra("recipe_id", recipes.get(position).getId());
             intent.putExtra("recipe_title", recipes.get(position).getTitle());
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
        intent.putExtra("ingredients", ingredientsList);
        startActivity(intent);
    }

}
