package com.example.myrecipeapp;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeStepsActivity extends BaseActivity {

    /**
     * Launches GetRecipeStepsAsync to get the steps from API call
     * then displays them in a listView
     */

    private static final String TAG = "RecipeStepsActivity";

    public int recipe_id;
    public String title;
    public String image;
    public String user;
    public ArrayList<String> UsedIngredients;
    public ArrayList<String> MissedIngredients;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    String displayUsedIngredients;
    String displayMissedIngredients;
    String origin;
    RecipeStepsActivity mActivity;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private Query mDatabaseQuery;
    private ChildEventListener mChildEventListener;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_recipe_steps, contentFrameLayout);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("recipesdb");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mActivity = this;


        Log.d(TAG, "Received intent from RecipeResultsActivity");

        // Get the Intent that started this activity
        Intent intent = getIntent();
        origin = intent.getStringExtra("activity");
        recipe_id = intent.getIntExtra("recipe_id", 0);

        if(origin.equals("SearchResult")) {
            // When it's coming from the search results and the info is coming from the previous activity
            title = intent.getStringExtra("recipe_title");
            image = intent.getStringExtra("recipe_image");
            UsedIngredients = intent.getStringArrayListExtra("UsedIngredients");
            MissedIngredients = intent.getStringArrayListExtra("missedIngredients");

            displayMissedIngredients = "";
            displayUsedIngredients= "";

            for (String s1 : UsedIngredients) {
                if(s1 != null) {
                    displayUsedIngredients += s1 + "\n";
                }
            }

            for (String s2 : MissedIngredients) {
                if(s2 != null) {
                    displayUsedIngredients += s2 + "\n";
                }

                TextView textView = findViewById(R.id.recipeTitle);
                textView.setText(title);

                ImageView imageView = findViewById(R.id.recipeImage);
                Picasso.get().load(Uri.parse(image)).into(imageView);

                TextView textView2 = findViewById(R.id.usedIngredients);
                textView2.setText(displayUsedIngredients);

                // Set up a new instance of our runnable object that will be run on the background thread
                GetRecipeStepsAsync getRecipeStepsAsync = new GetRecipeStepsAsync(mActivity, recipe_id);

                // Set up the thread that will use our runnable object
                Thread t = new Thread(getRecipeStepsAsync);

                // starts the thread in the background. It will automatically call the run method of
                // the getRecipeAsync object we gave it earlier
                t.start();

            }
        } else {
            // When it's coming from the Bookmarks activity and the info is coming from the database.
            mDatabaseQuery = mFirebaseDatabase.getReference().child("recipesdb").orderByChild("recipe_id").equalTo(recipe_id);
            mDatabaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        StoredData data = dataSnapshot.getValue(StoredData.class);
                        title = data.getTitle();
                        image = data.getImage();
                        displayUsedIngredients = data.getUsedIngredients();
                        displayMissedIngredients = data.getMissedIngredients();

                    }

                    TextView textView = findViewById(R.id.recipeTitle);
                    textView.setText(title);

                    ImageView imageView = findViewById(R.id.recipeImage);
                    Picasso.get().load(Uri.parse(image)).into(imageView);

                    TextView textView2 = findViewById(R.id.usedIngredients);
                    textView2.setText(displayUsedIngredients);

                    // Set up a new instance of our runnable object that will be run on the background thread
                    GetRecipeStepsAsync getRecipeStepsAsync = new GetRecipeStepsAsync(mActivity, recipe_id);

                    // Set up the thread that will use our runnable object
                    Thread t = new Thread(getRecipeStepsAsync);

                    // starts the thread in the background. It will automatically call the run method of
                    // the getRecipeAsync object we gave it earlier
                    t.start();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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
        user = mUser.getEmail();

        StoredData data = new StoredData(user, title, recipe_id, image, displayUsedIngredients, displayMissedIngredients);

        mDatabaseReference.push().setValue(data);

        Toast.makeText(this, "Recipe Saved", Toast.LENGTH_LONG).show();
    }
}