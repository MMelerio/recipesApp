package com.example.myrecipeapp;

import android.content.Context;
import android.text.Layout;
import android.widget.ArrayAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecipeListAdapter extends ArrayAdapter<RecipeFull> {

    /**
     * Custom Adapter for Recipe Results ListView
     * Sets up recipe title and image in ListView item
     */
    public static final String TAG = "RecipeListAdapter";
    private final Context context;
    int resource;

    public RecipeListAdapter(Context context, int resource, ArrayList<RecipeFull> recipes) {
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
