package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SuggestedRecipesActivity extends AppCompatActivity {

    private RecyclerView recipeRecyclerView;
    private Button backButton;

    private RecipeAdapter recipeAdapter;

    private final List<String> recipeNames = new ArrayList<>();
    private final List<String> recipeIngredients = new ArrayList<>();
    private final List<String> recipeQuantities = new ArrayList<>();
    private final List<String> recipeUnits = new ArrayList<>();
    private final List<String> recipeMethods = new ArrayList<>();

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_suggested_recipes);

        recipeRecyclerView =
                findViewById(R.id.recipeRecyclerView);

        backButton =
                findViewById(R.id.backButton);

        databaseHelper =
                new DatabaseHelper(this);

        recipeRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        loadMatchingRecipes();

        recipeAdapter = new RecipeAdapter(
                this,
                recipeNames,
                recipeIngredients,
                recipeQuantities,
                recipeUnits,
                recipeMethods
        );

        recipeRecyclerView.setAdapter(recipeAdapter);

        backButton.setOnClickListener(v -> finish());
    }

    private Map<String, Double> getPantryIngredients() {

        Map<String, Double> pantry =
                new HashMap<>();

        Cursor cursor =
                databaseHelper.getAllIngredients();

        while (cursor.moveToNext()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            double quantity =
                    cursor.getDouble(
                            cursor.getColumnIndexOrThrow("quantity")
                    );

            String unit =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("unit")
                    );

            String normalName =
                    normalizeIngredient(name);

            double comparableQuantity =
                    convertToComparableQuantity(
                            quantity,
                            unit
                    );

            pantry.put(
                    normalName,
                    pantry.getOrDefault(
                            normalName,
                            0.0
                    ) + comparableQuantity
            );
        }

        cursor.close();

        return pantry;
    }

    private boolean recipeCanBeMade(
            String ingredientsText,
            String quantitiesText,
            String unitsText,
            Map<String, Double> pantry) {

        String[] ingredients =
                ingredientsText.split(",");

        String[] quantities =
                quantitiesText.split(",");

        String[] units =
                unitsText.split(",");

        if (ingredients.length != quantities.length ||
                ingredients.length != units.length) {

            return false;
        }

        for (int i = 0; i < ingredients.length; i++) {

            String requiredName =
                    normalizeIngredient(
                            ingredients[i]
                    );

            double requiredQuantity;

            try {

                requiredQuantity =
                        Double.parseDouble(
                                quantities[i].trim()
                        );

            } catch (NumberFormatException e) {

                return false;
            }

            double comparableRequired =
                    convertToComparableQuantity(
                            requiredQuantity,
                            units[i].trim()
                    );

            if (!pantry.containsKey(requiredName)) {
                return false;
            }

            if (pantry.get(requiredName) <
                    comparableRequired) {

                return false;
            }
        }

        return true;
    }

    private String normalizeIngredient(
            String ingredient) {

        String name =
                ingredient
                        .trim()
                        .toLowerCase(Locale.ROOT);

        if (name.endsWith("ies")) {

            name = name.substring(
                    0,
                    name.length() - 3
            ) + "y";

        } else if (name.endsWith("oes")) {

            name = name.substring(
                    0,
                    name.length() - 2
            );

        } else if (name.endsWith("es")) {

            name = name.substring(
                    0,
                    name.length() - 2
            );

        } else if (name.endsWith("s")) {

            name = name.substring(
                    0,
                    name.length() - 1
            );
        }

        return name;
    }

    private double convertToComparableQuantity(
            double quantity,
            String unit) {

        String normalizedUnit =
                unit.trim()
                        .toLowerCase(Locale.ROOT);

        if (normalizedUnit.equals("kg")) {
            return quantity * 1000;
        }

        if (normalizedUnit.equals("g")) {
            return quantity;
        }

        if (normalizedUnit.equals("l") ||
                normalizedUnit.equals("litre") ||
                normalizedUnit.equals("litres")) {

            return quantity * 1000;
        }

        if (normalizedUnit.equals("ml")) {
            return quantity;
        }

        return quantity;
    }

    private void loadMatchingRecipes() {

        Map<String, Double> pantry =
                getPantryIngredients();

        Cursor cursor =
                databaseHelper.getAllRecipes();

        while (cursor.moveToNext()) {

            String name =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("name")
                    );

            String ingredients =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "ingredients"
                            )
                    );

            String quantities =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "quantities"
                            )
                    );

            String units =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("units")
                    );

            String method =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow("method")
                    );

            if (recipeCanBeMade(
                    ingredients,
                    quantities,
                    units,
                    pantry
            )) {

                recipeNames.add(name);
                recipeIngredients.add(ingredients);
                recipeQuantities.add(quantities);
                recipeUnits.add(units);
                recipeMethods.add(method);
            }
        }

        cursor.close();

        if (recipeNames.isEmpty()) {

            Toast.makeText(
                    this,
                    "No recipes match your pantry yet - add more ingredients.",
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}