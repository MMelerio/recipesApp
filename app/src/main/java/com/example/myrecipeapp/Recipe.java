package com.example.myrecipeapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Recipe {

    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private String title;
    @SerializedName("image")
    private String image;
    @SerializedName("imageType")
    private String imageType;
    @SerializedName("usedIngredientCount")
    private Integer usedIngredientCount;
    @SerializedName("missedIngredientCount")
    private Integer missedIngredientCount;
    @SerializedName("missedIngredients")
    private ArrayList<MissedIngredient> missedIngredients = null;
    @SerializedName("usedIngredients")
    private ArrayList<UsedIngredient> usedIngredients = null;
    @SerializedName("unusedIngredients")
    private List<UnusedIngredient> unusedIngredients = null;
    @SerializedName("likes")
    private Integer likes;
    @SerializedName("additionalProperties")
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public Integer getUsedIngredientCount() {
        return usedIngredientCount;
    }

    public void setUsedIngredientCount(Integer usedIngredientCount) {
        this.usedIngredientCount = usedIngredientCount;
    }

    public Integer getMissedIngredientCount() {
        return missedIngredientCount;
    }

    public void setMissedIngredientCount(Integer missedIngredientCount) {
        this.missedIngredientCount = missedIngredientCount;
    }

    public ArrayList<MissedIngredient> getMissedIngredients() {
        return missedIngredients;
    }

    public void setMissedIngredients(ArrayList<MissedIngredient> missedIngredients) {
        this.missedIngredients = missedIngredients;
    }

    public ArrayList<UsedIngredient> getUsedIngredients() {
        return usedIngredients;
    }

    public void setUsedIngredients(ArrayList<UsedIngredient> usedIngredients) {
        this.usedIngredients = usedIngredients;
    }

    public List<UnusedIngredient> getUnusedIngredients() {
        return unusedIngredients;
    }

    public void setUnusedIngredients(List<UnusedIngredient> unusedIngredients) {
        this.unusedIngredients = unusedIngredients;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
