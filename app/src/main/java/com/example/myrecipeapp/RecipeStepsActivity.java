package com.example.myrecipeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecipeStepsActivity extends BaseActivity {

    /**
     * Launches GetRecipeStepsAsync to get the steps from API call
     * then displays them in a listView
     */

    private static final String TAG = "RecipeStepsActivity";

    public int recipe_id;
    public String title;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_recipe_steps, contentFrameLayout);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("recipesdb");

        Log.d(TAG, "Received intent from RecipeResultsActivity");

        // Get the Intent that started this activity
        Intent intent = getIntent();
        recipe_id = intent.getIntExtra("recipe_id", 0);
        title = intent.getStringExtra("recipe_title");

        TextView textView = findViewById(R.id.recipeTitle);
        textView.setText(title);


        // Set up a new instance of our runnable object that will be run on the background thread
        GetRecipeStepsAsync getRecipeStepsAsync = new GetRecipeStepsAsync(this, recipe_id);

        // Set up the thread that will use our runnable object
        Thread t = new Thread(getRecipeStepsAsync);

        // starts the thread in the background. It will automatically call the run method of
        // the getRecipeAsync object we gave it earlier
        t.start();

    }

    void handleStepsListResult(RecipeSteps[] recipe) {
        Log.d("RecipeStepsActivity", "Back from API on the UI thread with the steps results!");

        // Check for an error
        if (recipe == null) {
            Log.d("RecipeStepsActivity", "API results were null");

            // Inform the user
            Toast.makeText(this, "An error occurred when retrieving the recipes",
                    Toast.LENGTH_LONG).show();
        } else {
            Log.d("RecipeStepsActivity", "recipes: ");

            ArrayList<Step> steps = recipe[0].getSteps();
            ArrayList<String> stepString = new ArrayList<>();


            for (int i = 0; i < steps.size(); i++) {
                stepString.add(steps.get(i).getStep());
            }


            mRecyclerView = findViewById(R.id.stepDetails);
            mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new StepsAdapter(stepString);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

        }
    }


    public void saveRecipe(View view) {
        mDatabaseReference.push().setValue(recipe_id);
    }
}