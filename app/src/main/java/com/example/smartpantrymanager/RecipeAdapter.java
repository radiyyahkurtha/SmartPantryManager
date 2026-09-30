package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecipeAdapter
        extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final ArrayList<String> recipeNames;
    private final ArrayList<String> recipeIngredients;
    private final ArrayList<String> recipeQuantities;
    private final ArrayList<String> recipeUnits;
    private final ArrayList<String> recipeMethods;

    private final Context context;

    public RecipeAdapter(
            Context context,
            List<String> recipeNames,
            List<String> recipeIngredients,
            List<String> recipeQuantities,
            List<String> recipeUnits,
            List<String> recipeMethods) {

        this.context = context;

        this.recipeNames =
                new ArrayList<>(recipeNames);

        this.recipeIngredients =
                new ArrayList<>(recipeIngredients);

        this.recipeQuantities =
                new ArrayList<>(recipeQuantities);

        this.recipeUnits =
                new ArrayList<>(recipeUnits);

        this.recipeMethods =
                new ArrayList<>(recipeMethods);
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view =
                LayoutInflater.from(
                        parent.getContext()
                ).inflate(
                        R.layout.item_recipe,
                        parent,
                        false
                );

        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RecipeViewHolder holder,
            int position) {

        String recipeName =
                recipeNames.get(position);

        String ingredients =
                recipeIngredients.get(position);

        holder.recipeName.setText(recipeName);

        holder.recipeIngredients.setText(
                ingredients.replace(",", ", ")
        );

        holder.itemView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            RecipeDetailActivity.class
                    );

            intent.putExtra(
                    "recipeName",
                    recipeName
            );

            intent.putExtra(
                    "recipeIngredients",
                    recipeIngredients.get(position)
            );

            intent.putExtra(
                    "recipeQuantities",
                    recipeQuantities.get(position)
            );

            intent.putExtra(
                    "recipeUnits",
                    recipeUnits.get(position)
            );

            intent.putExtra(
                    "recipeMethod",
                    recipeMethods.get(position)
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeNames.size();
    }

    public static class RecipeViewHolder
            extends RecyclerView.ViewHolder {

        TextView recipeName;
        TextView recipeIngredients;

        public RecipeViewHolder(
                @NonNull View itemView) {

            super(itemView);

            recipeName =
                    itemView.findViewById(
                            R.id.recipeName
                    );

            recipeIngredients =
                    itemView.findViewById(
                            R.id.recipeIngredients
                    );
        }
    }
}