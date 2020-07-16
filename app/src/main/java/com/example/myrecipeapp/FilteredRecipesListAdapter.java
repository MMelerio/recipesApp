package com.example.myrecipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.squareup.picasso.Picasso;

public class FilteredRecipesListAdapter extends ArrayAdapter<Recipe> {

    /**
     * Custom Adapter for Recipe Results ListView
     * Sets up recipe title and image in ListView item
     */

    public static final String TAG = "RecipeListAdapter";
    private final Context context;
    int resource;

    public FilteredRecipesListAdapter(Context context, int resource, Recipe[] recipes) {
        super(context, resource, recipes);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String title = getItem(position).getTitle();
        String imageURL = getItem(position).getImage();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView textView = convertView.findViewById(R.id.recipeTitle);
        ImageView imageView = convertView.findViewById(R.id.recipeImage);

        textView.setText(title);
        Picasso.get().load(imageURL).into(imageView);

        return convertView;
    }
}
