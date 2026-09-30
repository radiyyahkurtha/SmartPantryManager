package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeNameText;
    private TextView recipeIngredientsText;
    private TextView recipeMethodText;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_detail);

        recipeNameText = findViewById(R.id.recipeNameText);
        recipeIngredientsText =
                findViewById(R.id.recipeIngredientsText);
        recipeMethodText =
                findViewById(R.id.recipeMethodText);

        backButton = findViewById(R.id.backButton);

        String recipeName =
                getIntent().getStringExtra("recipeName");

        String recipeIngredients =
                getIntent().getStringExtra("recipeIngredients");

        String recipeQuantities =
                getIntent().getStringExtra("recipeQuantities");

        String recipeUnits =
                getIntent().getStringExtra("recipeUnits");

        String recipeMethod =
                getIntent().getStringExtra("recipeMethod");

        if (recipeName == null) {
            recipeName = "Recipe";
        }

        if (recipeIngredients == null) {
            recipeIngredients = "";
        }

        if (recipeQuantities == null) {
            recipeQuantities = "";
        }

        if (recipeUnits == null) {
            recipeUnits = "";
        }

        if (recipeMethod == null) {
            recipeMethod =
                    "Preparation method not available.";
        }

        recipeNameText.setText(recipeName);

        recipeIngredientsText.setText(
                buildIngredientList(
                        recipeIngredients,
                        recipeQuantities,
                        recipeUnits
                )
        );

        recipeMethodText.setText(recipeMethod);

        backButton.setOnClickListener(v -> finish());
    }

    private String buildIngredientList(
            String ingredientsText,
            String quantitiesText,
            String unitsText) {

        String[] ingredients =
                ingredientsText.split(",");

        String[] quantities =
                quantitiesText.split(",");

        String[] units =
                unitsText.split(",");

        StringBuilder result =
                new StringBuilder();

        for (int i = 0; i < ingredients.length; i++) {

            result.append("• ")
                    .append(ingredients[i].trim());

            if (i < quantities.length) {

                result.append(" - ")
                        .append(quantities[i].trim());

                if (i < units.length) {

                    result.append(" ")
                            .append(units[i].trim());
                }
            }

            result.append("\n");
        }

        return result.toString().trim();
    }
}