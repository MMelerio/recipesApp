package com.example.myrecipeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    private ArrayList<StoredData> data;
    private OnRecipeListener recipeListener;

    public BookmarksAdapter(ArrayList<StoredData> data, OnRecipeListener recipeListener) {
        this.data = data;
        this.recipeListener = recipeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bookmark, parent, false);
        return new ViewHolder(view, recipeListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.savedRecipeTitle.setText(data.get(position).getTitle());
        Picasso.get().load(data.get(position).getImage()).into(holder.savedRecipeImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView savedRecipeTitle;
        ImageView savedRecipeImage;
        OnRecipeListener recipeListener;

        public ViewHolder(@NonNull View itemView, OnRecipeListener recipeListener) {
            super(itemView);

            savedRecipeTitle = (TextView) itemView.findViewById(R.id.savedRecipeTitle);
            savedRecipeImage = (ImageView) itemView.findViewById(R.id.savedRecipeImage);
            this.recipeListener = recipeListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            recipeListener.onRecipeClick(getAdapterPosition());
        }
    }

    public interface OnRecipeListener{
        void onRecipeClick(int position);
    }
}
