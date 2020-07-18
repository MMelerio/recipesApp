package com.example.myrecipeapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Bookmarks extends AppCompatActivity implements BookmarksAdapter.OnRecipeListener {

    private FirebaseDatabase mFirebaseDatabase;
    private Query mQuery;
    private FirebaseUser mUser;
    private String user;
    ArrayList<StoredData> savedRecipes;
    BookmarksAdapter adapter;
    RecyclerView recyclerView;
    BookmarksAdapter.OnRecipeListener recipeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        recyclerView = (RecyclerView) findViewById(R.id.RecyclerSaved);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mUser = FirebaseAuth.getInstance().getCurrentUser();

        user = mUser.getEmail();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mQuery = mFirebaseDatabase.getReference().child("recipesdb").orderByChild("user").equalTo(user);

        savedRecipes = new ArrayList<>();

        recipeListener = this;

        mQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    StoredData data = dataSnapshot.getValue(StoredData.class);
                    savedRecipes.add(data);
                }

                adapter = new BookmarksAdapter(savedRecipes, recipeListener);
                recyclerView.setAdapter(adapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(getApplicationContext(), RecipeStepsActivity.class);
        intent.putExtra("activity", "Bookmark");
        intent.putExtra("recipe_id", savedRecipes.get(position).getRecipe_id());
        startActivity(intent);
    }
}