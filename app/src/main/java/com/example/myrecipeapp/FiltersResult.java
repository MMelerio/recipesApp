package com.example.myrecipeapp;

import com.google.gson.annotations.SerializedName;

public class FiltersResult {

    /**
     * JSON object that contains an array of Recipe objects
     */

    @SerializedName("results")
    private Recipe[] recipes;

    public Recipe[] getRecipes() {
        return recipes;
    }

    public void setRecipes(Recipe[] recipes) {
        this.recipes = recipes;
    }
}
