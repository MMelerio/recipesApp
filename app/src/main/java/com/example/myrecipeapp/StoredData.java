package com.example.myrecipeapp;

import java.util.ArrayList;

public class StoredData {

    private String user;
    private String title;
    private int recipe_id;
    private String image;
    private String usedIngredients;
    private String missedIngredients;

    public StoredData(){}

    public StoredData(String user, String title, int recipe_id, String image, String usedIngredients, String missedIngredients){
        this.user = user;
        this.title = title;
        this.recipe_id = recipe_id;
        this.image = image;
        this.usedIngredients = usedIngredients;
        this.missedIngredients = missedIngredients;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(int recipe_id) {
        this.recipe_id = recipe_id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsedIngredients() {
        return usedIngredients;
    }

    public void setUsedIngredients(String usedIngredients) {
        this.usedIngredients = usedIngredients;
    }

    public String getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(String missedIngredients) {
        this.missedIngredients = missedIngredients;
    }
}
