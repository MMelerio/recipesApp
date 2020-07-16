package com.example.myrecipeapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Switch;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class FiltersActivity extends AppCompatActivity {

    /**
     * Displays filters for user to modify search results
     * Launched from RecipeResultsActivity
     */

    private static final String TAG = "FiltersActivity";
    public HashMap<String, Boolean> filters = new HashMap<>();
    public Button filterResultsButton;
    public ArrayList<String> ingredients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);
        Log.d(TAG, "Received intent from RecipeResultsActivity");

        // Get the Intent that started this activity
        Intent intent = getIntent();
        ingredients = intent.getStringArrayListExtra("ingredients");

        // Initialize Filters button
        filterResultsButton = findViewById(R.id.filterButton);

    }

    /**
     * Checks for user's filter choices then passes on this info
     * thru intent to launch FiltersResultsActivity
     * @param view
     */
    public void filter(View view) {

        CheckBox dairy = findViewById(R.id.dairy);
        if(dairy.isChecked()){
            filters.put("Dairy-free", true);
        } else {
            filters.put("Dairy-free", false);
        }

        CheckBox gluten = findViewById(R.id.gluten);
        if(gluten.isChecked()){
            filters.put("Gluten-free", true);
        } else {
            filters.put("Gluten-free", false);
        }

        CheckBox vegan = findViewById(R.id.vegan);
        if(vegan.isChecked()){
            filters.put("Vegan", true);
        } else {
            filters.put("Vegan", false);
        }

        Log.d(TAG, "Users filters: " + filters);

        // Launch FilterResultsActivity to display results based on filters
        Log.d(TAG, "Creating intent for FilterResultsActivity to display results with filters");

        Intent intent = new Intent(this, FilterResultsActivity.class);
        intent.putExtra("filters", filters);
        intent.putExtra("ingredients", ingredients);
        startActivity(intent);
    }

}
