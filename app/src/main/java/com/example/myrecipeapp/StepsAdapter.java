package com.example.myrecipeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    ArrayList<String> stepString;

    public StepsAdapter(ArrayList<String> stepString) {
        this.stepString = stepString;
    }

    @NonNull
    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_steps_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.ViewHolder holder, int position) {
        holder.steps.setText(stepString.get(position));
    }

    @Override
    public int getItemCount() {
        return stepString.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView steps;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            steps = (TextView) itemView.findViewById(R.id.steps);
        }
    }
}
